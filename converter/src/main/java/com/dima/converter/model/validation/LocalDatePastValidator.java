package com.dima.converter.model.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class LocalDatePastValidator implements ConstraintValidator<LocalDatePast, LocalDate> {


    @Override
    public void initialize(LocalDatePast localDatePast) {

    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext constraintValidatorContext) {
        return /*localDate != null &&*/ localDate == null || localDate.isBefore(LocalDate.now().minusYears(5));
    }
}
