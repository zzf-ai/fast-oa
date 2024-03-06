package com.zzf.common.exception;

import com.zzf.common.result.ResultCodeEnum;
import lombok.Data;

/**
 * @author zzf
 * @date 2024-01-25
 */
@Data
public class CustomException extends RuntimeException {

    private Integer code;//状态码
    private String msg;//描述信息

    public CustomException(Integer code,String msg) {
        super(msg);
        this.code = code;
        this.msg = msg;
    }

    /**
     * 接收枚举类型对象
     * @param resultCodeEnum
     */
    public CustomException(ResultCodeEnum resultCodeEnum) {
        super(resultCodeEnum.getMessage());
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
    }

    @Override
    public String toString() {
        return "GuliException{" +
                "code=" + code +
                ", message=" + this.getMessage() +
                '}';
    }
}
