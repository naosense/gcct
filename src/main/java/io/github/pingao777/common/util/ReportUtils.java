package io.github.pingao777.common.util;

import io.github.pingao777.common.report.Templates;
import net.sf.dynamicreports.report.builder.chart.XyChartSerieBuilder;
import net.sf.dynamicreports.report.builder.chart.XyLineChartBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.column.ValueColumnBuilder;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.jasperreports.engine.JRDataSource;

import static net.sf.dynamicreports.report.builder.DynamicReports.cht;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;


/**
 * Created by pingao on 2020/1/31.
 */
public class ReportUtils {
    public static final TextColumnBuilder<String> name = col.column("name", "name", type.stringType()).setFixedWidth(200);
    public static final TextColumnBuilder<Long> count = col.column("count", "count", type.longType());
    public static final TextColumnBuilder<Double> max = col.column("max(sec)", "max", type.doubleType()).setValueFormatter(Templates.getDoubleValueFormatter());
    public static final TextColumnBuilder<Double> min = col.column("min(sec)", "min", type.doubleType()).setValueFormatter(Templates.getDoubleValueFormatter());
    public static final TextColumnBuilder<Double> avg = col.column("avg(sec)", "avg", type.doubleType()).setValueFormatter(Templates.getDoubleValueFormatter());
    public static final TextColumnBuilder<Double> sd = col.column("sd(sec)", "sd", type.doubleType()).setValueFormatter(Templates.getDoubleValueFormatter());
    public static final TextColumnBuilder<Double> startup = col.column("startup", "startup", type.doubleType());
    public static final TextColumnBuilder<Double> duration = col.column("duration", "duration", type.doubleType());
    public static final TextColumnBuilder<Double> durationScaled = col.column("duration scaled", "duration", type.doubleType());
    public static final TextColumnBuilder<Double> eden = col.column("eden", "eden", type.doubleType());
    public static final TextColumnBuilder<Double> survivors = col.column("survivors", "survivors", type.doubleType());
    public static final TextColumnBuilder<Double> young = col.column("young", "young", type.doubleType());

    public static final TextColumnBuilder<Double> old = col.column("old", "old", type.doubleType());
    public static final TextColumnBuilder<Double> heap = col.column("heap", "heap", type.doubleType());

    public static XyLineChartBuilder buildXyLineChart(String title, String xlabel, String ylabel,
        JRDataSource dataSource,
        DRIChartCustomizer customizers,
        ValueColumnBuilder<?, ? extends Number> x,
        XyChartSerieBuilder... yseries) {
        XyLineChartBuilder chart = cht.xyLineChart()
            .setTitle(title)
            .setTitleFont(Templates.boldFont)
            .setXValue(x)
            .setShowShapes(false)
            .series(
                yseries
            )
            .setXAxisFormat(
                cht.axisFormat().setLabel(xlabel))
            .setYAxisFormat(
                cht.axisFormat().setLabel(ylabel))
            .setDataSource(dataSource);
        if (customizers != null) {
            chart.customizers(customizers);
        }
        return chart;
    }

    public static double scale(double x, double mean, double sd) {
        return sd == 0 ? mean : (x - mean) / sd;
        // return (x - mean) / sd;
    }
}
