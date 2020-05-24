package com.github.restfegin;

import feign.*;

import java.net.URI;
import java.util.Collection;
import java.util.Map;

/**
 * 通用请求接口，Fegin会根据此类生成代理类
 */
interface ClientInterface {

    @RequestLine("GET")
    Response get(
            URI uri,
            @QueryMap Map<String, Collection<String>> params,
            @HeaderMap Map<String, Collection<String>> heads
    );

    @Headers("Content-Type: application/x-www-form-urlencoded")
    @RequestLine("POST")
    Response post(
            URI uri,
            HttpMethod request,
            @HeaderMap Map<String, Collection<String>> heads
    );
}
