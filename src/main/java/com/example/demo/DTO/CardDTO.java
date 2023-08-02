package com.example.demo.DTO;

import com.example.demo.Enum.CardType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class CardDTO {

    @Id
    private String id;

    private String cardHolderName;


    private String cardNumber;


    private String securityCode;

    private CardType cardType;

    private Date expirationDate;

    private String cardName;

    private String masterPassword;

    @Field("isSecure")
    @JsonProperty("isSecure")
    private boolean isSecure;
}
