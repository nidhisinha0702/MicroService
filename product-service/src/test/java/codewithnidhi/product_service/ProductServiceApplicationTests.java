package codewithnidhi.product_service;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductServiceApplicationTests {

	@ServiceConnection
	static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:7.0.5");
	
	@LocalServerPort
	private Integer port;
	
	@BeforeEach
	void setup(){
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	
	static {
		mongoDBContainer.start();
	}
	
	
	@DynamicPropertySource
	static void setProperties(DynamicPropertyRegistry dymDynamicPropertyRegistry) {
		dymDynamicPropertyRegistry.add("spring.data.mongodb.uri",mongoDBContainer::getReplicaSetUrl);
	}
	
	@Test
	void shouldCreateProduct() {
		String requestBody = """
				{
				"name":"iphone 15",
				"description":"iphone is a asmartphone from Apple",
				"price": 1000
				}
				""";
		RestAssured.given()
		.contentType("application/json")
		.body(requestBody)
		.when()
		.post("/api/product")
		.then()
		.statusCode(201)
		.body("id", Matchers.notNullValue())
		.body("name", Matchers.equalTo("iphone 15"))
		.body("description", Matchers.equalTo("iphone is a asmartphone from Apple"));
		
	}


}
