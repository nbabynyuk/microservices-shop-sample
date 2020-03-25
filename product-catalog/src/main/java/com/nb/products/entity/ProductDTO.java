package com.nb.products.entity;

import java.util.ArrayList;
import java.util.List;

public class ProductDTO extends Product {
  public ProductDTO(){}

  public ProductDTO(ProductEntity e){
    this.setId(e.getId());
    this.setName(e.getName());
    this.setDescription(e.getDescription());
    List<String> media = new ArrayList<>(e.getMediaFiles());
    this.setMediaFiles(media);
    this.setCurrentPrice(e.getCurrentPrice());
  }
}
