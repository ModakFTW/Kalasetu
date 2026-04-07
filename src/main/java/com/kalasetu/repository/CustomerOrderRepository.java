package com.kalasetu.repository;

import com.kalasetu.model.CustomerOrder;
import com.kalasetu.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByCustomerOrderByOrderDateDesc(User customer);
}
