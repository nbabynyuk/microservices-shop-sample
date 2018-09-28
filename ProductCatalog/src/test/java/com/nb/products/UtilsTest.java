package com.nb.products;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.nb.products.entity.Product;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Test;

public class UtilsTest {

  @Test
  public void test() {
    List<Product> productList = Utils.generateDummyProducts()
        .collect(Collectors.toList());
    assertEquals(4, productList.size());
    assertNotNull(productList.get(0).getName());
    assertNotNull(productList.get(0).getDescription());
    assertNotNull(productList.get(0).getCurrentPrice());
  }

}
