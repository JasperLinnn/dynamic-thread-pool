package com.jasper.middleware.dynamic.thread.pool.types;

import lombok.*;

import java.io.Serializable;

/**
 * @author zihong
 * @description 返回类
 * @create 2025/4/8 16:37
 **/
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {
    private static final long serialVersionUID = -2474596551402989285L;
    private String code;
    private String info;
    private T data;

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum Code {
        SUCCESS("0000", "成功"),
        ERROR("0001", "失败"),
        PARAM_ERROR("0002", "参数错误"),
        SERVER_ERROR("0003", "服务器异常"),
        ;
        private String code;
        private String info;
    }

    public static<T> Response<T> success(T data){
        return Response.<T>builder()
                .code(Response.Code.SUCCESS.getCode())
                .info(Response.Code.SUCCESS.getInfo())
                .data(data)
                .build();
    }


    public static<T> Response<T> error(){
        return Response.<T>builder()
                .code(Code.ERROR.getCode())
                .info(Code.ERROR.getInfo())
                .build();
    }

    public static Response<Boolean> error(Boolean type){
        return Response.<Boolean>builder()
                .code(Code.ERROR.getCode())
                .info(Code.ERROR.getInfo())
                .data(type)
                .build();
    }
}
