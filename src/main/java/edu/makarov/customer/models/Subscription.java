package edu.makarov.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Subscription")
@ApiModel(description = "Subscription Model")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id of the Subscription", name = "id", required = true, value = "12")
    private long id;

    @Column(name = "subscription_name")
    @ApiModelProperty(notes = "Name of the Subscription", name = "name", required = true, value = "Услуга оповещения об операциях")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "subscriptions", cascade = CascadeType.ALL)
    private List<Customer> customers;
}
