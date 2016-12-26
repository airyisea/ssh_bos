package com.airyisea.bos.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.airyisea.bos.domain.user.User;
@Repository("userDao")
public interface UserDao extends JpaRepository<User, Integer> {
	User findByUsernameAndPassword(String name,String password);
	
	@Modifying
	@Query("update User set password = ?1 where id = ?2")
	void updatePassword(String newPwd, Integer id);
}
