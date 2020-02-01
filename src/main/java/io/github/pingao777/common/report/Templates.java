package io.github.pingao777.common.report;

/*
 * DynamicReports - Free Java reporting library for creating reports dynamically
 *
 * Copyright (C) 2010 - 2018 Ricardo Mariaca and the Dynamic Reports Contributors
 * http://www.dynamicreports.org
 *
 * This file is part of DynamicReports.
 *
 * DynamicReports is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DynamicReports is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with DynamicReports. If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.*;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.List;
import java.util.Locale;
import io.github.pingao777.common.util.ParseUtils;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.base.expression.AbstractValueFormatter;
import net.sf.dynamicreports.report.builder.HyperLinkBuilder;
import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.component.ComponentBuilder;
import net.sf.dynamicreports.report.builder.datatype.BigDecimalType;
import net.sf.dynamicreports.report.builder.style.FontBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.tableofcontents.TableOfContentsCustomizerBuilder;
import net.sf.dynamicreports.report.constant.HorizontalTextAlignment;
import net.sf.dynamicreports.report.constant.LineStyle;
import net.sf.dynamicreports.report.constant.VerticalTextAlignment;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.definition.chart.DRIChartCustomizer;
import net.sf.dynamicreports.report.definition.expression.DRIValueFormatter;
import net.sf.jasperreports.charts.util.DrawChartRendererImpl;
import net.sf.jasperreports.renderers.Renderable;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.statistics.HistogramType;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.hyperLink;
import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.tableOfContentsCustomizer;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;


/**
 * <p>Templates class.</p>
 *
 * @author Ricardo Mariaca (r.mariaca@dynamicreports.org)
 * @version $Id: $Id
 */
public class Templates {
    public static final FontBuilder boldFont = stl.fontArialBold();

    public static final StyleBuilder textStyle = stl.style().italic()
        .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
        .setVerticalTextAlignment(VerticalTextAlignment.BOTTOM)
        .setRightPadding(10)
        .setTopPadding(10);
    ;
    /**
     * Constant <code>rootStyle</code>
     */
    public static final StyleBuilder rootStyle;
    /**
     * Constant <code>boldStyle</code>
     */
    public static final StyleBuilder boldStyle;
    /**
     * Constant <code>italicStyle</code>
     */
    public static final StyleBuilder italicStyle;
    /**
     * Constant <code>boldCenteredStyle</code>
     */
    public static final StyleBuilder boldCenteredStyle;
    /**
     * Constant <code>bold12CenteredStyle</code>
     */
    public static final StyleBuilder bold12CenteredStyle;
    /**
     * Constant <code>bold18CenteredStyle</code>
     */
    public static final StyleBuilder bold18CenteredStyle;
    /**
     * Constant <code>bold22CenteredStyle</code>
     */
    public static final StyleBuilder bold22CenteredStyle;
    /**
     * Constant <code>columnStyle</code>
     */
    public static final StyleBuilder columnStyle;
    /**
     * Constant <code>columnTitleStyle</code>
     */
    public static final StyleBuilder columnTitleStyle;
    /**
     * Constant <code>groupStyle</code>
     */
    public static final StyleBuilder groupStyle;
    /**
     * Constant <code>subtotalStyle</code>
     */
    public static final StyleBuilder subtotalStyle;

    /**
     * Constant <code>reportTemplate</code>
     */
    public static final ReportTemplateBuilder reportTemplate;
    /**
     * Constant <code>currencyType</code>
     */
    public static final CurrencyType currencyType;
    /**
     * Constant <code>dynamicReportsComponent</code>
     */
    public static final ComponentBuilder<?, ?> dynamicReportsComponent;
    /**
     * Constant <code>footerComponent</code>
     */
    public static final ComponentBuilder<?, ?> footerComponent;

