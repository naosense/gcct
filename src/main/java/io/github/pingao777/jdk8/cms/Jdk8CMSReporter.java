package io.github.pingao777.jdk8.cms;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import io.github.pingao777.common.enums.Phrase;
import io.github.pingao777.common.model.CommandLineFlags;
import io.github.pingao777.common.model.JdkInfo;
import io.github.pingao777.common.model.MemoryInfo;
import io.github.pingao777.common.model.ResultData;
import io.github.pingao777.common.report.Report;
import io.github.pingao777.common.report.Templates;
import io.github.pingao777.common.util.DoubleStatistics;
import io.github.pingao777.common.util.ParseUtils;
import io.github.pingao777.jdk8.cms.model.AllocationFailure;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentAbortable;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentMark;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentPreclean;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentReset;
import io.github.pingao777.jdk8.cms.model.CMSConcurrentSweep;
import io.github.pingao777.jdk8.cms.model.CMSFinalRemark;
import io.github.pingao777.jdk8.cms.model.CMSInitialMark;
import io.github.pingao777.jdk8.cms.model.ConcurrentModeFailure;
import io.github.pingao777.jdk8.cms.model.FullGC;
import io.github.pingao777.jdk8.cms.model.GCLockerInitiatedGC;
import net.sf.dynamicreports.jasper.constant.ImageType;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.chart.XyLineChartBuilder;
import net.sf.dynamicreports.report.builder.component.Components;
import net.sf.dynamicreports.report.builder.component.TextFieldBuilder;
import net.sf.dynamicreports.report.constant.AxisPosition;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;

import static io.github.pingao777.common.util.ReportUtils.avg;
import static io.github.pingao777.common.util.ReportUtils.buildXyLineChart;
import static io.github.pingao777.common.util.ReportUtils.count;
import static io.github.pingao777.common.util.ReportUtils.duration;
import static io.github.pingao777.common.util.ReportUtils.durationScaled;
import static io.github.pingao777.common.util.ReportUtils.heap;
import static io.github.pingao777.common.util.ReportUtils.max;
import static io.github.pingao777.common.util.ReportUtils.min;
import static io.github.pingao777.common.util.ReportUtils.name;
import static io.github.pingao777.common.util.ReportUtils.old;
import static io.github.pingao777.common.util.ReportUtils.scale;
import static io.github.pingao777.common.util.ReportUtils.sd;
import static io.github.pingao777.common.util.ReportUtils.startup;
import static io.github.pingao777.common.util.ReportUtils.young;
import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;


/**
 * Created by pingao on 2020/1/14.
 */
public class Jdk8CMSReporter implements Report {
    private List<Double> durationHist = new ArrayList<>();
    private StringBuilder jvmStr = new StringBuilder();
    private StringBuilder memStr = new StringBuilder();
    private StringBuilder flagStr = new StringBuilder();

    @Override
    public void report(List<? extends ResultData> result) {
        Map<String, JRDataSource> datasource = buildDataSources(result);

        XyLineChartBuilder memoryChart = buildXyLineChart(
            "memory",
            "startup(sec)",
            "memory",
            datasource.get("memory"),
            Templates.getByteChartCustomizer(),
            startup,
            cht.xySerie(young),
            cht.xySerie(old),
            cht.xySerie(heap));

        XyLineChartBuilder durationChart = buildXyLineChart(
            "duration",
            "startup(sec)",
            "duration(sec)",
            datasource.get("duration"),
            null,
            startup,
            cht.xySerie(duration));

        XyLineChartBuilder durationScaledChart = buildXyLineChart(
            "scaled duration",
            "startup(sec)",
            "scaled duration(sec)",
            datasource.get("duration_scaled"),
            null,
            startup,
            cht.xySerie(durationScaled));

        TextFieldBuilder<String> jvmText = cmp.text(jvmStr.toString())
            .setStyle(Templates.textStyle);

        TextFieldBuilder<String> memText = cmp.text(memStr.toString())
            .setStyle(Templates.textStyle);

        TextFieldBuilder<String> flagText = cmp.text(flagStr.toString())
            .setStyle(Templates.textStyle);

        try {
            DynamicReports.report()
                .setTemplate(Templates.reportTemplate)
                .title(
                    Templates.createTitleComponent("CT scan for garbage collector"),
                    memoryChart,
                    cmp.verticalGap(10),
                    cht.multiAxisChart()
                        .setTitle("duration")
                        .setTitleFont(Templates.boldFont)
                        .addChart(durationChart, AxisPosition.LEFT_OR_TOP)
                        .addChart(durationScaledChart, AxisPosition.RIGHT_OR_BOTTOM),
                    cmp.verticalGap(10),
                    cmp.image(Templates.createHistChartExpression(durationHist, "scaled duration distribution", "duration(sec)", "count")).setFixedHeight(150),
                    cmp.verticalGap(20),
                    jvmText,
                    memText,
                    flagText,
                    Components.pageBreak()
                )
                .columns(name, count, max, min, avg, sd)
                .pageFooter(Templates.footerComponent)
                .setDataSource(datasource.get("summary"))
                .show();
        } catch (DRException e) {
            e.printStackTrace();
        }
    }

