package io.github.pingao777.jdk8.g1;

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
import io.github.pingao777.jdk8.g1.model.G1Cleanup;
import io.github.pingao777.jdk8.g1.model.G1Concurrent;
import io.github.pingao777.jdk8.g1.model.G1FullGC;
import io.github.pingao777.jdk8.g1.model.G1Pause;
import io.github.pingao777.jdk8.g1.model.G1Remark;
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
import static io.github.pingao777.common.util.ReportUtils.eden;
import static io.github.pingao777.common.util.ReportUtils.heap;
import static io.github.pingao777.common.util.ReportUtils.max;
import static io.github.pingao777.common.util.ReportUtils.min;
import static io.github.pingao777.common.util.ReportUtils.name;
import static io.github.pingao777.common.util.ReportUtils.old;
import static io.github.pingao777.common.util.ReportUtils.scale;
import static io.github.pingao777.common.util.ReportUtils.sd;
import static io.github.pingao777.common.util.ReportUtils.startup;
import static io.github.pingao777.common.util.ReportUtils.survivors;
import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;


/**
 * Created by pingao on 2020/1/14.
 */
public class Jdk8G1Reporter implements Report {
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
            cht.xySerie(eden),
            cht.xySerie(survivors),
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
                    Templates.createTitleComponent("CT scanner for jvm garbage collector"),
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
        DRDataSource memoryData = new DRDataSource("startup", "eden", "survivors", "old", "heap");
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
                    case G1_EVACUATION_PAUSE:
                    case G1_HUMONGOUS_PAUSE: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (G1Pause) data)
                            .map(G1Pause::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (G1Pause) data)
                            .forEach(f -> {
                                memoryData.add(f.getStartup(), f.getEdenBefore(), f.getSurvivorsBefore(), f.getHeapBefore() - f.getEdenBefore() - f.getSurvivorsBefore(), f.getHeapBefore());
                                memoryData.add(f.getStartup() + f.getDuration(), f.getEdenAfter(), f.getSurvivorsAfter(), f.getHeapAfter() - f.getEdenAfter() - f.getSurvivorsAfter(), f.getHeapAfter());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case G1_CLEANUP: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (G1Cleanup) data)
                            .map(G1Cleanup::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (G1Cleanup) data)
                            .forEach(f -> {
                                // memoryData.add(f.getStartup(), f.getHeapBefore() - f.getOldBefore(), f.getOldBefore(), f.getHeapBefore());
                                // memoryData.add(f.getStartup() + f.getDuration(), f.getHeapAfter() - f.getOldAfter(), f.getOldAfter(), f.getHeapAfter());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case G1_REMARK: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (G1Remark) data)
                            .map(G1Remark::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (G1Remark) data)
                            .forEach(f -> {
                                // memoryData.add(f.getStartup(), f.getHeapOccupy() - f.getOldOccupy(), f.getOldOccupy(), f.getHeapOccupy());
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }

                    break;
                    case G1_CONCURRENT_MARK:
                    case G1_CONCURRENT_CLEANUP:
                    case G1_CONCURRENT_ROOT_REGION_SCAN: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (G1Concurrent) data)
                            .map(G1Concurrent::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream()
                            .map(data -> (G1Concurrent) data)
                            .forEach(f -> {
                                durationData.add(f.getStartup(), f.getDuration());
                                double scaled = scale(f.getDuration(), summary.getAverage(), summary.getStd());
                                durationScaledData.add(f.getStartup(), scaled);
                                durationHist.add(scaled);
                            });
                    }
                    break;
                    case G1_FULL_GC: {
                        DoubleStatistics summary = list.stream()
                            .map(data -> (G1FullGC) data)
                            .map(G1FullGC::getDuration)
                            .collect(DoubleStatistics::new,
                                DoubleStatistics::accept,
                                DoubleStatistics::combine);

                        summaryData.add(phrase.getName(), summary.getCount(),
                            summary.getMax(), summary.getMin(),
                            summary.getAverage(), summary.getStd());

                        list.stream().map(data -> (G1FullGC) data)
                            .forEach(f -> {
                                memoryData.add(f.getStartup(), f.getEdenBefore(), f.getSurvivorsBefore(), f.getHeapBefore() - f.getEdenBefore() - f.getSurvivorsBefore(), f.getHeapBefore());
                                memoryData.add(f.getStartup() + f.getDuration(), f.getEdenAfter(), f.getSurvivorsAfter(), f.getHeapAfter() - f.getEdenAfter() - f.getSurvivorsAfter(), f.getHeapAfter());
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
