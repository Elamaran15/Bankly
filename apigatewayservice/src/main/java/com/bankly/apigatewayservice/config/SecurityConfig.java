package com.bankly.apigatewayservice.config;


import org.springframework.context.annotation.Configuration;


@Configuration
//@EnableWebFluxSecurity
public class SecurityConfig {
//
//    private final JwtUtil jwtUtil;
//
//    public SecurityConfig(JwtUtil jwtUtil) {
//        this.jwtUtil = jwtUtil;
//    }
//
//    @Bean
//    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
//        http
//                .csrf(ServerHttpSecurity.CsrfSpec::disable)
//                .authorizeExchange(exchanges -> exchanges
//                        .pathMatchers("/api/users/register", "/api/users/login").permitAll()
//                        .anyExchange().authenticated()
//                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        // Use the default BearerTokenAuthenticationConverter to extract the token
//                        // from the Authorization header and wrap it in a BearerTokenAuthenticationToken.
//                        // This is implicitly done if not specified, but explicit for clarity.
//                        .bearerTokenConverter(new ServerBearerTokenAuthenticationConverter())
//                        // Provide our custom ReactiveAuthenticationManager to validate the extracted token
//                        .authenticationManager(jwtAuthenticationManager())
//                );
//        return http.build();
//    }
//
//    @Bean
//    public ReactiveAuthenticationManager jwtAuthenticationManager() {
//        return authentication -> {
//            // The authentication object passed here will be a BearerTokenAuthenticationToken
//            // because of the oauth2ResourceServer configuration.
//            if (!(authentication instanceof BearerTokenAuthenticationToken)) {
//                System.err.println("API Gateway SecurityConfig: Authentication is not a BearerTokenAuthenticationToken.");
//                return Mono.empty();
//            }
//
//            String authToken = ((BearerTokenAuthenticationToken) authentication).getToken();
//
//            String username;
//            try {
//                jwtUtil.validateToken(authToken); // This will throw if invalid or expired
//                Claims claims = jwtUtil.extractAllClaims(authToken);
//                username = claims.getSubject(); // Assuming subject is the username
//
//                return Mono.just(new UsernamePasswordAuthenticationToken(
//                        username,
//                        null, // Credentials are not needed after validation
//                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // Default role
//                ));
//
//            } catch (ExpiredJwtException e) {
//                System.err.println("API Gateway SecurityConfig: JWT token is expired: " + e.getMessage());
//                return Mono.empty(); // Authentication failed
//            } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException e) {
//                System.err.println("API Gateway SecurityConfig: Invalid JWT token (signature/format/claims): " + e.getMessage());
//                return Mono.empty(); // Authentication failed
//            } catch (Exception e) {
//                System.err.println("API Gateway SecurityConfig: Unexpected error during JWT validation: " + e.getMessage());
//                e.printStackTrace();
//                return Mono.empty(); // Authentication failed
//            }
//        };
//    }
//
//    @Bean
//    public CorsWebFilter corsWebFilter() {
//        CorsConfiguration corsConfig = new CorsConfiguration();
//        corsConfig.setAllowedOrigins(List.of("*")); // Allow all origins for development
//        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfig.setAllowedHeaders(List.of("*")); // Allow all headers
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfig);
//
//        return new CorsWebFilter(source);
//    }
}