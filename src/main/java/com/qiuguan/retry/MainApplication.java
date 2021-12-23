package com.qiuguan.retry;

import com.qiuguan.retry.ann.EnableRetry;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * @author created by qiuguan on 2021/12/22 16:49
 */
@EnableRetry
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {

        SpringApplication.run(MainApplication.class, args);
    }
}
