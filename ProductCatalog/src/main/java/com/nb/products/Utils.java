package com.nb.products;

import com.nb.products.entity.Product;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

  private static final String[] productNames = new String[]{
      "Xiaomi Mi Notebook Pro 15.6;Ноутбук • Класичний • 15,6 • IPS • 1920x1080 "
          + "• Intel Core i7-8550U ; 33635",
      "Apple MacBook Air 13; Ноутбук • Класичний • 13,3 • TN + film • 1400x900 • Intel Core i5-5350U"
          + ";27561",
      "Apple MacBook Pro 15\" Space Grey 2018 ; Ноутбук • Класичний • 15,4\" • IPS • 2880x1800 • "
          + "Intel Core i7-8750H • 2,2-4,1 ГГц; 68698",
      "Xiaomi Mi Gaming Laptop 15.6 ;  Ноутбук • Класичний • 15,6\" • IPS • 1920x1080 • Intel Core i7-7700HQ • 2,8-3,8 ГГц"
          + ";39247"


  };

  public static Stream<Product> generateDummyProducts() {
    final Random r = new Random();

    return Arrays.stream(productNames).map(s -> {
      String[] description = s.split(";");
      Product p = new Product();
      p.setName(description[0]);
      p.setDescription(description[1]);
      p.setCurrentPrice(new BigDecimal(description[2].trim()));
      p.setMediaFiles(Arrays.asList(
          "media-" + r.nextInt() * 10000,
          "media-" + r.nextInt() * 10000,
          "media-" + r.nextInt() * 10000
      ));
      return p;
    });
  }

}
