package com.logicea.cards.card;

/**
 * @author Alex Kiburu
 */
public enum StatusEnum {
    TO_DO("To Do"),
    IN_PROGRESS("In Progress"),
    DONE("Done");

    public final String label;

    StatusEnum(String label) {
        this.label = label;
    }
}
