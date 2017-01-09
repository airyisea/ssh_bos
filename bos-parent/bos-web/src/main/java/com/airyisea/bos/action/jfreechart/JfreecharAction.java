package com.airyisea.bos.action.jfreechart;

import java.awt.Font;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Map;












import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.domain.basic.DecidedZone;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
@SuppressWarnings("serial")
@Controller
@Scope("prototype")
@Namespace("/demo")
@ParentPackage("bos")
public class JfreecharAction extends ActionSupport{
	
	private List<String> hobby;
	public List<String> getHobby() {
		return hobby;
	}
	public void setHobby(List<String> hobby) {
		this.hobby = hobby;
	}

	private JFreeChart chart;
	
	//chart结果集会默认去值栈寻找名为chart的对象，本Action已经在值栈中了，所以会来调用该getter方法。
	public JFreeChart getChart() {
		//设置标题，横坐标，纵坐标，数据，排列方向（水平/垂直）
		chart = ChartFactory.createBarChart("兴趣统计结果", "项目", "结果", this.getDataset(), PlotOrientation.VERTICAL, false, false, false);
		chart.setTitle(new TextTitle("兴趣统计结果", new Font("黑体", Font.BOLD, 22)));
		//设置字体样式
		CategoryPlot plot = (CategoryPlot) chart.getPlot();
		CategoryAxis categoryAxis = plot.getDomainAxis();
		categoryAxis.setLabelFont(new Font("宋体", Font.BOLD, 22));
		categoryAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);// 设置角度
		NumberAxis numberAxis = (NumberAxis) plot.getRangeAxis();
		numberAxis.setLabelFont(new Font("宋体", Font.BOLD, 22));
		return chart;

	}
	public JFreeChart getPieChart() {
		chart = ChartFactory.createPieChart3D("兴趣统计结果", this.getPieDataset(), true, false, false);
		chart.setTitle(new TextTitle("兴趣统计结果", new Font("黑体", Font.BOLD, 22)));
		PiePlot plot = (PiePlot) chart.getPlot();
		//设置显示数据
		plot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0}={1}({2})", NumberFormat.getNumberInstance(),new DecimalFormat("0.00%")));
		return chart;
		
	}
	
	private PieDataset getPieDataset() {
		DefaultPieDataset dataset = new DefaultPieDataset();
		ActionContext context = ActionContext.getContext();
		Map map = context.getApplication();
		dataset.setValue("足球",(Integer) map.get("football"));
		dataset.setValue("篮球",(Integer) map.get("basketball"));
		dataset.setValue("排球",(Integer) map.get("volleyball"));
		dataset.setValue("网球",(Integer) map.get("tennis"));
		return dataset;
	}
	private CategoryDataset getDataset() {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		ActionContext context = ActionContext.getContext();
		Map map = context.getApplication();
		dataset.setValue((Integer) map.get("football"), "", "足球");// 更新成最新值
		dataset.setValue((Integer) map.get("basketball"), "", "篮球");
		dataset.setValue((Integer) map.get("volleyball"), "", "排球");
		dataset.setValue((Integer) map.get("tennis"), "", "网球");
		return dataset;
	}
	@Override
	@Action(value="jfreechart_hobby",results={@Result(name="success",location="/demo/jfreechart_demo.jsp")})
	public String execute() throws Exception {
		//使用servlet域模拟数据库
		ActionContext context = ActionContext.getContext();
		Map map = context.getApplication();
		for (String str : hobby) {
			// 表示用户第一次投票
			if (null == map.get(str))
			{
				map.put(str, 1);
			} else {
				map.put(str, (Integer) map.get(str) + 1);
			}
		}
		return SUCCESS;
	}
	
	@Action(value="jfreechart_getBar",results={@Result(name="getBar",type="chart",params={"height","600","width","400"})})
	public String getBar() {
		return "getBar";
	}
	@Action(value="jfreechart_getPie",results={@Result(name="getPie",type="chart",params={"height","600","width","600","value","pieChart"})})
	public String getPie() {
		return "getPie";
	}

}
