package com.airyisea.bos.action.basic;


import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
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
import com.airyisea.bos.utils.PinYin4jUtils;
@SuppressWarnings("all")
@Controller("regionAction")
@Scope("prototype")
@Namespace("/basic")
@ParentPackage("bos")
public class RegionAction extends BaseAction<Region> {
	
	
	//======================ajax===========================================
	/**
	 * 批量导入区域数据
	 * @return
	 * @throws Exception
	 */
	@Action(value="region_importRegion",results={@Result(name="importRegion",type="json")})
	public String importRegion() {
		try {
			List<Region> list = new ArrayList<Region>();
			//获取工作簿
			HSSFWorkbook wb = new HSSFWorkbook(new FileInputStream(upload));
			HSSFSheet sheet = wb.getSheetAt(0);
			//遍历每行记录,插入集合中
			for (Row row : sheet) {
				if(row.getRowNum() == 0) {
					continue;
				}
				Region region = new Region();
				region.setId(row.getCell(0).getStringCellValue());
				region.setProvince(row.getCell(1).getStringCellValue());
				region.setCity(row.getCell(2).getStringCellValue());
				region.setDistrict(row.getCell(3).getStringCellValue());
				region.setPostcode(row.getCell(4).getStringCellValue());
				region.setCitycode(getCitycode(region.getCity()));
				region.setShortcode(getShortcode(region.getProvince(),region.getCity(),region.getDistrict()));
				list.add(region);
			}
			//调用批量保存方法
			facadeService.getRegionService().save(list);
			push(true);
		} catch (Exception e) {
			e.printStackTrace();
			push(false);
		}
		return "importRegion";
	}
	/**
	 * 分页查询区域
	 * @return
	 * @throws Exception
	 */
	@Action(value="region_queryPage")
	public String queryPage() {
		
		Specification<Region> c1 = getLikeCondition("province","city","district","shortcode");
		Specification<Region> c2 = getEqCondition("id");
		Page<Region> pageResponse = facadeService.getRegionService().queryPage(getAndCondition(c1,c2),getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
	}
	/**
	 * 校验id是否重复
	 * @return
	 * @throws Exception
	 */
	@Action(value="region_checkId",results={@Result(name="checkId",type="json")})
	public String checkId() {
		Region existRegion = facadeService.getRegionService().checkId(model.getId());
		if(existRegion == null) {
			push(true);
		}else {
			push(false);
		}
		return "checkId";
	}
	
	/**
	 * 根据省/市/区模糊查询
	 * @return
	 * @throws Exception
	 */
	@Action(value="region_list",results={@Result(name="list",type="json")})
	public String list() {
		List<Region> list = facadeService.getRegionService().queryList(getParameter("q"));
		push(list);
		return "list";
	}
	/**
	 * 省市区三级联动
	 * @return
	 * @throws Exception
	 */
	@Action(value="region_getPCDAjax",results={@Result(name="getPCDAjax",type="json")})
	public String getPCDAjax() {
		List<String> list = facadeService.getRegionService().getPCD(model.getProvince(),model.getCity());
		push(list);
		return "getPCDAjax";
	}
	
	
	//=================================================================
	/**
	 * 添加区域
	 * @return
	 * @throws Exception
	 */
	@Action(value="region_add",results={@Result(name="add",location="/WEB-INF/pages/base/region.jsp")})
	public String add() {
		if(StringUtils.isBlank(model.getCitycode())) {
			model.setCitycode(getCitycode(model.getCity()));
		}
		if(StringUtils.isBlank(model.getShortcode())) {
			model.setShortcode(getShortcode(model.getProvince(), model.getCity(), model.getDistrict()));
		}
		facadeService.getRegionService().add(model);
		return "add";
	}
	
	//============================================================
	/**
	 * 获取区域拼音首字母
	 * @param province:省
	 * @param city:市
	 * @param district:区 
	 * @return
	 */
	private String getShortcode(String province, String city, String district) {
		if(StringUtils.isNotBlank(province)) {
			province = province.substring(0, province.length() - 1);
		}else {
			province = "";
		}
		
		if(StringUtils.isNotBlank(city)) {
			city = city.substring(0, city.length() - 1);
		}else {
			city = "";
		}
		if(StringUtils.isNotBlank(district)) {
			district = district.substring(0, district.length() - 1);
		}else {
			district = "";
		}
		if(province.equals(city)) {
			//直辖市
			String[] strings = PinYin4jUtils.getHeadByString(province + district);
			StringBuffer sb = new StringBuffer();
			for (String string : strings) {
				sb.append(string);
			}
			return sb.toString();
		}else {
			String[] strings = PinYin4jUtils.getHeadByString(province + city + district);
			StringBuffer sb = new StringBuffer();
			for (String string : strings) {
				sb.append(string);
			}
			return sb.toString();
		}
	}

	/**
	 * 获取城市拼音
	 * @param city:市
	 * @return
	 */
	private String getCitycode(String city) {
		if(StringUtils.isNotBlank(city)) {
			city = city.substring(0, city.length() - 1);
			return PinYin4jUtils.hanziToPinyin(city,"");
		}else {
			return null;
		}
	}
	
	
	
}
