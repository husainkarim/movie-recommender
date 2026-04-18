package backend.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
public class Routes {
    @Value("${user.service.url}")
	private String userServiceUrl;
    @Value("${movie.service.url}")
    private String movieServiceUrl;
    @Value("${rating.service.url}")
    private String ratingServiceUrl;
    @Value("${recommendation.service.url}")
    private String recommendationServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> userServiceRoute() {
        return GatewayRouterFunctions.route("user-service")
            .route(RequestPredicates.path("/api/users/**"), HandlerFunctions.http(userServiceUrl))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> movieServiceRoute() {
        return GatewayRouterFunctions.route("movie-service")
            .route(RequestPredicates.path("/api/movies/**"), HandlerFunctions.http(movieServiceUrl))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> ratingServiceRoute() {
        return GatewayRouterFunctions.route("rating-service")
            .route(RequestPredicates.path("/api/ratings/**"), HandlerFunctions.http(ratingServiceUrl))
            .build();
    }

    @Bean
    public RouterFunction<ServerResponse> recommendationServiceRoute() {
        return GatewayRouterFunctions.route("recommendation-service")
            .route(RequestPredicates.path("/api/recommendations/**"), HandlerFunctions.http(recommendationServiceUrl))
            .build();
    }
}
