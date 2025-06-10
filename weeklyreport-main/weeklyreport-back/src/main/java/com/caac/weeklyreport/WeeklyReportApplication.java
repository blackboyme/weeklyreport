package com.caac.weeklyreport;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.caac.weeklyreport.mapper")
public class WeeklyReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeeklyReportApplication.class, args);
    }

}
