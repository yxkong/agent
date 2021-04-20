package com.yxkong.agent.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: yxkong
 * @Date: 2021/4/20 4:22 下午
 * @version: 1.0
 */
@SpringBootApplication
@RestController
public class Starter {

    public static void main(String[] args) {
        new SpringApplication(Starter.class).run(args);
    }
    @RestController
    class EchoController {
        @RequestMapping(value = "/echo/{string}", method = RequestMethod.GET)
        public String echo(@PathVariable String string) {
            return "Hello  " + string;
        }
    }
}