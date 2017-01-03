package com.airyisea.bos.dao.qp.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.wltea.analyzer.lucene.IKAnalyzer;

import com.airyisea.bos.domain.qp.WorkOrderManage;


@SuppressWarnings("unchecked")
public class WorkOrderManageDaoImpl {
	@PersistenceContext
	private EntityManager em;
	
	public Page<WorkOrderManage> queryIndex(Pageable pageRequest, String conditionName, String conditionValue) {
		try {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
			QueryParser parser = new QueryParser(Version.LUCENE_31, conditionName, new IKAnalyzer());
			Query query = parser.parse(conditionValue);
			WildcardQuery query2 = new WildcardQuery(new Term(conditionName, "%" +conditionValue+ "%"));
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(query, Occur.SHOULD);
			booleanQuery.add(query2,Occur.SHOULD);
			FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(booleanQuery);
			int total = fullTextQuery.getResultSize();
			fullTextQuery.setFirstResult(pageRequest.getOffset());
			fullTextQuery.setMaxResults(pageRequest.getPageSize());
			List<WorkOrderManage> list = fullTextQuery.getResultList();
			Page<WorkOrderManage> page = new PageImpl<>(list,pageRequest,total);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
