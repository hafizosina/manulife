package com.manulife.id.factory;

public interface AbstractFactory <T, U> {
    U buildDto(T entity);

    T fillEntity(U dto, T entity);
}
