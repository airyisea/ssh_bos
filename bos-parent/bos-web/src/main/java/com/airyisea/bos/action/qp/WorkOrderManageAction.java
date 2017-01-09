package com.airyisea.bos.action.qp;

import java.awt.Color;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.qp.WorkOrderManage;
import com.airyisea.bos.utils.FileEncodeUtils;
import com.lowagie.text.BadElementException;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

@SuppressWarnings("serial")
@Controller("workOrderManageAction")
@Scope("prototype")
@Namespace("/qp")
@ParentPackage("bos")
public class WorkOrderManageAction extends BaseAction<WorkOrderManage> {
	
	
	@Action(value="workordermanage_add",results={@Result(name="add",type="json")})
	public String add() {
		try {
			facadeService.getWorkOrderManageService().add(model);
			push(true);
		} catch (Exception e) {
			push(false);
			e.printStackTrace();
		}
		return "add";
	}
	@Action(value="workordermanage_listNoStart",results={@Result(name="listNoStart",location="/WEB-INF/pages/workflow/startransfer.jsp")})
	public String listNoStart() {
		List<WorkOrderManage> list = facadeService.getWorkOrderManageService().findNoStart();
		set("list", list);
		return "listNoStart";
	}
	@Action(value="workordermanage_start",results={@Result(name="start",type="redirectAction",location="/workordermanage_listNoStart")})
	public String start() {
		facadeService.getWorkOrderManageService().start(model.getId());
		return "start";
	}
	
	@Action(value="workordermanage_queryPage")
	public String queryPage() {
		String conditionName = getParameter("conditionName");
		String conditionValue = getParameter("conditionValue");
		Page<WorkOrderManage> pageResponse = null;
		if(StringUtils.isNotBlank(conditionValue) && StringUtils.isNotBlank(conditionName)) {
			pageResponse = facadeService.getWorkOrderManageService().queryPage(getPageRequest(), conditionName, conditionValue);
		}else {
			pageResponse = facadeService.getWorkOrderManageService().queryPage(getPageRequest());
			
		}
		setPageResponse(pageResponse);
		return "queryPage";
	}
	@Action(value="workordermanage_exportPDF")
	public String exportPDF() {
		Document document = new Document();
		try {
			List<WorkOrderManage> list = facadeService.getWorkOrderManageService().findAll();
			HttpServletResponse response = ServletActionContext.getResponse();
			PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
			//设置密码
			writer.setEncryption("user".getBytes(), "airyisea".getBytes(), PdfWriter.ALLOW_SCREENREADERS, PdfWriter.STANDARD_ENCRYPTION_128);
			String filename = new Date(System.currentTimeMillis()) + "_工作单报表.pdf";
			//两头
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			response.setHeader("Content-Disposition", "attachment;filename=" + FileEncodeUtils.encodeDownloadFilename(filename, ServletActionContext.getRequest().getHeader("user-agent")));
			//打开文档
			document.open();
			Table table = new Table(5,list.size() + 1);
			table.setBorderWidth(1f);
			// 其中1为居中对齐，2为右对齐，3为左对齐
			table.setAlignment(1); 
			// 边框
			table.setBorder(1); 
			// 水平对齐方式
			table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER); 
			 // 垂直对齐方式
			table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
			// 设置表格字体
			BaseFont cn = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", false);
			Font font = new Font(cn, 10, Font.NORMAL, Color.BLUE);
			//设置表头
			table.addCell(buildCell("到达地", font));
			table.addCell(buildCell("货物", font));
			table.addCell(buildCell("数量", font));
			table.addCell(buildCell("重量", font));
			table.addCell(buildCell("配载要求", font));
			
			for (WorkOrderManage workOrderManage : list) {
				table.addCell(buildCell(workOrderManage.getArrivecity(), font));
				table.addCell(buildCell(workOrderManage.getProduct(), font));
				table.addCell(buildCell(workOrderManage.getNum().toString(), font));
				table.addCell(buildCell(workOrderManage.getWeight().toString(), font));
				table.addCell(buildCell(workOrderManage.getFloadreqr(), font));
			}
			document.add(table);
			

		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			document.close();
		}
		return NONE;
	}
	@Autowired
	private DataSource dataSource;
	
	@Action(value="workordermanage_exportPDF2")
	public String exportPDF2() {
		try {
			// 1: 加载设计文件mavenbos.jrxml
			String path = ServletActionContext.getServletContext().getRealPath("/jr/mavenbos.jrxml");
			// 2: 报表 parameter 赋值 需要Map 集合
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("company", "上海XXX");
			// 3: 编译该文件 JasperCompilerManager
			JasperReport report = JasperCompileManager.compileReport(path);
			// 4: 创建PDF绘制对象
			JasperPrint jasperPrint = JasperFillManager.fillReport(report, param, dataSource.getConnection());
			// 5: 下载 准备一个流 两个头
			HttpServletResponse response = ServletActionContext.getResponse();
			ServletOutputStream outputStream = response.getOutputStream();
			String filename = "工作单报表.pdf";
			response.setContentType(ServletActionContext.getServletContext().getMimeType(filename));
			response.setHeader("Content-Disposition", "attachment;filename=" + FileEncodeUtils.encodeDownloadFilename(filename, ServletActionContext.getRequest().getHeader("user-agent")));
			// 6: JapdfExport
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, outputStream);
			// 7: 导出
			exporter.exportReport();
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return NONE;
	}
	

	// 创建单元格
	private Cell buildCell(String content, Font font) throws BadElementException {
		Phrase phrase = new Phrase(content, font);
		Cell cell = new Cell(phrase);
		// 设置垂直居中
		cell.setVerticalAlignment(1);
		// 设置水平居中
		cell.setHorizontalAlignment(1);
		return cell;
	}
}
