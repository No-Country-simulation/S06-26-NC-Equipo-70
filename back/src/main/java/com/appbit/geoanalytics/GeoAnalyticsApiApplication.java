package com.appbit.geoanalytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class GeoAnalyticsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(GeoAnalyticsApiApplication.class, args);
	}

}
