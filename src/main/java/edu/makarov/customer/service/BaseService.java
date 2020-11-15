package edu.makarov.customer.service;

import edu.makarov.customer.models.Customer;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {

    List<T> findAll();

    Optional<T> findById(long id);

    T create(T model);

    Optional<T> update(long id, T model);

    boolean delete(long id);
}
