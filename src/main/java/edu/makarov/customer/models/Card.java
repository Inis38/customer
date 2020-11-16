package edu.makarov.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "Card")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ApiModel(description = "Card Model")
@ToString(of = { "id", "cardNumber"})
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id of the Bank Card", name = "id", required = true, value = "12")
    private long id;

    @Column(name = "card_number")
    @ApiModelProperty(notes = "Number of the Bank Card", name = "cardNumber", required = true, value = "2828 3930 7269 2098")
    private String cardNumber;

    @JsonIgnore
    @ManyToOne
    @JoinColumn
    private Account account;
}
