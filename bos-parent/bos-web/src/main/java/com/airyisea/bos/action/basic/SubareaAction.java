package com.airyisea.bos.action.basic;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

import com.airyisea.bos.action.base.BaseAction;
import com.airyisea.bos.domain.basic.Region;
import com.airyisea.bos.domain.basic.Subarea;
import com.airyisea.bos.utils.FileEncodeUtils;

@SuppressWarnings("serial")
@Controller("subareaAction")
@Scope("prototype")
@Namespace("/basic")
@ParentPackage("bos")
public class SubareaAction extends BaseAction<Subarea> {
	
	/**
	 * 校验id是否重复
	 * @return
	 * @throws Exception
	 */
	@Action(value="subarea_checkId",results={@Result(name="checkId",type="json")})
	public String checkId() {
		Subarea existSubarea = facadeService.getSubareaService().checkId(model.getId());
		if(existSubarea == null) {
			push(true);
		}else {
			push(false);
		}
		return "checkId";
	}
	
	/**
	 * 条件分页查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="subarea_queryPage")
	public String queryPage() {
		Page<Subarea> pageResponse = facadeService.getSubareaService().queryPage(getCondition(),getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
	}
	/**
	 * 查询未定区的分区
	 * @return
	 * @throws Exception
	 */
	@Action(value="subarea_findNoAssociationAjax",results={@Result(name="findNoAssociationAjax",type="json")})
	public String findNoAssociationAjax() {
		List<Subarea> list = facadeService.getSubareaService().findNoAssociation();
		push(list);
		return "findNoAssociationAjax";
	}
	/**
	 * 查询已定区的分区
	 * @return
	 * @throws Exception
	 */
	@Action(value="subaera_findByDecidedZoneAjax",results={@Result(name="findByDecidedZoneAjax",type="json")})
	public String findByDecidedZone() {
		List<Subarea> list = facadeService.getSubareaService().findByDecidedZone(model.getDecidedZone());
		push(list);
		return "findByDecidedZoneAjax";
	}
	
	/**
	 * 删除定区
	 * @return
	 * @throws Exception
	 */
	@Action(value="subarea_removeDecidedZone",results={@Result(name="removeDecidedZone",type="json")})
	public String removeDecidedZone() {
		String param = getParameter("ids");
		if(StringUtils.isNotBlank(param)) {
			String[] ids = param.split(",");
			facadeService.getSubareaService().removeDecidedZone(ids);
		}
		push(true);
		return "removeDecidedZone";
	}
	
	/**
	 * 批量导入分区数据
	 * @return
	 * @throws Exception
	 */
	@Action(value="subarea_importData",results={@Result(name="importData",type="json")})
	public String importData() {
		try {
			List<Subarea> list = new ArrayList<Subarea>();
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(upload));
			HSSFSheet sheet = wb.getSheetAt(0);
			for (Row row : sheet) {
				if(row.getRowNum() == 0)
					continue;
				Subarea subarea = new Subarea();
				subarea.setId(row.getCell(0).getStringCellValue());
				Region region = new Region();
				region.setId(row.getCell(1).getStringCellValue());
				subarea.setRegion(region);
				subarea.setAddresskey(row.getCell(2).getStringCellValue());
				subarea.setStartnum(row.getCell(3).getStringCellValue());
				subarea.setEndnum(row.getCell(4).getStringCellValue());
				subarea.setSingle(row.getCell(5).getStringCellValue().toCharArray()[0]);
				subarea.setPosition(row.getCell(6).getStringCellValue());
				list.add(subarea);
			}
			facadeService.getSubareaService().save(list);
			push(true);
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "importData";
	}
	
	//=======================================================================
	/**
	 * 添加分区
	 * @return
	 * @throws Exception
	 */
	@Action(value="subarea_add",results={@Result(name="add",location="/WEB-INF/pages/base/subarea.jsp")})
	public String add() {
		facadeService.getSubareaService().add(model);
		return "add";
	}
	/**
	 * 导出分区
	 * @return
	 * @throws Exception
	 */
	@Action(value="subarea_exportData")
	public String exportData() {
		try {
			List<Subarea> list = facadeService.getSubareaService().queryList(getCondition());
			HttpServletResponse response = ServletActionContext.getResponse();
			String filename = new Timestamp(System.currentTimeMillis()) + "_分区数据.xls";
			String mimeType = ServletActionContext.getServletContext().getMimeType(filename);
			response.setContentType(mimeType + ";charset=utf-8");
			response.setHeader("Content-Disposition", "attachment;filename=" + 
					FileEncodeUtils.encodeDownloadFilename(filename, getRequest().getHeader("user-agent")));
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("分区信息1");
			HSSFRow title = sheet.createRow(0);
			title.createCell(0).setCellValue("分区编号");
			title.createCell(1).setCellValue("省");
			title.createCell(2).setCellValue("市");
			title.createCell(3).setCellValue("区");
			title.createCell(4).setCellValue("关键字");
			title.createCell(5).setCellValue("位置");
			for (Subarea s : list) {
				HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
				row.createCell(0).setCellValue(s.getId());
				row.createCell(1).setCellValue(s.getRegion().getProvince());
				row.createCell(2).setCellValue(s.getRegion().getCity());
				row.createCell(3).setCellValue(s.getRegion().getDistrict());
				row.createCell(4).setCellValue(s.getAddresskey());
				row.createCell(5).setCellValue(s.getPosition());
			}
			wb.write(response.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return NONE;
	}
	
	

	/**
	 * 获取条件查询对象
	 * @return
	 */
	private Specification<Subarea> getCondition() {
		Specification<Subarea> condition = new Specification<Subarea>() {
			@Override
			public Predicate toPredicate(Root<Subarea> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				List<Predicate> plist = new ArrayList<>();
				if(StringUtils.isNotBlank(model.getAddresskey())) {
					Predicate p = cb.like(root.get("addresskey").as(String.class), "%" + model.getAddresskey() + "%");
					plist.add(p);
				}
				
				if(model.getRegion() != null) {
					Join<Subarea, Region> join = root.join(root.getModel().getSingularAttribute("region", Region.class),JoinType.LEFT);
					if(StringUtils.isNotBlank(model.getRegion().getProvince())) {
						Predicate p1 = cb.like(join.get("province").as(String.class), "%" + model.getRegion().getProvince() + "%");
						plist.add(p1);
					}
					if(StringUtils.isNotBlank(model.getRegion().getCity())) {
						Predicate p2 = cb.like(join.get("city").as(String.class), "%" + model.getRegion().getCity() + "%");
						plist.add(p2);
					}
					if(StringUtils.isNotBlank(model.getRegion().getDistrict())) {
						Predicate p3 = cb.like(join.get("district").as(String.class), "%" + model.getRegion().getDistrict() + "%");
						plist.add(p3);
					}
				}
				if(model.getDecidedZone() != null && StringUtils.isNotBlank(model.getDecidedZone().getId())) {
					Predicate p4 = cb.equal(root.get("region").as(Region.class), model.getRegion());
					plist.add(p4);
				}
				Predicate[] predicates = new Predicate[plist.size()];
				return cb.and(plist.toArray(predicates));
			}
		};
		return condition;
	}
}
