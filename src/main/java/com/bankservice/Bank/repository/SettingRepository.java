package com.bankservice.Bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.bankservice.Bank.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting,Integer>{

    @Query("SELECT value FROM Setting s WHERE s.name = ?1")
    String findByName(String name);
}
