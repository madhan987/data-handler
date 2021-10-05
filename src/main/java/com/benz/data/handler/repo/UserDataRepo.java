package com.benz.data.handler.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.benz.data.handler.entity.UserEntity;

@Repository
public interface UserDataRepo extends JpaRepository<UserEntity, Integer> {

	UserEntity findByUserEmail(String emailId);
}
