package com.HealthTrioApp;

import com.Models.Hospital;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;


@SpringBootApplication
@RestController
public class HealthTrioAppApplication {

	private static final Logger logger = LoggerFactory.getLogger(HealthTrioAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(HealthTrioAppApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	@Bean
	@RequestMapping("/")
	public String hospitals(RestTemplate restTemplate) {

			Hospital[] hospitals = restTemplate.getForObject(
					"https://www.healthit.gov/data/open-api?source=Meaningful-Use-Acceleration-Scorecard.csv", Hospital[].class);
			List<Hospital> hospitals2014 = Arrays.stream(hospitals)
					.filter(hospital -> StringUtils.isNotBlank(hospital.period()) && !hospital.region().equals("National") && hospital.period().equals("2014"))
					.sorted(Comparator.comparing(Hospital::region).reversed())
					.collect(Collectors.toList());

			String finalHospitals = "";
			for (Hospital h : hospitals2014) {
				logger.info(h.region() + " (" + h.region_code() + ") pct_hospitals_mu : " + (Double.parseDouble(h.pct_hospitals_mu()) * 100) + "% in " + h.period());
				finalHospitals +=h.region() + " (" + h.region_code() + ") pct_hospitals_mu : " + (Double.parseDouble(h.pct_hospitals_mu()) * 100) + "% in " + h.period() + "<br>";
			}
		return finalHospitals;
	}
}
