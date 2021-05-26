package com.lhg.beans;

import java.util.List;

public class ResultInfo<T> {

    private String rtnMsg;
    private String rtnCode;
    private T result;
    private List<T> beans;

    public ResultInfo(ResultInfoEnum resultInfoEnum) {
        this.rtnMsg = resultInfoEnum.getMsg();
        this.rtnCode = resultInfoEnum.getCode();
    }

    public ResultInfo(ResultInfoEnum resultInfoEnum, T result) {
        this.rtnMsg = resultInfoEnum.getMsg();
        this.rtnCode = resultInfoEnum.getCode();
        this.result = result;
    }

    public ResultInfo(String rtnMsg, String rtnCode) {
        this.rtnMsg = rtnMsg;
        this.rtnCode = rtnCode;
    }

    public ResultInfo() {
    }

    public String getRtnMsg() {
        return rtnMsg;
    }

    public void setRtnMsg(String rtnMsg) {
        this.rtnMsg = rtnMsg;
    }

    public String getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(String rtnCode) {
        this.rtnCode = rtnCode;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public List<T> getBeans() {
        return beans;
    }

    public void setBeans(List<T> beans) {
        this.beans = beans;
    }

    @Override
    public String toString() {
        return "ReturnInfo{" +
                "rtnMsg='" + rtnMsg + '\'' +
                ", rtnCode='" + rtnCode + '\'' +
                ", result=" + result +
                ", beans=" + beans +
                '}';
    }
}
