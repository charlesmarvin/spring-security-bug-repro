package test;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class App {

  public static void main(String[] args) {
    SpringApplication.run(App.class, args);
  }

  @GetMapping("/v1/test")
  @PreAuthorize("hasRole('ADMIN')")
  public Mono<String> getTest() {
    return Mono.just("test");
  }
}
