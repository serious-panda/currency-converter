package com.dima.converter.service;

import com.dima.converter.model.ConversionQuery;
import com.dima.converter.model.QueryResult;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueryHistory {
    private final static Queue<Entry> EMPTY_HISTORY = new LinkedList<>();
    private final static int HISTORY_SIZE = 10;
    private Map<String, Queue<Entry>> history = new HashMap<>();


    public void add(String user, Entry entry){
        if (!history.containsKey(user)){
            history.put(user, new LinkedList<>());
        }
        Queue<Entry> entries = history.get(user);

        if (entries.size()>=HISTORY_SIZE){
            entries.remove();
        }
        entries.add(entry);
    }

    public Entry[] get(String user){
        Queue<Entry> tmp = history.getOrDefault(user, EMPTY_HISTORY);
        Entry[] arr = tmp.toArray(new Entry[tmp.size()]);
        Collections.reverse(Arrays.asList(arr));
        return arr;
    }

    public static class Entry {
        private Date timestamp;
        QueryResult queryResult;

        public Entry(Date timestamp, QueryResult queryResult) {
            this.timestamp = timestamp;
            this.queryResult = queryResult;
        }

        public Date getTimestamp() {
            return timestamp;
        }

        public QueryResult getQueryResult() {
            return queryResult;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "timestamp=" + timestamp +
                    ", queryResult=" + queryResult +
                    '}';
        }
    }
}
