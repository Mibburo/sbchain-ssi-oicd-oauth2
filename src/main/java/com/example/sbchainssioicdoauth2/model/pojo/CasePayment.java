package com.example.sbchainssioicdoauth2.model.pojo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class CasePayment {

    private LocalDateTime paymentDate;
    private BigDecimal payment;
    private State state;
    
}