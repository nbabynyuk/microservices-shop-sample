package com.nb.products.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

import com.nb.products.entity.Product;
import com.nb.products.repository.ProductRepository;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductRepository productRepository;

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @RequestMapping(method = GET)
  public Page<Product> productList(Pageable currentPage) {
    return productRepository.findAll(currentPage);
  }

  @RequestMapping(method = POST)
  @ResponseStatus(value = HttpStatus.CREATED)
  public void addProduct(@Valid @RequestBody Product p) {
    productRepository.insert(p);
  }
}
