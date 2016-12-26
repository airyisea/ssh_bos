package com.airyisea.bos.action.basic;


import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
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
@SuppressWarnings("serial")
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
	public String importRegion() throws Exception {
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
		try {
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
	public String queryPage() throws Exception {
		
		Specification<Region> c1 = getLikeCondition("province","city","district","shortcode");
		Specification<Region> c2 = getEqCondition("id");
		Page<Region> pageResponse = facadeService.getRegionService().queryPage(getAndCondition(c1,c2),getPageRequest());
		setPageResponse(pageResponse);
		return "queryPage";
	}
	
	@Action(value="region_checkId",results={@Result(name="checkId",type="json")})
	public String checkId() throws Exception {
		Region existRegion = facadeService.getRegionService().checkId(model.getId());
		if(existRegion == null) {
			push(true);
		}else {
			push(false);
		}
		return "checkId";
	}
	
	
	
	//======================ajax===========================================
	/**
	 * 添加区域
	 * @return
	 * @throws Exception
	 */
	@Action(value="region_add",results={@Result(name="add",location="/WEB-INF/pages/base/region.jsp")})
	public String add() throws Exception {
		if(StringUtils.isBlank(model.getCitycode())) {
			model.setCitycode(getCitycode(model.getCity()));
		}
		if(StringUtils.isBlank(model.getShortcode())) {
			model.setShortcode(getShortcode(model.getProvince(), model.getCity(), model.getDistrict()));
		}
		facadeService.getRegionService().add(model);
		return "add";
	}
	
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
	
	
	/**
	 * 获取条件查询Specification的实例
	 * @return
	 */
	private Specification<Region> getCondition() {
		Specification<Region> condition = new Specification<Region>() {
			@Override
			public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				// TODO Auto-generated method stub
				return null;
			}
		};
			
		return condition;
	}
	
	/**
	 * @param param:需要查询的属性
	 * @return
	 */
	private Specification<Region> getCondition(final String...params) {
		Specification<Region> condition = new Specification<Region>() {
			@Override
			public Predicate toPredicate(Root<Region> root, CriteriaQuery<?> query,
					CriteriaBuilder cb) {
				try {
					List<String> paramList = new ArrayList<String>();
					for (int i = 0; i < params.length; i++) {
						paramList.add(params[i]);
					}
					List<Predicate> plist = new ArrayList<Predicate>();
					Class<? extends Region> clazz = root.getJavaType();
					BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
					PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
					for (PropertyDescriptor pd : pds) {
						String name = pd.getName();
						if(paramList.contains(name)) {
							Object param = pd.getReadMethod().invoke(model);
							Predicate p = cb.like(root.get(name).as(String.class), "%" + param + "%");
							plist.add(p);
						}
					}
					Predicate[] predicates = new Predicate[plist.size()];
					return cb.and(plist.toArray(predicates));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
		};
		
		return condition;
	}

}