    private Map<String, JRDataSource> buildDataSources(List<? extends ResultData> result) {
        Map<String, JRDataSource> datasource = new HashMap<>();
        DRDataSource summaryData = new DRDataSource("name", "count", "max", "min", "avg", "sd");
        DRDataSource memoryData = new DRDataSource("startup", "young", "old", "heap");
        DRDataSource durationData = new DRDataSource("startup", "duration");
        DRDataSource durationScaledData = new DRDataSource("startup", "duration");

        result.stream()
            .collect(Collectors.groupingBy(ResultData::getPhrase))
            .entrySet()
            .stream()
            .sorted(Comparator.comparing(Map.Entry::getKey))
            .forEach(entry -> {
                Phrase phrase = entry.getKey();
                List<? extends ResultData> list = entry.getValue();
                switch (phrase) {
                    case JDK_INFO:
                        JdkInfo jdkInfo = (JdkInfo) list.get(0);
                        jvmStr.append(jdkInfo.getJvm());
                        jvmStr.append(' ');
                        jvmStr.append(jdkInfo.getJvmVersion());
                        jvmStr.append(", Jdk ");
                        jvmStr.append(jdkInfo.getJdkVersion());
                        break;
                    case MEMORY_INFO:
                        MemoryInfo memoryInfo = (MemoryInfo) list.get(0);
                        memStr.append("page ").append(ParseUtils.simple(memoryInfo.getPage())).append(", ")
                            .append("physical ").append(ParseUtils.simple(memoryInfo.getPhysicalFree())).append('/')
                            .append(ParseUtils.simple(memoryInfo.getPhysicalTotal())).append(", ")
                            .append("swap ").append(ParseUtils.simple(memoryInfo.getSwapFree())).append('/')
                            .append(ParseUtils.simple(memoryInfo.getSwapTotal()));
                        break;
                    case COMMAND_LINE_FLAGS:
                        CommandLineFlags flags = (CommandLineFlags) list.get(0);
                        flagStr.append(flags.getFlags());
                        break;
                    case CMS_ALLOCATION_FAILURE: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (AllocationFailure) data)
                            .map(AllocationFailure::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (AllocationFailure) data)
                            .forEach(f -> {
                                memoryData.add(f.getStartup(), f.getYoungBefore(), f.getHeapBefore() - f.getYoungBefore(), f.getHeapBefore());
                                memoryData.add(f.getStartup() + f.getDuration(), f.getYoungAfter(), f.getHeapAfter() - f.getYoungAfter(), f.getHeapAfter());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case CMS_CONCURRENT_MODE_FAILURE: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (ConcurrentModeFailure) data)
                            .map(ConcurrentModeFailure::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (ConcurrentModeFailure) data)
                            .forEach(f -> {
                                memoryData.add(f.getStartup(), f.getHeapBefore() - f.getOldBefore(), f.getOldBefore(), f.getHeapBefore());
                                memoryData.add(f.getStartup() + f.getDuration(), f.getHeapAfter() - f.getOldAfter(), f.getOldAfter(), f.getHeapAfter());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case CMS_INITIAL_MARK: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (CMSInitialMark) data)
                            .map(CMSInitialMark::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (CMSInitialMark) data)
                            .forEach(f -> {
                                memoryData.add(f.getStartup(), f.getHeapOccupy() - f.getOldOccupy(), f.getOldOccupy(), f.getHeapOccupy());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }

                    break;
                    case CMS_CONCURRENT_MARK: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (CMSConcurrentMark) data)
                            .map(CMSConcurrentMark::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (CMSConcurrentMark) data)
                            .forEach(f -> {
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case CMS_CONCURRENT_PRECLEAN: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (CMSConcurrentPreclean) data)
                            .map(CMSConcurrentPreclean::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (CMSConcurrentPreclean) data)
                            .forEach(f -> {
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case CMS_CONCURRENT_ABORTABLE_PRECLEAN: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (CMSConcurrentAbortable) data)
                            .map(CMSConcurrentAbortable::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (CMSConcurrentAbortable) data)
                            .forEach(f -> {
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }

                    break;
                    case CMS_FINAL_REMARK: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (CMSFinalRemark) data)
                            .map(CMSFinalRemark::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (CMSFinalRemark) data)
                            .forEach(f -> {
                                memoryData.add(f.getStartup(), f.getYoungOccupy(),
                                    f.getOldOccupy(), f.getHeapOccupy());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case CMS_CONCURRENT_SWEEP: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (CMSConcurrentSweep) data)
                            .map(CMSConcurrentSweep::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (CMSConcurrentSweep) data)
                            .forEach(f -> {
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }

                    break;
                    case CMS_CONCURRENT_RESET: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (CMSConcurrentReset) data)
                            .map(CMSConcurrentReset::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (CMSConcurrentReset) data)
                            .forEach(f -> {
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case CMS_FULL_GC: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (FullGC) data)
                            .map(FullGC::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream().map(data -> (FullGC) data).forEach(f -> {
                            memoryData.add(f.getStartup(), f.getHeapBefore() - f.getOldBefore(), f.getOldBefore(), f.getHeapBefore());
                            memoryData.add(f.getStartup() + f.getDuration(), f.getHeapAfter() - f.getOldAfter(), f.getOldAfter(), f.getHeapAfter());
                            durationData.add(f.getStartup(), f.getDuration());
                            double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                            durationScaledData.add(f.getStartup(), scaled);
                            durationHist.add(scaled);
                        });
                    }
                    break;
                    case CMS_GCLOCKER_INITIATED_GC: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (GCLockerInitiatedGC) data)
                            .map(GCLockerInitiatedGC::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (GCLockerInitiatedGC) data)
                            .forEach(f -> {
                                memoryData.add(f.getStartup(), f.getYoungBefore(), f.getHeapBefore() - f.getYoungBefore(), f.getHeapBefore());
                                memoryData.add(f.getStartup() + f.getDuration(), f.getYoungAfter(), f.getHeapAfter() - f.getYoungAfter(), f.getHeapAfter());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                }
            });

        datasource.put("summary", summaryData);
        datasource.put("memory", memoryData);
        datasource.put("duration", durationData);
        datasource.put("duration_scaled", durationScaledData);
        return datasource;
    }
}
