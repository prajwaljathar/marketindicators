package com.example.demo.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.WatchlistEntity;

@Repository
public interface WatchlistRepository extends JpaRepository<WatchlistEntity, String> {

	//Optional<WatchlistEntity> findById(String instrumentKey);
	Optional<WatchlistEntity> findByInstrumentKey(String instrumentKey);
}


