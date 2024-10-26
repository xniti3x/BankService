package com.bankservice.Bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.bankservice.Bank.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting,Integer>{

    @Query("SELECT value FROM Setting s WHERE s.name = ?1")
    String findByName(String name);

    @Modifying
    @Transactional
    @Query("UPDATE Setting s SET s.value = :value WHERE s.name = 'USER_ACTIVE'")
    void setName(@Param("value") String value);
}
