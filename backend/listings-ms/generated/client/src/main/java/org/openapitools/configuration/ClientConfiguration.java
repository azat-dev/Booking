package org.openapitools.configuration;

import feign.auth.BasicAuthRequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
public class ClientConfiguration {

  @Value("${listings.security.bearerAuth.username:}")
  private String bearerAuthUsername;

  @Value("${listings.security.bearerAuth.password:}")
  private String bearerAuthPassword;

  @Bean
  @ConditionalOnProperty(name = "listings.security.bearerAuth.username")
  public BasicAuthRequestInterceptor bearerAuthRequestInterceptor() {
    return new BasicAuthRequestInterceptor(this.bearerAuthUsername, this.bearerAuthPassword);
  }

}
