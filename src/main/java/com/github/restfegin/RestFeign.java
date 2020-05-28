package com.github.restfegin;

import feign.*;
import feign.slf4j.Slf4jLogger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class RestFeign {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(RestFeign.class);

    //根据接口生成的代理类，每个RestFegin实例生成一个代理类
    private ClientInterface clientInterface;

    public static RestFeginFactory factory() {
        return new RestFeginFactory();
    }

    private RestFeign(Client client, Logger.Level logLevel, Iterable<RequestInterceptor> interceptors) {
        initClient(client, logLevel, interceptors);
    }

    private void initClient(Client client, Logger.Level logLevel, Iterable<RequestInterceptor> interceptors) {
        Feign.Builder builder = Feign.builder()
                .client(client)
                .logLevel(logLevel)
                .requestInterceptors(interceptors)
                .logger(new Slf4jLogger())
                //参数中无法识别的（没加注解），会调用此方法处理
                .encoder((object, bodyType, template) -> {
                    if (bodyType == HttpMethod.class) {
                        HttpMethod request = (HttpMethod) object;
                        request.encodeRequestBody(template);
                    } else {
                        log.error("feign.codec.Encoder.encode: bodyType not instance HttpMethod is " + bodyType);
                    }
                });
        clientInterface = builder.target(Target.EmptyTarget.create(ClientInterface.class));
    }

    /*调用代理类，发送请求*/
    public Response executor(URI uri, HttpMethod httpMethod) {
        return clientInterface.executor(uri, httpMethod);
    }

    public HttpMethod get(String url) {
        return new HttpGet(url, this);
    }

    public HttpMethod post(String url) {
        return new HttpUrlEncodedPost(url, this);
    }


    public static class RestFeginFactory {

        private Client client = new Client.Default(null, null);
        private Logger.Level logLevel = Logger.Level.NONE;
        private List<RequestInterceptor> interceptors = new ArrayList<>();

        public RestFeginFactory logLevel(Logger.Level logLevel) {
            this.logLevel = logLevel;
            return this;
        }

        public RestFeginFactory client(Client client) {
            this.client = client;
            return this;
        }

        public RestFeginFactory requestInterceptor(RequestInterceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }
        
        public RestFeign build() {
            return new RestFeign(client, logLevel, interceptors);
        }

    }

    /**
     * 通用请求接口，Fegin会根据此类生成代理类
     */
    interface ClientInterface {

        @RequestLine("GET")
        Response executor(URI uri, HttpMethod request);

    }

}
