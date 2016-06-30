package com.dima.converter.model;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

public class PastDateValidator implements ConstraintValidator<LocalDatePast, LocalDate> {


    @Override
    public void initialize(LocalDatePast localDatePast) {

    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return localDate != null && localDate.isBefore(LocalDate.now().minusYears(5));
    }
}
