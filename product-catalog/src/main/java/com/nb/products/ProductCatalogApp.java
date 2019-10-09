package com.nb.products;

import com.nb.products.entity.Product;
import com.nb.products.repository.ProductRepository;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ProductCatalogApp implements CommandLineRunner  {

  @Autowired
  private ProductRepository productRepository;

  public static void main(String[] args) {
    SpringApplication.run(ProductCatalogApp.class, args);
  }

  @Override
  public void run(String... args) throws Exception {
    if (args != null && args.length > 0 ) {
      if (args[0].equalsIgnoreCase("generate-dummy-data") ) {
        Utils.generateDummyProducts().forEach( p -> productRepository.insert(p));
      }
    }
  }
}
