package edu.makarov.customer.models.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Сlass for managing customer subscriptions")
public class SubscriptionManagementDTO {

    @ApiModelProperty(notes = "Customer id", name = "id", required = true, value = "22")
    long customerId;

    @ApiModelProperty(notes = "Subscription id", name = "id", required = true, value = "22")
    long subscriptionId;
}
