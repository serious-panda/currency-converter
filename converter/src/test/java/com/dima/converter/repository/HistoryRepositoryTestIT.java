package com.dima.converter.repository;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import com.dima.converter.model.History;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;

@SpringApplicationConfiguration(TestHistoryRepositoryConfig.class)
public class HistoryRepositoryTestIT extends AbstractTransactionalJUnit4SpringContextTests {

    @Autowired
    HistoryRepository repo;


    private History saveEvent(){

        History event = new History();
        event.setBase("abc");
        event.setQuote("bbb");
        event.setResult(12);
        event.setAmount(21);
        event.setUsername("username");
        event.setTimestamp(new Date(System.currentTimeMillis()));

        History savedEvent = repo.save(event);
        repo.flush();
        return savedEvent;
    }

    @Test
    public void findAll() {
        List<History> events = repo.findAll();
        assertThat(events).isNotNull();
        assertThat(events.size()).isEqualTo(0);
        saveEvent();
        events = repo.findAll();
        assertThat(events.size()).isGreaterThan(0);
    }

    @Test
    public void findOne() {
        History event = repo.findOne(1L);
        assertThat(event).isNull();
        event = saveEvent();
        History result = repo.findOne(event.getId());
        assertThat(result).isNotNull();
    }

    @Test
    public void save() {
        final int numRowsInTable = countNumEvents();
        final LocalDate tomorrow = LocalDate.now().plusDays(1);

        History event = new History();
        event.setBase("abc");
        event.setQuote("bbb");
        event.setResult(12);
        event.setAmount(21);
        event.setUsername("username");
        event.setTimestamp(new Date(System.currentTimeMillis()));

        History savedEvent = repo.save(event);
        repo.flush();

        assertThat(savedEvent.getId()).isNotNull();
        assertNumEvents(numRowsInTable + 1);
        History retrievedSavedEvent = repo.findOne(savedEvent.getId());
        assertThat(retrievedSavedEvent).isEqualTo(event);
        //TODO figure out the local date feature
        //assertThat(retrievedSavedEvent.getEventDate()).isEqualTo(tomorrow);
    }

    private int countNumEvents() {
        return countRowsInTable("history");
    }

    private void assertNumEvents(int expectedNumRows) {
        assertThat(countNumEvents()).isEqualTo(expectedNumRows);
    }

}