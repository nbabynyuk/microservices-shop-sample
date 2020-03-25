package com.nb.products.controllers;

import com.nb.products.entity.ProductDTO;
import com.nb.products.entity.ProductEntity;
import com.nb.products.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  private final ProductRepository productRepository;

  public ProductController(ProductRepository productRepository) {
    this.productRepository = productRepository;
  }

  @GetMapping
  public Page<ProductDTO> productList(Pageable currentPage) {
    return productRepository
        .findAll(currentPage)
        .map(ProductDTO::new);
  }

  @PostMapping
  @ResponseStatus(value = HttpStatus.CREATED)
  public void addProduct(@Valid @RequestBody ProductDTO p) {
    productRepository.insert(new ProductEntity(p));
  }
}
