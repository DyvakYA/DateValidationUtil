package com.validation;

import org.apache.log4j.Logger;
import org.junit.Test;

import java.time.LocalDate;

import static com.validation.DateValidator.*;
import static org.junit.Assert.assertTrue;

public class DateValidatorTest {

    private static final Logger log = Logger.getLogger(DateValidatorTest.class);

    @Test
    public void localDateTest_whenFromLessThenTo() {
        LocalDate from = LocalDate.of(2020, 12, 19);
        LocalDate to = LocalDate.of(2020, 12, 20);
        log.info(from.atStartOfDay().toLocalTime() + " : " + to.atStartOfDay().toLocalTime());
        boolean result = validate(from, to);
        assertTrue(result);
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_whenFromMoreThenTo() {
        LocalDate from = LocalDate.of(2020, 12, 20);
        LocalDate to = LocalDate.of(2012, 12, 12);
        boolean result = validate(from, to);
        assertTrue(result);
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_whenInclusiveOff_sameDates() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2020, 12, 12);
        validate(from, to);
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_whenFromIsExclusive_sameDates() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2020, 12, 12);
        validate(exclusive(from), to);
    }

    @Test
    public void localDateTest_whenFromIsExclusiveAndToIsInclusive_sameDates() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2020, 12, 12);
        validate(exclusive(from), inclusive(to));
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_whenInclusiveOnFirstParameter_sameDates() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2020, 12, 12);
        boolean result = validate(inclusive(from), to);
        assertTrue(result);
    }

    @Test
    public void localDateTest_whenInclusiveOnSecondParameter_sameDates() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2020, 12, 12);
        boolean result = validate(from, inclusive(to));
        assertTrue(result);
    }

    @Test
    public void localDateTest_whenInclusiveOnBoth_sameDates() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2020, 12, 12);
        boolean result = validate(inclusive(from), inclusive(to));
        assertTrue(result);
    }

    @Test
    public void localDateTest_fromInPast_toInFuture() {
        LocalDate from = LocalDate.of(2012, 12, 12);
        LocalDate to = LocalDate.of(2020, 12, 12);
        boolean result = validate(inPastOn(from), inFutureOn(to));
        assertTrue(result);
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_fromInPastButFuture_toInFuture() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2012, 12, 12);
        boolean result = validate(inPastOn(from), to);
        assertTrue(result);
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_fromInPast_toInFutureButPast() {
        LocalDate from = LocalDate.of(2020, 12, 12);
        LocalDate to = LocalDate.of(2012, 12, 12);
        boolean result = validate(from, inFutureOn(to));
        assertTrue(result);
    }

    @Test
    public void localDateTest_fromInPastAndInFuture() {
        LocalDate from = LocalDate.of(2010, 12, 12);
        LocalDate to = LocalDate.of(2012, 12, 12);
        boolean result = validate(inFutureOn(inPastOn(from)), to);
        assertTrue(result);
    }

    @Test
    public void localDateTest_fromInPastAndInFutureAndInclusiveAndExclusive() {
        LocalDate from = LocalDate.of(2010, 12, 12);
        LocalDate to = LocalDate.of(2012, 12, 12);
        boolean result = validate(inFutureOff(inclusive(exclusive(exclusive(inFutureOn(inPastOn(inclusive(from))))))), to);
        assertTrue(result);
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_fromIsNull() {
        LocalDate from = null;
        LocalDate to = LocalDate.of(2012, 12, 12);
        boolean result = validate(from, to);
        assertTrue(result);
    }

    @Test(expected = DateValidationException.class)
    public void localDateTest_toIsNull() {
        LocalDate from = LocalDate.of(2012, 12, 12);
        LocalDate to = null;
        boolean result = validate(from, to);
        assertTrue(result);
    }

}
