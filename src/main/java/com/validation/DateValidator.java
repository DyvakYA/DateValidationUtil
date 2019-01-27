package com.validation;

import org.apache.log4j.Logger;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateValidator {

    private static final Logger log = Logger.getLogger(DateValidator.class);

    // Main method for validation
    public static boolean validate(DateContainer from, DateContainer to) {
        if (validDateContainer(from) & validDateContainer(to)) {
            checkInclusions(checkFromDateContainer(from), checkToDateContainer(to));
            checkPastAndFuture(from);
            checkPastAndFuture(to);
            return true;
        } else {
            throw new DateValidationException(String.format("Date from - %s and to - %s must be not a null",
                    from.getDate(), to.getDate()));
        }
    }

    // Check inclusions in both date containers
    private static void checkInclusions(DateContainer from, DateContainer to) {
        LocalDate fromDate = from.getDate();
        LocalDate toDate = to.getDate();
        long daysBetween = ChronoUnit.DAYS.between(fromDate, toDate);

        log.info(String.format("Days between dates: %s", daysBetween));

        log.info(String.format("Date from : %s, Date to : %s",
                fromDate.atStartOfDay().toString(),
                toDate.atStartOfDay().toString()));
        if (to.isInclusive()) {
            if (daysBetween < 0) {
                throw new DateValidationException(String.format("Date %s must be less or equals date %s",
                        fromDate, toDate));
            }
        } else if (from.isInclusive() & !to.isInclusive()) {
            if (daysBetween <= 0) {
                throw new DateValidationException(String.format("Date %s must be less then date %s",
                        fromDate, toDate));
            }
        } else if (!from.isInclusive() & !to.isInclusive()) {
            if (daysBetween < 0) {
                throw new DateValidationException(String.format("Date %s must be less or equals date %s",
                        fromDate, toDate));
            }
        }
    }

    // Add 1 day for date from if inclusion is false
    private static DateContainer checkFromDateContainer(DateContainer container) {
        if (!container.isInclusive()) {
            container.setDate(container.getDate().plusDays(1));
        }
        return container;
    }

    // Add 1 day for date to if inclusion is true
    private static DateContainer checkToDateContainer(DateContainer container) {
        if (container.isInclusive()) {
            container.setDate(container.getDate().plusDays(1));
        }
        return container;
    }

    // Check past and future parameters
    private static boolean checkPastAndFuture(DateContainer container) {
        if (container.inPast & !container.inFuture) {
            if (ChronoUnit.DAYS.between(LocalDate.now().atStartOfDay().toLocalDate(), container.getDate()) > 1) {
                throw new DateValidationException(String.format("Date %s must be in past", container.getDate().toString()));
            }
        } else if (container.inFuture & !container.inPast) {
            if (ChronoUnit.DAYS.between(LocalDate.now().atStartOfDay().toLocalDate(), container.getDate()) < 1) {
                throw new DateValidationException(String.format("Date %s must be in future", container.getDate().toString()));
            }
        }
        return true;
    }

    // check date container for DateContainer is not null and LocalDate is not null
    private static boolean validDateContainer(DateContainer dateContainer) {
        return (dateContainer != null & dateContainer.getDate() != null) ? true : false;
    }

    // Overloading methods
    public static boolean validate(DateContainer from, LocalDate to) {
        return validate(from, new DateContainer(false, false, false, to));
    }

    public static boolean validate(LocalDate from, DateContainer to) {
        return validate(new DateContainer(true, false, false, from), to);
    }

    public static boolean validate(LocalDate from, LocalDate to) {
        return validate(new DateContainer(true, false, false, from),
                new DateContainer(false, false, false, to));
    }

    public static DateContainer inPastOn(LocalDate date) {
        return new DateContainer(false, true, false, date);
    }

    public static DateContainer inPastOn(DateContainer dateContainer) {
        dateContainer.setInPast(true);
        return dateContainer;
    }

    public static DateContainer inPastOff(LocalDate date) {
        return new DateContainer(false, false, false, date);
    }

    public static DateContainer inPastOff(DateContainer dateContainer) {
        dateContainer.setInPast(false);
        return dateContainer;
    }

    public static DateContainer inFutureOn(LocalDate date) {
        return new DateContainer(false, false, true, date);
    }

    public static DateContainer inFutureOn(DateContainer dateContainer) {
        dateContainer.setInFuture(true);
        return dateContainer;
    }

    public static DateContainer inFutureOff(LocalDate date) {
        return new DateContainer(false, false, false, date);
    }

    public static DateContainer inFutureOff(DateContainer dateContainer) {
        dateContainer.setInFuture(false);
        return dateContainer;
    }

    public static DateContainer inclusive(LocalDate date) {
        return new DateContainer(true, false, false, date);
    }

    public static DateContainer inclusive(DateContainer dateContainer) {
        dateContainer.setInclusive(true);
        return dateContainer;
    }

    public static DateContainer exclusive(LocalDate date) {
        return new DateContainer(false, false, false, date);
    }

    public static DateContainer exclusive(DateContainer dateContainer) {
        dateContainer.setInclusive(false);
        return dateContainer;
    }

    // Date container entity included parameters for validation
    private static class DateContainer {
        private boolean inclusive;
        private boolean inPast;
        private boolean inFuture;
        private LocalDate date;

        public DateContainer(boolean inclusive, boolean inPast, boolean inFuture, LocalDate date) {
            this.inclusive = inclusive;
            this.inPast = inPast;
            this.inFuture = inFuture;
            this.date = date;
        }

        public boolean isInclusive() {
            return inclusive;
        }

        public void setInclusive(boolean inclusive) {
            this.inclusive = inclusive;
        }

        public boolean isInPast() {
            return inPast;
        }

        public void setInPast(boolean inPast) {
            this.inPast = inPast;
        }

        public boolean isInFuture() {
            return inFuture;
        }

        public void setInFuture(boolean inFuture) {
            this.inFuture = inFuture;
        }

        public LocalDate getDate() {
            return date;
        }

        public void setDate(LocalDate date) {
            this.date = date;
        }
    }
}
