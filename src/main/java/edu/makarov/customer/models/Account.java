package edu.makarov.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "Account")
@ApiModel(description = "Account Model")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id of the Account", name = "id", required = true, value = "12")
    private long id;

    @Column(name = "account_number")
    @ApiModelProperty(notes = "Number of the Account", name = "accountNumber", required = true, value = "456333999929992000")
    private String accountNumber;

    @Column(name = "balance", columnDefinition = "decimal(11, 2)")
    @ApiModelProperty(notes = "Balance of the Account", name = "balance", required = true, value = "1000.00")
    private BigDecimal balance;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Customer customer;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "account", fetch = FetchType.LAZY)
    private Collection<Card> cards;

}
