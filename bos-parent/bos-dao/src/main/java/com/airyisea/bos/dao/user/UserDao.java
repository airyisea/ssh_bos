package com.airyisea.bos.dao.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.airyisea.bos.domain.user.User;
@Repository("userDao")
public interface UserDao extends JpaRepository<User, Integer> {
	User findByUsernameAndPassword(String name,String password);
}
