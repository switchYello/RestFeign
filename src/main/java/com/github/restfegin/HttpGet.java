package com.github.restfegin;

import feign.Request;
import feign.RequestTemplate;
import feign.Response;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * hcy 2020/5/24
 */
class HttpGet extends AbstractHttpMethod {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(HttpGet.class);
    private URI uri;
    private RestFeign restFeign;

    HttpGet(String uri, RestFeign restFeign) {
        this.uri = URI.create(uri);
        this.restFeign = restFeign;
    }

    @Override
    protected Response doExecutor() {
        return restFeign.executor(uri, this);
    }

    @Override
    public void encodeRequestBody(RequestTemplate template) {
        //处理请求方式
        template.method(Request.HttpMethod.GET);
        //处理heads
        template.headers(this.heads);
        //处理参数
        template.queries(this.params);
    }
}
