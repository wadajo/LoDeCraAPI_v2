package com.lodecra.apiV1;

import com.mongodb.ConnectionString;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoClientSettingsBuilderCustomizer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiV2Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiV2Application.class, args);
	}

	@Bean
	public MongoClientSettingsBuilderCustomizer customizer(@Value("${custom.uri}") String uri) {
		ConnectionString connection = new ConnectionString(uri);
		return settings -> settings.applyConnectionString(connection);
	}
}
