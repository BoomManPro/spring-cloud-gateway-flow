package cn.boommanpro.common;

import java.io.Serializable;

import lombok.Data;

/**
 * @author wangqimeng
 * @date 2019/11/7 14:40
 */
@Data
public class ResultVo<T> implements Serializable {

    private String code;

    private String message;

    private T data;

    private static final String SUCCESS = "SUCCESS";

    private static final String ERROR = "ERROR";

    public ResultVo() {
    }

    private ResultVo(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultVo<T> ofSuccess(T data) {
        return new ResultVo<>(SUCCESS, null, data);
    }

    public static <T> ResultVo<T> ofSuccess() {
        return new ResultVo<>(SUCCESS, null, null);
    }

    public static <T> ResultVo<T> ofError(String message) {
        return new ResultVo<>(ERROR, message, null);
    }
}
