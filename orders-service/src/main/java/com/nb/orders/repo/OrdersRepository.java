package com.nb.orders.repo;

import com.nb.orders.entity.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersRepository extends ReactiveMongoRepository<Order, String> {

}
