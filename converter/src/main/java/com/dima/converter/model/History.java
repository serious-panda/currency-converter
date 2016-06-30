package com.dima.converter.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "history", uniqueConstraints = @UniqueConstraint(columnNames = {"username", "timestamp"}))
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "timestamp", nullable = false)
    private Date timestamp;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "base", nullable = false)
    private String base;

    @Column(name = "result", nullable = false)
    private double result;

    @Column(name = "quote", nullable = false)
    private String quote;

    @Column(name = "date")
    private Date date;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public Date getDate() { return date; }

    public void setDate(Date date) {
        this.date = date;
    }
}
