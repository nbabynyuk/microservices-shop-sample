package com.nb.payments.repo;

import com.nb.payments.entity.PaymentData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepo extends ReactiveMongoRepository<PaymentData, String> {

}
