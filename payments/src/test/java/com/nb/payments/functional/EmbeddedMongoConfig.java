//package com.nb.payments.functional;
//
//import com.mongodb.reactivestreams.client.MongoClient;
//import com.mongodb.reactivestreams.client.MongoClients;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.DependsOn;
//import org.springframework.core.env.Environment;
//import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
//
//@Configuration
//public class EmbeddedMongoConfig extends AbstractReactiveMongoConfiguration {
//
//  private final Environment environment;
//
//  public EmbeddedMongoConfig(Environment environment) {
//    this.environment = environment;
//  }
//
//  @Bean
//  @DependsOn("embeddedMongoServer")
//  @Override
//  public MongoClient reactiveMongoClient() {
//    int port = environment.getProperty("local.mongo.port", Integer.class);
//    return MongoClients.create(String.format("mongodb://localhost:%d", port));
//  }
//
//  @Override
//  protected String getDatabaseName() {
//    return "payments";
//  }
//}
