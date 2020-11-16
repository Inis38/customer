package edu.makarov.customer.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "Customer")
@Getter
@Setter
@ApiModel(description = "Customer Model")
@ToString(of = { "id", "fullName", "documentNumber"})
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(notes = "Id of the Customer", name = "id", required = true, value = "12")
    private long id;

    @Column(name = "full_name")
    @ApiModelProperty(notes = "Full name of the Customer", name = "fullName", required = true, value = "Иванов Иван Иванович")
    private String fullName;

    @Column(name = "document_number")
    @ApiModelProperty(notes = "Document number of the Customer", name = "documentNumber", required = true, value = "123 456789")
    private String documentNumber;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer", fetch = FetchType.LAZY)
    private Collection<Account> accounts = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "customer_subscription",
            joinColumns = @JoinColumn(name = "customer_id"),
            inverseJoinColumns = @JoinColumn(name = "subscription_id"))
    private Set<Subscription> subscriptions;
}
