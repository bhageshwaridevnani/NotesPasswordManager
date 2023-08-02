package com.example.demo.Controller;

import com.example.demo.DTO.CardDTO;
import com.example.demo.DTO.IdDTO;
import com.example.demo.DTO.ListDTO;
import com.example.demo.Service.CardService;
import com.example.demo.Util.Constant;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@Api(tags = "Card API")
public class CardController extends BaseController{

    @Autowired
    private CardService cardService;

    @PostMapping("/card")
    @CircuitBreaker(name = "cardManage-service",fallbackMethod = "cardServiceFallback")
    @ApiOperation(value = "Add Pricing Period API",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> addCard(HttpServletRequest request,@Validated @RequestBody CardDTO cardDTO) {
        return okSuccessResponse(cardService.addCard(request, cardDTO), "Card added successfully");
    }
    public ResponseEntity<?> cardServiceFallback(HttpServletRequest request,@Validated @RequestBody CardDTO cardDTO,Exception e) {
        return okSuccessResponse("Card service is down");
    }

    @PutMapping("/card")
    @ApiOperation(value = "Edit card detail api",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> editCard(HttpServletRequest request,@Validated @RequestBody CardDTO cardDTO) {
        return okSuccessResponse(cardService.editCard(request,cardDTO), "Card edited Successfully");
    }

    @DeleteMapping("/card")
    @ApiOperation(value = "Delete card api",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> deleteCard(HttpServletRequest request,@Validated @RequestBody IdDTO idDTO) throws Exception {
        cardService.deleteCard(request,idDTO);
        return okSuccessResponse("Card deleted successfully.");
    }

    @PostMapping("/cardList")
    @ApiOperation(value = "List Card api",
            response = ResponseEntity.class,
            produces = "application/json",
            authorizations = {@Authorization(value = Constant.API_KEY)})
    public ResponseEntity<?> listCard(HttpServletRequest request,@Validated @RequestBody ListDTO listDTO){
        return okSuccessResponse(cardService.listCard(request,listDTO),"Card list get successfully.");
    }
}
