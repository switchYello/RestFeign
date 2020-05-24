package com.github.restfegin;

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
    private ClientInterface clientInterface;

    public HttpUrlEncodedPost(String uri, ClientInterface clientInterface) {
        this.uri = URI.create(uri);
        this.clientInterface = clientInterface;
    }

    @Override
    Response doExecutor() {
        return clientInterface.post(uri, this, this.heads);
    }

    /*
    * 处理参数的方式是将参数使用&拼接起来，并且需要url转码
    * */
    @Override
    public void encodeRequestBody(RequestTemplate template) {
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
        Iterator<String> iterator = values.iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(urlEncode(field)).append("=").append(urlEncode(iterator.next()));
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        return sb;
    }


}
