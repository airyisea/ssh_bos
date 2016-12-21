package com.airyisea.bos.dao.user;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.airyisea.bos.domain.user.User;
@Repository("DemoDao")
public interface DemoDao extends JpaRepository<User, Integer> {
	//约定方法名生成jdql语句
	User findByUsernameAndPassword(String name,String password);
	
	//命名查询
	User login(String name,String password);
	
	//使用@Query
	@Query("from User where password = ?2 and username = ?1")
	User login2(String name,String password);
	
	//使用sql
	@Query(nativeQuery=true,value="select * from t_user where username = ? and password = ?")
	User login3(String name,String password);
	
	//使用命名参数
	@Query("from User where username =:name and password = :password")
	User login4(@Param("name")String name,@Param("password")String password);
	
}
