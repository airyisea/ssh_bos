package com.airyisea.bos.service.qp.impl;

import java.sql.Timestamp;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.DecidedZoneDao;
import com.airyisea.bos.dao.basic.RegionDao;
import com.airyisea.bos.dao.qp.NoticeBillDao;
import com.airyisea.bos.dao.qp.WorkBillDao;
import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.domain.basic.Region;
import com.airyisea.bos.domain.basic.Staff;
import com.airyisea.bos.domain.basic.Subarea;
import com.airyisea.bos.domain.qp.NoticeBill;
import com.airyisea.bos.domain.qp.WorkBill;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.qp.NoticeBillService;
import com.airyisea.crm.domain.customer.Customer;

@Service("noticeBillService")
@Transactional
public class NoticeBillServiceImpl extends BaseServiceImpl<NoticeBill, String>implements NoticeBillService {
	
	private final static String URL = ResourceBundle.getBundle("serviceUrl").getString("customer.service");
	
	private NoticeBillDao noticeBillDao;
	@Autowired
	public void setSuperDao(NoticeBillDao noticeBillDao) {
		super.setDao(noticeBillDao);
		super.setSdao(noticeBillDao);
		this.noticeBillDao = noticeBillDao;
	}
	
	@Autowired
	private WorkBillDao workBillDao;
	@Autowired
	private DecidedZoneDao decidedZoneDao;
	@Autowired
	private RegionDao regionDao;
	
	@Override
	public List<NoticeBill> findNoAssociations() {
		return noticeBillDao.findByStaffIsNull();
	}
	
	@Override
	public Customer checkTel(String telephone) {
		if(StringUtils.isNotBlank(telephone)) {
			String telUrl = URL + "/tel/" + telephone;
			return WebClient.create(telUrl).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		}else {
			return null;
		}
	}

	@Override
	public void add(NoticeBill model, String province, String city, String district) {
		
		//根据电话获取用户
		Customer existCustomer = WebClient.create(URL + "/tel/" + model.getTelephone()).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		if(existCustomer == null) {
			//不存在,创建新客户,先不进行保存,待匹配分区后保存
			existCustomer = new Customer();
			existCustomer.setName(model.getCustomerName());
			existCustomer.setTelephone(model.getTelephone());
			existCustomer.setAddress(model.getPickaddress());
			existCustomer.setStation(model.getCustomerStation());
		}else {
			//已存在客户,封装客户id进model
			model.setCustomerId(existCustomer.getId() + "");
			//判断地址是否发生变化
			if(model.getPickaddress().equals(existCustomer.getAddress())) {
				//地址未改变,是否已经绑定定区
				if(StringUtils.isNotBlank(existCustomer.getDecidedzoneId())) {
					//已绑定定区
					DecidedZone decidedZone = decidedZoneDao.findOne(existCustomer.getDecidedzoneId());
					//定区是否存在
					if(decidedZone != null) {
						Staff staff = decidedZone.getStaff();
						//本定区是否有取派员
						if(staff != null) {
							//有取派员,匹配成功
							model.setOrdertype("自动");
							model.setStaff(staff);
							//生成工作单
							WorkBill workBill = createWorkBill(model);
							//保存通知单,工作单
							noticeBillDao.save(model);
							workBillDao.save(workBill);
							return;
						}
					}
					//本定区无取派员或定区不存在,保存通知单,转手工派单
					model.setOrdertype("人工");
					noticeBillDao.save(model);
					return;
				}
			}else {
				//地址改变,封装新地址进客户
				existCustomer.setAddress(model.getPickaddress());
			}
		}
		//地址已经一致,新客户,无id!旧客户有id!
		String addrUrl = URL + "/addr/" + model.getPickaddress();
		//根据crm约定,预定义客户的定区为未定区,后续如果匹配到则修改定区
		existCustomer.setDecidedzoneId("''");
		Customer addrCustomer = WebClient.create(addrUrl).accept(MediaType.APPLICATION_JSON).get(Customer.class);
		//=============根据输入的地址查询地址库进行精确匹配===================================
		if(addrCustomer != null) {
			//根据地址精确匹配
			DecidedZone decidedZone = decidedZoneDao.findOne(addrCustomer.getDecidedzoneId());
			//定区是否存在
			if(decidedZone != null) {
				//存在,将定区信息封装入客户
				existCustomer.setDecidedzoneId(decidedZone.getId());
				Staff staff = decidedZone.getStaff();
				//本定区是否有取派员
				if(staff != null) {
					//有取派员,匹配成功
					model.setOrdertype("自动");
					model.setStaff(staff);
					//保存新用户,更新旧用户地址
					int cid = saveOrUpdateCustomer(existCustomer);
					model.setCustomerId(cid + "");
					//生成工作单
					WorkBill workBill = createWorkBill(model);
					//保存通知单,工作单
					noticeBillDao.save(model);
					workBillDao.save(workBill);
					return;
				}
			}
			//本定区无取派员或定区不存在,保存通知单,转手工派单
			int cid = saveOrUpdateCustomer(existCustomer);
			model.setCustomerId(cid + "");
			model.setOrdertype("人工");
			noticeBillDao.save(model);
			return;
		}
		//===============输入的地址在地址库中不存在,进行模糊匹配=================================
		//根据省,市,区获取region
		Region region = regionDao.findByProvinceAndCityAndDistrictLike(province, city, district);
		//根据region获取所有分区
		Set<Subarea> subareas = region.getSubareas();
		//遍历分区,模糊匹配关键字
		for (Subarea subarea : subareas) {
			if(model.getPickaddress().contains(subarea.getAddresskey())) {
				//匹配成功
				//封装定区信息进客户
				existCustomer.setDecidedzoneId(subarea.getDecidedZone().getId());
				//本定区是否有派件员
				Staff staff = subarea.getDecidedZone().getStaff();
				if(staff != null) {
					//保存或更新客户
					int cid = saveOrUpdateCustomer(existCustomer);
					model.setCustomerId(cid + "");
					//有派件员,自动派单成功,保存通知单和工单
					model.setStaff(staff);
					model.setOrdertype("自动");
					WorkBill workBill = createWorkBill(model);
					noticeBillDao.save(model);
					workBillDao.save(workBill);
					return;
				}
			}
		}
		//人工派单
		int cid = saveOrUpdateCustomer(existCustomer);
		model.setCustomerId(cid + "");
		model.setOrdertype("人工");
		noticeBillDao.save(model);
		return;
		
	}

	private int saveOrUpdateCustomer(Customer existCustomer) {
		int cid = existCustomer.getId();
		if(cid == 0) {
			//新客户,保存到crm,将返回的id装入model
			Response response = WebClient.create(URL + "/").accept(MediaType.APPLICATION_JSON).put(existCustomer);
			Customer customer = response.readEntity(Customer.class);
			cid = customer.getId();
			existCustomer.setId(cid);
		}else {
			//旧客户,向crm更新地址和定区
			String updateUrl = URL + "/addr/" + existCustomer.getAddress() + "/" + 
					existCustomer.getDecidedzoneId() + "/" + existCustomer.getId();
			WebClient.create(updateUrl).post(null);
		}
		return cid;
	}

	private WorkBill createWorkBill(NoticeBill model) {
		WorkBill workBill = new WorkBill();
		workBill.setAttachbilltimes(0);
		workBill.setBuildtime(new Timestamp(System.currentTimeMillis()));
		workBill.setNoticeBill(model);
		workBill.setPickstate("新单");
		workBill.setRemark(model.getRemark());
		workBill.setStaff(model.getStaff());
		workBill.setType("新");
		return workBill;
	}

	

}
