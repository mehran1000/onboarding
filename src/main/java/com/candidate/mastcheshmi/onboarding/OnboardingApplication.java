package com.candidate.mastcheshmi.onboarding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@EnableCaching
public class OnboardingApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnboardingApplication.class, args);
    }
}
