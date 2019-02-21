package com.nb.orders.repo;

import com.nb.orders.entity.Order;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface OrdersRepository extends ReactiveMongoRepository<Order, String> {

}
