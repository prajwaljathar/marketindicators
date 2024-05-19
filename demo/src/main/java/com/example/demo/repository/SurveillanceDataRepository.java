package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.SurveillanceData;

@Repository
public interface SurveillanceDataRepository extends JpaRepository<SurveillanceData, Long> {
}