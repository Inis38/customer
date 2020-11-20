package edu.makarov.customer.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Class for transferring funds from account to account")
public class MoneyTransactionDTO {

    @ApiModelProperty(notes = "Account id to which you want to transfer money",
            name = "id", required = true, value = "22")
    private long increaseId;

    @ApiModelProperty(notes = "Account id from which you want to transfer money",
            name = "id", required = true, value = "12")
    private long reduceId;

    @ApiModelProperty(notes = "Transfer amount", name = "balance", required = true, value = "1000.00")
    private BigDecimal sum;
}
