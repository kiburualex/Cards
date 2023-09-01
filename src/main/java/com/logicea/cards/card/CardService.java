package com.logicea.cards.card;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

/**
 * @author Alex Kiburu
 */
public interface CardService {

    ResponseEntity<?> getCards(
            String startDate,
            String endDate,
            String search,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection,
            HttpServletRequest request) ;

    ResponseEntity<?> getCardById(Long id);

    ResponseEntity<?> addCard(CardRequest cardRequest, HttpServletRequest request);

    ResponseEntity<?> updateCard(Long id, CardRequest cardRequest);

    ResponseEntity<?> deleteCard(Long id);
}
