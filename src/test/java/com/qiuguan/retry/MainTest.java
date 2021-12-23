package com.qiuguan.retry;

import com.qiuguan.retry.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author created by qiuguan on 2021/12/22 17:38
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MainTest {

    @Autowired
    private UserService userService;

    @Test
    public void test(){

        this.userService.runTask(-10);
    }
}
