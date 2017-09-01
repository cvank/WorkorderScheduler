package com.workorder.scheduler.ui.chart;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.PublicCloneable;
import org.springframework.stereotype.Component;

import com.workorder.scheduler.domain.LocationWorkOrderMap;
import com.workorder.scheduler.domain.WorkOrderStatus;

@Component
public class TechnicianWorkOrderRouteMap {

	public String plotChart(List<LocationWorkOrderMap> locationWorkOrderMaps) {
		XYDataset dataset = createDataset(locationWorkOrderMaps);
		JFreeChart chart = createChart(dataset, locationWorkOrderMaps.get(0).getTechnician().getName());
		try {
			String str = locationWorkOrderMaps.get(0).getTechnician().getName() + "_" + System.currentTimeMillis()
					+ ".png";
			FileOutputStream fileOutputStream = new FileOutputStream(new File(str));
			ChartUtilities.writeChartAsPNG(fileOutputStream, chart, 500, 270);
			return str;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private XYDataset createDataset(List<LocationWorkOrderMap> locationWorkOrderMaps) {
		XYSeries series = new XYSeries(locationWorkOrderMaps.get(0).getTechnician().getName(), false, true);

		locationWorkOrderMaps.stream()
				.filter(item -> item.getWorkOrder().getCurrentStatus() != WorkOrderStatus.COMPLETED
						&& !item.getWorkOrder().getScheduledDate().equals(new Date()))
				.forEach(item -> series.add(item.getLocation().getCoordinates().getLat(),
						item.getLocation().getCoordinates().getLang()));

		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;

	}

	/**
	 * Creates a chart.
	 *
	 * @param dataset
	 *            the data for the chart.
	 *
	 * @return a chart.
	 */
	private JFreeChart createChart(XYDataset dataset, final String name) {
		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(name + "'s Route Map",

				"X", // x axis label
				"Y", // y axis label
				dataset, // data
				PlotOrientation.VERTICAL, true, // include legend
				true, // tooltips
				false // urls
		);

		chart.setBackgroundPaint(Color.CYAN);

		XYPlot plot = (XYPlot) chart.getPlot();
		NumberAxis xAxis = new NumberAxis();
		xAxis.setTickUnit(new NumberTickUnit(5));

		// Assign it to the chart
		plot.setDomainAxis(xAxis);
		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();

		renderer.setShapesVisible(true);
		renderer.setShapesFilled(true);
		renderer.setBaseItemLabelGenerator(new LegendXYItemLabelGenerator(plot.getLegendItems()));
		renderer.setBaseItemLabelsVisible(true);
		renderer.setItemLabelAnchorOffset(-8.0);

		return chart;
	}

	public class LegendXYItemLabelGenerator extends StandardXYItemLabelGenerator
			implements XYItemLabelGenerator, Cloneable, PublicCloneable, Serializable {
		private static final long serialVersionUID = 3909600482102881635L;
		private LegendItemCollection legendItems;

		public LegendXYItemLabelGenerator(LegendItemCollection legendItems) {
			super();
			this.legendItems = legendItems;
		}

		@Override
		public String generateLabel(XYDataset dataset, int series, int item) {
			LegendItem legendItem = legendItems.get(series);
			return String.valueOf(item + 1);
		}
	}

}