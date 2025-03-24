package codewithnidhi.order_service;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MySQLContainer;

import codewithnidhi.order_service.stubs.InventoryClientStub;
import io.restassured.RestAssured;
import static org.awaitility.Awaitility.await;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class OrderServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
	
	@LocalServerPort
	private Integer port;
	
	@Value("${wiremock.server.port}")
	private int wireMockPort;
	
	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
		// Ensure WireMock is ready before continuing
	    await().atMost(5, TimeUnit.SECONDS).until(() -> {
	        try {
	            new URL("http://localhost:" + wireMockPort + "/__admin/").openConnection().connect();
	            return true;
	        } catch (IOException e) {
	            return false;
	        }
	    });
	}
	
	static {
		mySQLContainer.start();
	}
	
	@Test
	void shouldSubmitOrder() {
		String submitOrderJson = """
				{
			    "skuCode":"iphone_15",
			    "price":1000,
			    "quantity":1
				}
				""";
		InventoryClientStub.stubInventoryCall("iphone_15", 1);
		
		var responseBodyString = RestAssured.given()
				.contentType("application/json")
				.body(submitOrderJson)
				.when()
				.post("/api/order")
				.then()
				.log().all()
				.statusCode(201)
				.extract()
				.body().asString();
		
		assertThat(responseBodyString, Matchers.is("Order Placed Successfully"));
	}

}
