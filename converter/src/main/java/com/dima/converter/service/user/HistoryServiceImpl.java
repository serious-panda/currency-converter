package com.dima.converter.service.user;

import com.dima.converter.model.jpa.History;
import com.dima.converter.model.QueryResult;
import com.dima.converter.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class HistoryServiceImpl implements HistoryService {

    private final HistoryRepository historyRepository;

    @Autowired
    public HistoryServiceImpl(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    @Override
    public List<History> getUserHistory(String username) {
        return historyRepository.findFirst10ByUsernameOrderByTimestampDesc(username);
    }

    @Override
    public History create(String username, QueryResult result) {
        History history = new History();
        history.setUsername(username);
        history.setAmount(result.getQuery().getAmount());
        history.setBase(result.getQuery().getFrom());
        history.setResult(result.getResult());
        history.setQuote(result.getQuery().getTo());
        history.setTimestamp(LocalDate.now());
        history.setDate(result.getQuery().getDate());
        return historyRepository.save(history);
    }
}