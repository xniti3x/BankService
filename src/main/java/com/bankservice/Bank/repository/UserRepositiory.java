package com.bankservice.Bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bankservice.Bank.entity.User;

public interface UserRepositiory extends JpaRepository<User,Integer> {

}
