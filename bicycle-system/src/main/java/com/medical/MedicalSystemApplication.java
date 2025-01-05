package com.medical;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * springboot项目启动器
 * */
@SpringBootApplication
@EnableTransactionManagement
@MapperScan(value = "com.medical.mapper.*")
public class MedicalSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MedicalSystemApplication.class, args);
    }
}
