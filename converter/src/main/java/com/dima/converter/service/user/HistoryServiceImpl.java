package com.dima.converter.service.user;

import com.dima.converter.model.History;
import com.dima.converter.model.QueryResult;
import com.dima.converter.model.Registration;
import com.dima.converter.model.User;
import com.dima.converter.repository.HistoryRepository;
import com.dima.converter.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        history.setTimestamp(new Date());
        history.setDate(result.getQuery().getDate());
        return historyRepository.save(history);
    }
}