package com.logicea.cards.card;

import com.logicea.cards.exceptions.ResourceNotFoundException;
import com.logicea.cards.user.Role;
import com.logicea.cards.user.User;
import com.logicea.cards.user.UserRepository;
import com.logicea.cards.utils.NativeFunctions;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author Alex Kiburu
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService{
    private final CardRepository cardRepository;
    private final UserRepository userRepository;
    private final NativeFunctions nativeFunctions;

    @Override
    public ResponseEntity<?> getCards(
            String startDate,
            String endDate,
            String search,
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String sortDirection,
            HttpServletRequest request) {
        log.info("/cards startDate={}, endDate={}, search={}, pageNumber={}, pageSize={}, sortBy={}, sortDirection={}",
                startDate, endDate, search, pageNumber, pageSize, sortBy, sortDirection);
        Map<String, Object> responseMap = new HashMap<>();

        // check if either date has value
        if((null != startDate && !startDate.isEmpty() && null == endDate )
                || (null == startDate && null != endDate && !endDate.isEmpty())){
            responseMap.put("status", "01");
            responseMap.put("message", "Should have both startDate and endDate");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }

        // check if start is valid if present
        if(null != startDate && !startDate.isEmpty()){
            Boolean isStartDateValid = nativeFunctions.isDateFormatValid(startDate, "yyyy-MM-dd");
            if(!isStartDateValid){
                responseMap.put("status", "01");
                responseMap.put("message", "Incorrect start date format. Format is yyyy-MM-dd");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
        }

        // check if start is valid if present
        if(null != endDate && !endDate.isEmpty()){
            Boolean isEndDateValid = nativeFunctions.isDateFormatValid(endDate, "yyyy-MM-dd");
            if(!isEndDateValid){
                responseMap.put("status", "01");
                responseMap.put("message", "Incorrect end date format. Format is yyyy-MM-dd");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize,
                Sort.by(
                        sortDirection.toLowerCase().trim().contains("asc") ?
                                Sort.Direction.ASC :
                                Sort.Direction.DESC,
                        sortBy));
        String formattedEndDate = nativeFunctions.formatEndDate(endDate);
        Page<Card> pagedResult;

        log.info("dates selected="+startDate +", endDate="+endDate);

        String ownerEmail = (String) request.getSession().getAttribute("email");
        ownerEmail = "%".concat(ownerEmail.toLowerCase()).concat("%");
        Optional<User> ownerOptional = userRepository.findByEmail(ownerEmail);
        String role = ownerOptional.get().getRole().toString().toUpperCase();

        if(null != startDate && (null != search && !search.isEmpty())){
            // apply both dates and search filters
            pagedResult = cardRepository.findByDateAndSearch(
                    startDate,
                    formattedEndDate,
                    search.toLowerCase(),
                    pageable, role, ownerEmail);
        }else if(null != startDate){
            // apply date filters only
            pagedResult = cardRepository.findByDate(startDate, formattedEndDate, pageable, role, ownerEmail);
        }else if(null != search && !search.isEmpty()){
            // apply search only
            pagedResult = cardRepository.findBySearch(search.toLowerCase(), pageable, role, ownerEmail);
        }else{
            // default without filters
            pagedResult = cardRepository.findAll(pageable, role, ownerEmail);
        }

        List<Card> cards = pagedResult.getContent();
        Map<String, Object> resultsMap = new HashMap<>();

        Map<String, Object> pageProfileMap = new HashMap<>();
        pageProfileMap.put("pageNumber", pagedResult.getNumber());
        pageProfileMap.put("pageSize", pagedResult.getSize());
        pageProfileMap.put("totalPages", pagedResult.getTotalPages());
        pageProfileMap.put("totalElements", pagedResult.getTotalElements());

        resultsMap.put("pageProfile", pageProfileMap);
        resultsMap.put("results", cards);
        responseMap.put("data", resultsMap);

        return ResponseEntity.ok(responseMap);
    }

    @Override
    public ResponseEntity<?> getCardById(Long id){
        Card card = cardRepository.findById(id).orElseThrow( ()->{
                    return new ResourceNotFoundException("Card id [" + id + "] not found ");
            }
        );

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("status", "00");
        responseMap.put("data", card);

        return ResponseEntity.ok(responseMap);
    }

    @Override
    public ResponseEntity<?> addCard(CardRequest cardRequest, HttpServletRequest request){
        // get owner from session and attach as owner to card
        Map<String, Object> responseMap = new HashMap<>();
        // validate if name is present
        if( null == cardRequest.getName() && cardRequest.getName().isEmpty() ){
            responseMap.put("status", "01");
            responseMap.put("message", "Name is mandatory");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        // check if color has been provided
        if( null != cardRequest.getColor() && !cardRequest.getName().isEmpty() ){
            boolean isValidColorCode = nativeFunctions.isColorCodeValid(cardRequest.getColor());
            if(!isValidColorCode){
                responseMap.put("status", "01");
                responseMap.put("message", "Invalid color code");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
        }

        String ownerEmail = (String) request.getSession().getAttribute("email");

        // insert card
        Card newCard = Card.builder()
                .name(cardRequest.getName())
                .color(cardRequest.getColor())
                .description(cardRequest.getDescription())
                .status(StatusEnum.TO_DO.label)
                .owner(ownerEmail)
                .createdOn(new Date())
                .updatedOn(new Date())
                .build();
        Card savedCard = cardRepository.save(newCard);

        responseMap.put("status", "00");
        responseMap.put("message", "Card created successfully");
        responseMap.put("id", savedCard.getId());

        return ResponseEntity.ok(responseMap);
    }

    @Override
    public ResponseEntity<?> updateCard(Long id, CardRequest cardRequest){
        Card card = cardRepository.findById(id).orElseThrow( ()->{
                    return new ResourceNotFoundException("Card id [" + id + "] not found ");
                }
        );

        Map<String, Object> responseMap = new HashMap<>();
        // validate if name is present
        if( null == cardRequest.getName() && cardRequest.getName().isEmpty() ){
            responseMap.put("status", "01");
            responseMap.put("message", "Name is mandatory");
            return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
        }
        // check if color has been provided
        if( null != cardRequest.getColor() && !cardRequest.getColor().isEmpty() ){
            boolean isValidColorCode = nativeFunctions.isColorCodeValid(cardRequest.getColor());
            if(!isValidColorCode){
                responseMap.put("status", "01");
                responseMap.put("message", "Invalid color code");
                return new ResponseEntity<>(responseMap, HttpStatus.BAD_REQUEST);
            }
        }

        card.setName(cardRequest.getName());
        card.setColor(cardRequest.getColor());
        card.setDescription(cardRequest.getDescription());
        if(null != cardRequest.getStatus())
            card.setStatus(cardRequest.getStatus().label);
        card.setUpdatedOn(new Date());
        cardRepository.save(card);

        responseMap.put("status", "00");
        responseMap.put("message", "Card updated successfully");

        return ResponseEntity.ok(responseMap);
    }

    @Override
    public ResponseEntity<?> deleteCard(Long id){
        cardRepository.findById(id).orElseThrow( ()->{
                    return new ResourceNotFoundException("Card id [" + id + "] not found ");
                }
        );

        Map<String, Object> responseMap = new HashMap<>();

        cardRepository.deleteById(id);
        boolean cardStillExists = cardRepository.existsById(id);
        if(cardStillExists){
            responseMap.put("status", "01");
            responseMap.put("message", "Card not deleted");
            return ResponseEntity.ok(responseMap);

        }

        responseMap.put("status", "00");
        responseMap.put("message", "Card deleted successfully");
        return ResponseEntity.ok(responseMap);
    }
}
