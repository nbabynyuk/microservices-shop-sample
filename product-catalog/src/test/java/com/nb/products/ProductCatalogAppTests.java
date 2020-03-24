package com.nb.products;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nb.products.entity.Product;
import java.math.BigDecimal;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductCatalogAppTests {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private ObjectMapper mapper = new ObjectMapper();

  @Before
  public void init() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void scenario_one() throws Exception {

    final String productName = "Xiaomi Mi Notebook";

    Product p = new Product();
    p.setName(productName);
    p.setDescription("This is one of the best laptops currently available on the market");
    p.setMediaFiles(Arrays.asList(
        "product1-media1-picture1",
        "product2, media2-picture-2",
        "picture-3-product-1"
    ));
    p.setCurrentPrice(new BigDecimal("15.43"));

    String s = mapper.writeValueAsString(p);
    mockMvc.perform(
        post("/api/products")
        .header("Content-Type", "application/json")
        .content(s)
    ).andExpect(status().isCreated());

    MvcResult result = mockMvc.perform(
        get("/api/products")
        .header("Content-Type", "application/json")
    ).andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.content").isArray())
        .andExpect(jsonPath("$.content[0].name").value(productName))
        .andExpect( jsonPath("$.totalPages").value(1))
        .andExpect( jsonPath("$.size").value(20))
        .andReturn();
  }
}
