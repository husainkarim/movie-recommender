package backend.movie_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import backend.movie_service.config.Neo4jConfig;

@SpringBootTest
@Import(Neo4jConfig.class)
class MovieServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
