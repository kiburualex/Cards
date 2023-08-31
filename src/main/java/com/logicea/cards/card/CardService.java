package com.logicea.cards.card;

import com.logicea.cards.exceptions.ResourceNotFoundException;
import com.logicea.cards.utils.NativeFunctions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.support.HttpRequestHandlerServlet;

import java.util.*;

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
            HttpRequestHandlerServlet request) ;

    ResponseEntity<?> getCardById(Long id);

    ResponseEntity<?> addCard(CardRequest cardRequest, HttpRequestHandlerServlet request);

    ResponseEntity<?> updateCard(Long id, CardRequest cardRequest);

    ResponseEntity<?> deleteCard(Long id);
}
