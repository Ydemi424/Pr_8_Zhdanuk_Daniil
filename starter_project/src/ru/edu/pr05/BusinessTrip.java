package ru.edu.pr05;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BusinessTrip {
    private final String id;
    private final String destination;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private TripStatus status;
    private String rejectionReason;

    // 6-й вариант
    private static final long MIN_DAYS_AFTER_END = 35;

    public BusinessTrip(String id, String destination, LocalDate startDate, LocalDate endDate) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("ID не может быть пустым");
        if (destination == null || destination.isBlank()) throw new IllegalArgumentException("Место не может быть пустым");
        if (startDate == null || endDate == null) throw new IllegalArgumentException("Даты не могут быть null");
        if (startDate.isAfter(endDate)) throw new IllegalArgumentException("Дата начала позже даты окончания");

        this.id = id;
        this.destination = destination;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = TripStatus.DRAFT;
    }

    public void submit() {
        if (status != TripStatus.DRAFT) {
            throw new IllegalStateException("Отправить можно только из DRAFT");
        }
        this.status = TripStatus.SUBMITTED;
    }

    public void approve() {
        if (status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Одобрить можно только из SUBMITTED");
        }
        this.status = TripStatus.APPROVED;
    }

    public void reject(String reason) {
        if (status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Отклонить можно только из SUBMITTED");
        }
        if (reason == null || reason.trim().length() < 10) {
            throw new IllegalArgumentException("Причина отклонения должна содержать минимум 10 символов");
        }
        this.status = TripStatus.REJECTED;
        this.rejectionReason = reason;
    }

    public void cancel() {
        if (status == TripStatus.COMPLETED) {
            throw new IllegalStateException("Нельзя отменить завершенную командировку");
        }
        if (status == TripStatus.REJECTED || status == TripStatus.CANCELLED) {
            throw new IllegalStateException("Заявка уже отменена или отклонена");
        }
        this.status = TripStatus.CANCELLED;
    }

    public void complete(LocalDate currentDate) {
        if (status != TripStatus.APPROVED) {
            throw new IllegalStateException("Завершить можно только из APPROVED");
        }

        long daysSinceEnd = ChronoUnit.DAYS.between(endDate, currentDate);

        //6-й вариант
        if (daysSinceEnd < MIN_DAYS_AFTER_END) {
            throw new IllegalStateException("Завершение доступно только через " + MIN_DAYS_AFTER_END + " дней после окончания");
        }

        this.status = TripStatus.COMPLETED;
    }

    public TripStatus getStatus() { return status; }
    public String getRejectionReason() { return rejectionReason; }
}