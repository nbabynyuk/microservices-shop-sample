package com.nb.orders.entity;

import java.util.Collection;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Order {

  @Id
  private Long id;

  private Collection<OrderItem> items;

  private OrderState orderState;

  private UserDetails userDetails;

}
