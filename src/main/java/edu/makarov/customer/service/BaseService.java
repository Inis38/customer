package edu.makarov.customer.service;

import java.util.List;

public interface BaseService<T> {

    List<T> findAll();

    //T findById(long id);

    T create(T model);

//    T update(long id, T model);

    boolean delete(long id);
}
