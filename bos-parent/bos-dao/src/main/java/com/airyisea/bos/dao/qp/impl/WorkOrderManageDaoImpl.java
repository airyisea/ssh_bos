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
	//注入EntityManager
	@PersistenceContext
	private EntityManager em;
	
	public Page<WorkOrderManage> queryIndex(Pageable pageRequest, String conditionName, String conditionValue) {
		try {
			//使用EntityManager创造全文检索引擎：FullTextEntityManager
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
			//创建lucence的query
			//第一类：长词分词模糊匹配，参数1：lucence的版本，参数2：索引字段的名字。参数3：分词器
			QueryParser parser = new QueryParser(Version.LUCENE_31, conditionName, new IKAnalyzer());
			//查询关键字（解析器会自动分词后再查询）
			Query query = parser.parse(conditionValue);
			//第二类，短词直接匹配
			WildcardQuery query2 = new WildcardQuery(new Term(conditionName, "%" +conditionValue+ "%"));
			//将两个query合并，ocurr：should或，must与
			BooleanQuery booleanQuery = new BooleanQuery();
			booleanQuery.add(query, Occur.SHOULD);
			booleanQuery.add(query2,Occur.SHOULD);
			//创建全文检索对象，参数1：query对象，参数2：实体类
			FullTextQuery fullTextQuery = fullTextEntityManager.createFullTextQuery(booleanQuery,WorkOrderManage.class);
			//查询总记录数
			int total = fullTextQuery.getResultSize();
			//设置分页
			fullTextQuery.setFirstResult(pageRequest.getOffset());
			fullTextQuery.setMaxResults(pageRequest.getPageSize());
			//获取查询结果
			List<WorkOrderManage> list = fullTextQuery.getResultList();
			Page<WorkOrderManage> page = new PageImpl<>(list,pageRequest,total);
			return page;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}
