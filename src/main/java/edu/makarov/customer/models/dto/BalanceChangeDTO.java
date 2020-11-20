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
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Class for changing Account balance")
public class BalanceChangeDTO {

    @ApiModelProperty(notes = "Id of the Account", name = "id", required = true, value = "12")
    private long id;

    @ApiModelProperty(notes = "The amount to change the balance by. It can only be positive.",
            name = "balance",
            required = true,
            value = "1000.00")
    private BigDecimal sum;
}
