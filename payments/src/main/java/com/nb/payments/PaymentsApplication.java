package com.nb.payments;

import com.nb.payments.functional.PaymentHandlerFunctions;
import com.nb.payments.repo.PaymentRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.validation.Validator;

@SpringBootApplication
public class PaymentsApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentsApplication.class, args);
  }

  @Bean
  public PaymentHandlerFunctions paymentsHandlerFunctions(PaymentRepo paymentRepo) {
    return new PaymentHandlerFunctions(paymentRepo, localValidatorFactoryBean());
  }

  @Bean
  public Validator localValidatorFactoryBean() {
    return new LocalValidatorFactoryBean();
  }
}

