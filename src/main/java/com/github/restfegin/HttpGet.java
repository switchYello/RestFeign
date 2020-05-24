package com.github.restfegin;

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
    private ClientInterface clientInterface;

    public HttpGet(String uri, ClientInterface clientInterface) {
        this.uri = URI.create(uri);
        this.clientInterface = clientInterface;
    }

    @Override
    Response doExecutor() {
        return clientInterface.get(uri, this.params, this.heads);
    }

    @Override
    public void encodeRequestBody(RequestTemplate template) {
        throw new IllegalStateException("Get Method Should Don't Call [com.restfegin.HttpGet.encode]");
    }
}
