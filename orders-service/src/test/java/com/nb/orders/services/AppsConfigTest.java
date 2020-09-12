package com.nb.orders.services;

import com.nb.orders.remote.PaymentsRemoteRepository;
import com.nb.orders.remote.StockRemoteRepository;
import com.nb.orders.services.handlers.PaymentProcessingHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.Assert.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class AppsConfigTest {
    
    @Autowired
    private ApplicationContext applicationContext;
    
    @Test
    public void verifyAllBeansInitializedProperly() {
        PaymentsRemoteRepository paymentsRepo = applicationContext.getBean(PaymentsRemoteRepository.class);
        assertNotNull(paymentsRepo);
        StockRemoteRepository stockBean = applicationContext.getBean(StockRemoteRepository.class);
        assertNotNull(stockBean);
        PaymentProcessingHandler paymentsProcessor = applicationContext.getBean(PaymentProcessingHandler.class);
        assertNotNull(paymentsProcessor);
    }
}
