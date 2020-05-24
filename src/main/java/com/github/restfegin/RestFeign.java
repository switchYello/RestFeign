package com.github.restfegin;

import feign.*;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;

public class RestFeign {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(RestFeign.class);

    //根据接口生成的代理类，每个RestFegin实例生成一个代理类
    private ClientInterface clientInterface;

    private Client client = new Client.Default(null, null);
    private Logger.Level logLevel = Logger.Level.NONE;

    private void initClient() {
        Feign.Builder builder = Feign.builder()
                .client(client)
                .logLevel(logLevel)
                .logger(new Slf4jLogger())
                //参数中无法识别的（没加注解），会调用此方法处理
                .encoder(new Encoder() {
                    @Override
                    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
                        if (bodyType == HttpMethod.class) {
                            HttpMethod request = (HttpMethod) object;
                            request.encodeRequestBody(template);
                        } else {
                            log.warn("feign.codec.Encoder.encode: bodyType not instance HttpMethod is " + bodyType);
                        }
                    }
                });
        clientInterface = builder.target(Target.EmptyTarget.create(ClientInterface.class));
    }

    public RestFeign() {
        initClient();
    }

    public RestFeign(Client client) {
        this.client = client;
        initClient();
    }

    public RestFeign(Client client, Logger.Level logLevel) {
        this.client = client;
        this.logLevel = logLevel;
        initClient();
    }

    public HttpMethod get(String url) {
        return new HttpGet(url, clientInterface);
    }

    public HttpMethod post(String url) {
        return new HttpUrlEncodedPost(url, clientInterface);
    }

}