    static {
        rootStyle = stl.style().setPadding(2);
        boldStyle = stl.style(rootStyle).bold();
        italicStyle = stl.style(rootStyle).italic();
        boldCenteredStyle = stl.style(boldStyle).setTextAlignment(HorizontalTextAlignment.CENTER, VerticalTextAlignment.MIDDLE);
        bold12CenteredStyle = stl.style(boldCenteredStyle).setFontSize(12);
        bold18CenteredStyle = stl.style(boldCenteredStyle).setFontSize(18);
        bold22CenteredStyle = stl.style(boldCenteredStyle).setFontSize(22);
        columnStyle = stl.style(rootStyle).setVerticalTextAlignment(VerticalTextAlignment.MIDDLE).setBorder(stl.pen(0.5f, LineStyle.SOLID));
        columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point()).setHorizontalTextAlignment(HorizontalTextAlignment.CENTER)
            .setBackgroundColor(Color.LIGHT_GRAY).bold();
        groupStyle = stl.style(boldStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT);
        subtotalStyle = stl.style(boldStyle).setTopBorder(stl.pen1Point());

        StyleBuilder crosstabGroupStyle = stl.style(columnTitleStyle);
        StyleBuilder crosstabGroupTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(170, 170, 170));
        StyleBuilder crosstabGrandTotalStyle = stl.style(columnTitleStyle).setBackgroundColor(new Color(140, 140, 140));
        StyleBuilder crosstabCellStyle = stl.style(columnStyle).setBorder(stl.pen1Point());

        TableOfContentsCustomizerBuilder tableOfContentsCustomizer = tableOfContentsCustomizer().setHeadingStyle(0, stl.style(rootStyle).bold());

        reportTemplate = template()
            .setLocale(Locale.ENGLISH)
            .setColumnStyle(columnStyle)
            .setColumnTitleStyle(columnTitleStyle)
            .setGroupStyle(groupStyle)
            .setGroupTitleStyle(groupStyle)
            .setSubtotalStyle(subtotalStyle)
            .highlightDetailEvenRows()
            .crosstabHighlightEvenRows()
            .setCrosstabGroupStyle(crosstabGroupStyle)
            .setCrosstabGroupTotalStyle(crosstabGroupTotalStyle)
            .setCrosstabGrandTotalStyle(crosstabGrandTotalStyle)
            .setCrosstabCellStyle(crosstabCellStyle)
            .setTableOfContentsCustomizer(tableOfContentsCustomizer);

        currencyType = new CurrencyType();

        HyperLinkBuilder link = hyperLink("https://github.pingao777/gcct");
        dynamicReportsComponent = cmp.horizontalList(
            cmp.image(Templates.class.getResource("images/dynamicreports.png")).setFixedDimension(60, 60),
            cmp.verticalList(
                cmp.text("GCCT").setStyle(bold22CenteredStyle).setHorizontalTextAlignment(HorizontalTextAlignment.LEFT),
                cmp.text("https://github.pingao777/gcct").setStyle(italicStyle).setHyperLink(link))).setFixedWidth(300);

        footerComponent = cmp.pageXofY().setStyle(stl.style(boldCenteredStyle).setTopBorder(stl.pen1Point()));
    }

    /**
     * Creates custom component which is possible to add to any report band component
     *
     * @param label a {@link String} object.
     * @return a {@link net.sf.dynamicreports.report.builder.component.ComponentBuilder} object.
     */
    public static ComponentBuilder<?, ?> createTitleComponent(String label) {
        return cmp.horizontalList()
            .add(
                dynamicReportsComponent,
                cmp.text(label).setStyle(stl.style().boldItalic()
                    .setHorizontalTextAlignment(HorizontalTextAlignment.RIGHT)
                    .setVerticalTextAlignment(VerticalTextAlignment.BOTTOM)
                    .setPadding(7))
            )
            .newRow()
            .add(cmp.line())
            .newRow()
            .add(cmp.verticalGap(10));
    }

    /**
     * <p>createCurrencyValueFormatter.</p>
     *
     * @param label a {@link String} object.
     */
    public static CurrencyValueFormatter createCurrencyValueFormatter(String label) {
        return new CurrencyValueFormatter(label);
    }

    public static class CurrencyType extends BigDecimalType {
        private static final long serialVersionUID = 1L;

        @Override
        public String getPattern() {
            return "$ #,###.00";
        }
    }


    private static class CurrencyValueFormatter extends AbstractValueFormatter<String, Number> {
        private static final long serialVersionUID = 1L;

        private String label;

        public CurrencyValueFormatter(String label) {
            this.label = label;
        }

        @Override
        public String format(Number value, ReportParameters reportParameters) {
            return label + currencyType.valueToString(value, reportParameters.getLocale());
        }
    }


    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static DoubleValueFormatter getDoubleValueFormatter() {
        return new DoubleValueFormatter();
    }

    public static LongValueFormatter getLongValueFormatter() {
        return new LongValueFormatter();
    }

    public static DRIChartCustomizer getByteChartCustomizer() {
        return new ByteChartCustomizer();
    }

    public static HistChartExpression createHistChartExpression(List<Double> doubles, String title, String x, String y) {
        return new HistChartExpression(doubles, title, x, y);
    }

    private static class DoubleValueFormatter implements DRIValueFormatter<String, Double> {

        private static final long serialVersionUID = 7075707547791671894L;

        @Override
        public String format(Double value, ReportParameters reportParameters) {
            return DF.format(value);
        }

        @Override
        public Class<String> getValueClass() {
            return String.class;
        }
    }


    private static class LongValueFormatter implements DRIValueFormatter<String, Long> {

        private static final long serialVersionUID = 3048599452747094930L;

        @Override
        public String format(Long value, ReportParameters reportParameters) {
            if (value > 1000) {
                return DF.format(value / 1000.0);
            }

            if (value > 1000000) {
                return DF.format(value / 1000000.0);
            }

            return String.valueOf(value);
        }

        @Override
        public Class<String> getValueClass() {
            return String.class;
        }
    }


    private static class ByteChartCustomizer implements DRIChartCustomizer {

        private static final long serialVersionUID = -1952062939716538200L;

        @Override
        public void customize(JFreeChart chart, ReportParameters reportParameters) {
            XYPlot xyplot = (XYPlot) chart.getPlot();
            NumberAxis rangeAxis = (NumberAxis) xyplot.getRangeAxis();
            rangeAxis.setNumberFormatOverride(new ByteFormat());
        }
    }


    private static class ByteFormat extends NumberFormat {
        private static final long serialVersionUID = -6622117046039360172L;

        @Override
        public Number parse(String source, ParsePosition parsePosition) {
            return null;
        }

        @Override
        public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
            return new StringBuffer(ParseUtils.simple(number));
        }

        @Override
        public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
            return new StringBuffer(ParseUtils.simple(number));
        }
    }


    private static class HistChartExpression extends AbstractSimpleExpression<Renderable> {
        private static final long serialVersionUID = -4863375798091751144L;
        private String title;
        private double[] values;
        private String x;
        private String y;

        public HistChartExpression(List<Double> doubles, String title, String x, String y) {
            this.values = new double[doubles.size()];
            for (int i = 0; i < doubles.size(); i++) {
                values[i] = doubles.get(i);
            }
            this.title = title;
            this.x = x;
            this.y = y;
        }

        @Override
        public Renderable evaluate(ReportParameters reportParameters) {
            HistogramDataset hist = new HistogramDataset();
            hist.setType(HistogramType.FREQUENCY);
            hist.addSeries("phrase.getName()", values, 100);
            JFreeChart chart = ChartFactory.createHistogram(
                title, x, y,
                hist,
                PlotOrientation.VERTICAL, false, false, false);
            Font boldFont = new Font(Templates.boldFont.getFont().getFontName(), Font.BOLD, Templates.boldFont.getFont().getFontSize());
            Font regularFont = new Font(Templates.boldFont.getFont().getFontName(), Font.PLAIN, Templates.boldFont.getFont().getFontSize());
            chart.getTitle().setFont(boldFont);
            chart.setBackgroundPaint(Color.WHITE);
            XYPlot xyplot = (XYPlot) chart.getPlot();
            xyplot.getDomainAxis().setLabelFont(regularFont);
            xyplot.getDomainAxis().setTickLabelFont(regularFont);
            xyplot.getRangeAxis().setLabelFont(regularFont);
            xyplot.getRangeAxis().setTickLabelFont(regularFont);
            xyplot.setBackgroundPaint(Color.WHITE);
            final int gridIn = 200;
            xyplot.setDomainGridlinePaint(new Color(gridIn, gridIn, gridIn));
            xyplot.setRangeGridlinePaint(new Color(gridIn, gridIn, gridIn));
            XYBarRenderer renderer = (XYBarRenderer) xyplot.getRenderer();
            renderer.setDrawBarOutline(false);
            renderer.setShadowVisible(false);
            renderer.setBarPainter(new StandardXYBarPainter());
            return new DrawChartRendererImpl(chart, null);
        }
    }
}