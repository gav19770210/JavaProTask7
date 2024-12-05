package ru.gav19770210.javapro.task05.controllers;

import lombok.Getter;

import java.util.Date;

@Getter
public class ErrorMessage {
    private final Date timestamp;
    private final String message;
    private final String description;

    public ErrorMessage(String message, String description) {
        this.timestamp = new Date();
        this.message = message;
        this.description = description;
    }
}
