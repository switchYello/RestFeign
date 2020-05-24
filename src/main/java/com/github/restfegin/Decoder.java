package com.github.restfegin;

import feign.Response;

/**
 * 解码http响应，原生的不支持泛型，所以重写了一个
 */
public interface Decoder<T> {

    T decoder(Response response);

}
