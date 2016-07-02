package com.dima.converter.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import java.util.Date;

public class ConversionQuery {
    @NotNull
    private String from;

    @NotNull
    private String to;

    private double amount = 1;

    @Past(message = "Invalid date. Must be in the past.")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private Date date;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getAmount() { return amount; }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Date getDate() {return date; }

    public void setDate(Date date) { this.date = date; }

    @Override
    public String toString() {
        return "ConversionQuery{" +
                "from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", amount=" + amount +
                ", date=" + date +
                '}';
    }
}
