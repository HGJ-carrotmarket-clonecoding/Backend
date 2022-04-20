package com.marketkurly;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MarketkurlyApplication {
    private static final Logger logger = LoggerFactory.getLogger(MarketkurlyApplication.class);

    public static void main(String[] args) {
        logger.info("MarketkurlyApplication Start");
        SpringApplication.run(MarketkurlyApplication.class, args);
    }

}
