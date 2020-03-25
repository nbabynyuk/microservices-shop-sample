package com.nb.products.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document
public class ProductEntity extends Product {

  public ProductEntity(){}

  public ProductEntity(Product dto){
    this.setId(dto.getId());
    this.setName(dto.getName());
    this.setDescription(dto.getDescription());
    List<String> media = new ArrayList<>(dto.getMediaFiles());
    this.setMediaFiles(media);
    this.setCurrentPrice(dto.getCurrentPrice());
  }

  @Override
  @Id
  public String getId(){
    return super.getId();
  }
}
