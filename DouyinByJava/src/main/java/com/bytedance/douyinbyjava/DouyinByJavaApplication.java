package com.bytedance.douyinbyjava;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableCaching
@ServletComponentScan
@EnableTransactionManagement
public class DouyinByJavaApplication {

    public static void main(String[] args) {
        SpringApplication.run(DouyinByJavaApplication.class, args);
    }

}
