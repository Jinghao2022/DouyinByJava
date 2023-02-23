package com.bytedance.douyinbyjava.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto<T> implements Serializable {
    /**
     * 状态码，0-成功，其他值-失败
     */
    @JsonProperty("status_code")
    private Integer statusCode;
    /**
     * 返回状态描述
     */
    @JsonProperty("status_msg")
    private String statusMsg;

    private T data;

    /**
     * 补充信息，动态数据
     */
    private Map<String, Object> map = new HashMap<>();

    public static <T> ResponseDto success(T data) {
        ResponseDto<T> r = new ResponseDto<>();
        r.statusCode = 0;
        r.data = data;
        return r;
    }

    public static <Void> ResponseDto failure(String errMsg) {
        ResponseDto r = new ResponseDto();
        r.statusCode = 1;
        r.statusMsg = errMsg;
        return r;
    }

    public ResponseDto add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }
}
