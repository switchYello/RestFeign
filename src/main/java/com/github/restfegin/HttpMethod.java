package com.github.restfegin;

import feign.RequestTemplate;

/**
 * 请求通用接口
 */
public interface HttpMethod {

    /*
     * 添加参数，如果value为null，则不添加
     * */
    HttpMethod addParam(String key, Object value);

    /*
     * 添加请求头，如果value为null，则变成空字符
     * */
    HttpMethod addHeader(String key, Object value);

    /*
     * 发送请求，并将响应体转成字符串
     * */
    String executor();

    /*
     * 发送请求，使用自定义解码器解码响应
     * */
    <T> T executor(Decoder<T> decoder);

    /**
     * 当Fegin解析接口时，发现无法识别的参数（如参数上没有加注解），会使用encode接口处理参数。
     * 自定义encoder内，回掉此方法来处理参数
     */
    void encodeRequestBody(RequestTemplate template);

}
