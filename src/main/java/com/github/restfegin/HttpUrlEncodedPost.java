package com.github.restfegin;

import feign.Request;
import feign.RequestTemplate;
import feign.Response;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * hcy 2020/5/24
 */
class HttpUrlEncodedPost extends AbstractHttpMethod {

    private URI uri;
    private RestFeign restFeign;

    HttpUrlEncodedPost(String uri, RestFeign restFeign) {
        this.uri = URI.create(uri);
        this.restFeign = restFeign;
        init();
    }

    private void init() {
        addHeader("Content-Type", "application/x-www-form-urlencoded");
    }

    @Override
    Response doExecutor() {
        return restFeign.executor(uri, this);
    }

    /*
     * 处理参数的方式是将参数使用&拼接起来，并且需要url转码
     * */
    @Override
    public void encodeRequestBody(RequestTemplate template) {
        //请求方式
        template.method(Request.HttpMethod.POST);
        //处理heads
        template.headers(this.heads);
        //处理参数
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<String, Collection<String>>> iterator = this.params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Collection<String>> entity = iterator.next();
            sb.append(join(entity.getKey(), entity.getValue()));
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        template.body(sb.toString().getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
    }

    private static StringBuilder join(String field, Collection<String> values) {
        StringBuilder sb = new StringBuilder();
        field = urlEncode(field);
        for (Iterator<String> iterator = values.iterator(); iterator.hasNext(); ) {
            sb.append(field).append("=").append(urlEncode(iterator.next()));
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        return sb;
    }


}
