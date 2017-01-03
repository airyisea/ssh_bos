package com.airyisea.bos.service.basic.impl;

import java.util.List;
import java.util.ResourceBundle;

import javax.ws.rs.core.MediaType;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.airyisea.bos.dao.basic.DecidedZoneDao;
import com.airyisea.bos.dao.basic.SubareaDao;
import com.airyisea.bos.domain.basic.DecidedZone;
import com.airyisea.bos.service.base.impl.BaseServiceImpl;
import com.airyisea.bos.service.basic.DecidedZoneService;
import com.airyisea.crm.domain.customer.Customer;
@SuppressWarnings("unchecked")
@Service("decidedZoneService")
@Transactional
public class DecidedZoneServiceImpl extends BaseServiceImpl<DecidedZone, String> implements DecidedZoneService {
	private DecidedZoneDao decidedZoneDao;
	@Autowired
	private SubareaDao subareaDao;
	
	@Autowired
	public void setSuperDao(DecidedZoneDao decidedZoneDao) {
		super.setDao(decidedZoneDao);
		super.setSdao(decidedZoneDao);
		this.decidedZoneDao = decidedZoneDao;
	}
	
	@Override
	public void add(DecidedZone model, String[] subareas) {
		if(model.getStaff() != null && StringUtils.isBlank(model.getStaff().getId()))
			model.setStaff(null);
		decidedZoneDao.save(model);
		if(subareas != null && subareas.length != 0) {
			for (String sid : subareas) {
				subareaDao.associateDecidedZone(sid,model);
			}
		}
	}

	@Override
	public DecidedZone checkId(String id) {
		return decidedZoneDao.findOne(id);
	}

	/*@Override
	public Page<DecidedZone> queryPage(Specification<DecidedZone> condition, Pageable pageRequest) {
		return decidedZoneDao.sfindAll(condition, pageRequest);
	}*/

	
	@Override
	public List<Customer> getNoAssociationCustomer() {
		String url = ResourceBundle.getBundle("serviceUrl").getString("customer.association.service");
		return (List<Customer>)WebClient.create(url).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
	}

	@Override
	public List<Customer> getAssociationCustomer(String id) {
		String url = ResourceBundle.getBundle("serviceUrl").getString("customer.association.service");
		url = url + "/" + id;
		return (List<Customer>)WebClient.create(url).accept(MediaType.APPLICATION_JSON).getCollection(Customer.class);
	}

	@Override
	public void assignCustomersToDecidedzone(String[] cids, String id) {
		String url = ResourceBundle.getBundle("serviceUrl").getString("customer.association.service");
		String cidUrl = "";
		if(cids != null && cids.length != 0) {
			for (String cid : cids) {
				cidUrl += cid + ",";
			}
			cidUrl = cidUrl.substring(0, cidUrl.length() - 1 );
		}else {
			cidUrl = "''";
		}
		url = url + "/" + cidUrl + "/" + id;
		WebClient.create(url).post(null);
	}

}
