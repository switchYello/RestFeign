package com.github.restfegin;

import feign.Logger;
import org.junit.Test;


public class RestFeignTest {

    @Test
    public void get() {
        RestFeign restFeign = RestFeign.factory()
                .logLevel(Logger.Level.FULL)
                .build();
        String executor = restFeign.get("https://www.baidu.com")
                .addParam("name", "123")
                .executor();
    }

    @Test
    public void post() {
        RestFeign restFeign = RestFeign.factory()
                .logLevel(Logger.Level.FULL)
                .build();
        String executor = restFeign.post("https://fanyi.baidu.com/langdetect")
                .addParam("query", "hello")
                .executor();
    }


}