package edu.makarov.customer.models.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MoneyTransactionDTO {

    private long increaseId;
    private long reduceId;
    private BigDecimal sum;
}
