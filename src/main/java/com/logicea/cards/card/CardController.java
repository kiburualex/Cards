package com.logicea.cards.card;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author Alex Kiburu
 */
@RestController
@RequestMapping("/api/v1/cards")
@RequiredArgsConstructor
public class CardController {

    private final CardService cardService;

    @GetMapping
    public ResponseEntity<?> list(
            @RequestParam(required=false) String startDate,
            @RequestParam(required=false) String endDate,
            @RequestParam(required=false) String search,
            @RequestParam(defaultValue="0", required=false) Integer pageNumber,
            @RequestParam(defaultValue="10", required=false) Integer pageSize,
            @RequestParam(defaultValue = "id", required=false) String sortBy,
            @RequestParam(defaultValue = "desc", required=false) String sortDirection,
            @Parameter(hidden = true) HttpServletRequest request) {

        return cardService.getCards(startDate, endDate, search, pageNumber, pageSize, sortBy, sortDirection, request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCardById(@PathVariable Long id){
        return cardService.getCardById(id);
    }

    @PostMapping
    public ResponseEntity<?> addCard(@RequestBody CardRequest cardRequest, HttpServletRequest request){
        return cardService.addCard(cardRequest, request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCardById(@PathVariable Long id, @RequestBody CardRequest cardRequest){
        return cardService.updateCard(id, cardRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCardById(@PathVariable Long id){
        return cardService.deleteCard(id);
    }


}
