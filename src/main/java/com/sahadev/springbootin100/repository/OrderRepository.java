package com.sahadev.springbootin100.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sahadev.springbootin100.entities.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long>{

}
