package com.nb.products;

import com.nb.products.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductCatalogApp implements ApplicationRunner {

  @Autowired
  private ProductRepository productRepository;

  public static void main(String[] args) {
    SpringApplication.run(ProductCatalogApp.class, args);
  }

  @Override
  public void run(ApplicationArguments args) throws Exception {
    if(args.getOptionNames().contains("generate-dummy-data")){
      Utils.generateDummyProducts().forEach( p -> productRepository.insert(p));
    }
  }
}
