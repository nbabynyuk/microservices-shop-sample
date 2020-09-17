package com.nb;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.io.IOException;
import java.time.Duration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
class ApiGatewayAppTests {

    private static final Logger LOG = LoggerFactory.getLogger(ApiGatewayAppTests.class);

    private static MockWebServer usersApp;
    private static MockWebServer ordersApp;
    private static MockWebServer productCatalog;

    @LocalServerPort
    private int gatewayPort;
    private WebTestClient webClient;

    @BeforeAll
    public static   void setupRemoteSystems() throws Exception {
        usersApp = new MockWebServer();
        ordersApp = new MockWebServer();
        productCatalog = new MockWebServer();
        startServerAndExposeVariable(usersApp, "USERS_SERVICE");
        startServerAndExposeVariable(ordersApp, "ORDERS_SERVICE");
        startServerAndExposeVariable(productCatalog, "PRODUCT_CATALOG_SERVICE");
    }

    private static void startServerAndExposeVariable(MockWebServer server, 
                                                     String envVariableName) throws IOException {
        server.start();
        server.enqueue(new MockResponse()
            .addHeader("Content-Type", "application/json")
            .setBody("{}"));
        String serviceUrl = "http://localhost:" + server.getPort() + "/";
        LOG.info("user {} is exposed at url: {}",  envVariableName, serviceUrl);
        System.getProperties().put(envVariableName, serviceUrl);
    }

    @AfterAll
    public static   void shutdownRemoteSystems()  {
        shutdownGracefully(usersApp);
        shutdownGracefully(ordersApp);
        shutdownGracefully(productCatalog);
    }
    
    private static void shutdownGracefully(MockWebServer webServer){
        try {
            if (null != webServer){
                webServer.shutdown();
            }
        } catch (IOException e) {
            LOG.error("error occurred during shutdown mock server, reason: " + e.getMessage());
        }
    }
    
    @BeforeEach
    public void setup() {
        String baseUri = "http://localhost:" + gatewayPort;
        this.webClient = WebTestClient.bindToServer()
            .responseTimeout(Duration.ofSeconds(10)).baseUrl(baseUri).build();
    }
    
    @Test
    void verifyUserAppRouting() {
        webClient.get().uri("/api/users")
            .exchange()
            .expectStatus()
            .isOk();

        webClient.get().uri("/api/orders")
            .exchange()
            .expectStatus()
            .isOk();

        webClient.get().uri("/api/products")
            .exchange()
            .expectStatus()
            .isOk();
        
    }
}
