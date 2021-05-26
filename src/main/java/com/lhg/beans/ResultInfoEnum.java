package com.lhg.beans;

public enum ResultInfoEnum {
    SUCCESS("成功", "0"),
    FAIL("失败", "-9999"),
    ;
    private String msg;
    private String code;

    ResultInfoEnum(String msg, String code) {
        this.msg = msg;
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
