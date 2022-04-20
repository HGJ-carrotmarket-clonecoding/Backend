package com.marketkurly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
public class MarketkurlyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketkurlyApplication.class, args);
    }

    @PostConstruct // 초기화 작업을 할 메소드에 적용, WAS(DB 조회나 다양한 로직 처리를 요구하는 동적인 컨텐츠를 제공하기 위해 만들어진 Application Server)가 띄워질때 실행.
    public void before() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
