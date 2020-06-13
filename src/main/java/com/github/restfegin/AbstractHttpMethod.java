package com.github.restfegin;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import feign.codec.StringDecoder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基础实现
 */
public abstract class AbstractHttpMethod implements HttpMethod {

    private ErrorDecoder errorDecoder = new ErrorDecoder.Default();
    private Decoder<String> defaultDecoder = new Decoder<String>() {
        @Override
        public String decoder(Response response) {
            try {
                return (String) new StringDecoder().decode(response, String.class);
            } catch (IOException e) {
                throw new RuntimeException(errorDecoder.decode("decoder exception", response));
            }
        }
    };
    protected Map<String, Collection<String>> params = new LinkedHashMap<>(1);
    protected Map<String, Collection<String>> heads = new LinkedHashMap<>(1);

    /*
     * skip null value
     * */
    @Override
    public HttpMethod addParam(String key, Object value) {
        if (value == null) {
            return this;
        }
        params.computeIfAbsent(key, k -> new ArrayList<>(1));
        params.get(key).add(String.valueOf(value));
        return this;
    }

    /*
     * null to empty
     * */
    @Override
    public HttpMethod addHeader(String key, Object value) {
        heads.computeIfAbsent(key, k -> new ArrayList<>(1));
        heads.get(key).add(value == null ? "" : String.valueOf(value));
        return this;
    }

    @Override
    public String executor() {
        return executor(defaultDecoder);
    }

    @Override
    public <T> T executor(Decoder<T> decoder) {
        try (Response response = doExecutor()) {
            if (response.status() >= 200 && response.status() < 300) {
                return decoder.decoder(response);
            }
            throw FeignException.errorStatus("status not in [200,300)", response);
        }
    }

    static String urlEncode(Object arg) {
        try {
            return URLEncoder.encode(String.valueOf(arg), "utf-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract Response doExecutor();

}
