package cdewithnidhi.inventory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MySQLContainer;

import io.restassured.RestAssured;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InventoryServiceApplicationTests {

	@ServiceConnection
	static MySQLContainer mySQLContainer = new MySQLContainer("mysql:8.3.0");
	
	@LocalServerPort
	private Integer port;
	
	@BeforeEach
	void setUp() {
		RestAssured.baseURI = "http://localhost";
		RestAssured.port = port;
	}
	static {
		mySQLContainer.start();
	}
	@Test
	void shouldReadInventory() {
		var response = RestAssured.given()
				.queryParam("skuCode", "iphone_15")
				.queryParam("quantity", 1)
				.when().get("/api/inventory")
				.then()
				.log().all()
				.statusCode(200)
				.extract().as(Boolean.class);
		assertTrue(response);
		
		var negativeResponse = RestAssured.given()
				.queryParam("skuCode", "iphone_15")
				.queryParam("quantity", 1000)
				.when().get("/api/inventory")
				.then()
				.log().all()
				.statusCode(200)
				.extract().as(Boolean.class);
		assertFalse(negativeResponse);
				
	}

}
