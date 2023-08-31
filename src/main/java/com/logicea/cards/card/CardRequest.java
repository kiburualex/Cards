package com.logicea.cards.card;

import lombok.Data;

/**
 * @author Alex Kiburu
 */
@Data
public class CardRequest {
    private String name;
    private String color;
    private String description;
    private String password;
    private StatusEnum status;
}
