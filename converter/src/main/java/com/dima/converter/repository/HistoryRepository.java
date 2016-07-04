package com.dima.converter.repository;

import com.dima.converter.model.jpa.History;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {
    List<History> findFirst10ByUsernameOrderByTimestampDesc(String username);
}