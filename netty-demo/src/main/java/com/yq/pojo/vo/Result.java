package com.yq.pojo.vo;

/**
 * 返回结果集封装
 */
public class Result {
    private boolean success;//操作是否成功标识
    private String message;//返回的描述信息
    private Object result;//返回结果集封装

    public Result(boolean success, String message, Object result) {
        this.success = success;
        this.message = message;
        this.result = result;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Result(boolean success, Object result) {
        this.success = success;
        this.result = result;
    }

    public Result() {
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
