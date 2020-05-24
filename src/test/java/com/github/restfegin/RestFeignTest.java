package com.github.restfegin;

import org.junit.Test;


public class RestFeignTest {

    @Test
    public void get() {
        String executor = new RestFeign().get("https://www.baidu.com")
                .addParam("name", "123")
                .executor();
    }

    @Test
    public void post() {
        String executor = new RestFeign().post("https://fanyi.baidu.com/langdetect")
                .addParam("query", "hello")
                .executor();
    }
}