package com.dima.converter.model;

import com.dima.converter.model.validation.LocalDatePast;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ConversionQuery {
    @NotNull
    private String from;

    @NotNull
    private String to;

    private double amount = 1;

    @LocalDatePast(message = "{errors.dateInFuture}")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate date;

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

    public LocalDate getDate() {return date; }

    public void setDate(LocalDate date) { this.date = date; }

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
