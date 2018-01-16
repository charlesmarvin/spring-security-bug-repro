package test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

  @Autowired
  private SecurityContextRepository securityContextRepository;

  @Bean
  public SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) {
    return http.csrf().disable()
        .httpBasic().disable()
        .securityContextRepository(securityContextRepository)
        .authorizeExchange()
        .pathMatchers("/v1/test").hasRole("ADMIN")
        .anyExchange().authenticated()
        .and()
        .build();
  }

  @Bean
  MapReactiveUserDetailsService userDetailsRepository() {
    UserDetails admin = User
        .withDefaultPasswordEncoder()
        .username("admin")
        .password("admin")
        .roles("USER", "ADMIN")
        .build();
    return new MapReactiveUserDetailsService(admin);
  }

  @Component
  class SecurityContextRepository implements ServerSecurityContextRepository {

    public Mono<SecurityContext> load(ServerWebExchange exchange) {
      return Mono.just(new SecurityContextImpl());
    }

    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
      return Mono.empty();
    }
  }
}