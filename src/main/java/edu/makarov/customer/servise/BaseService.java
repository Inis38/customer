package edu.makarov.customer.servise;

import edu.makarov.customer.models.Customer;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {

    List<T> findAll();

    T findById(long id);

    T create(T model);

    T update(long id, T model);

    void delete(long id);
}
