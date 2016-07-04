package com.dima.converter.service.user;

import com.dima.converter.model.jpa.History;
import com.dima.converter.model.QueryResult;

import java.util.List;

public interface HistoryService {

    List<History> getUserHistory(String username);

    History create(String username, QueryResult result);

}