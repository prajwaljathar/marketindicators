package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Instrument;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Long>  {

	Instrument findByInstrumentKey(String instrumentKey);


}
