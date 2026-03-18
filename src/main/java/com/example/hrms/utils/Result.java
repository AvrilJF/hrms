package com.example.hrms.utils;

import lombok.Data;


/**
 * 全局统一响应结果类（包含所有需要的方法）
 */
@Data
public class Result<T> {
    // 响应码：200成功 401未授权 403权限不足 500服务器错误
    private Integer code;
    // 响应消息
    private String msg;
    // 响应数据
    private T data;

    private Long total;
    // 分页数据列表（分页场景专用）
    private T records;
    // 私有构造，避免外部直接创建
    private Result() {}

    // ========== 成功响应 ==========
    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        return result;
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setData(data);
        return result;
    }

    public static <T> Result<T> success(long total, T records) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg("操作成功");
        result.setTotal(total); // 赋值总条数
        result.setRecords(records); // 赋值分页列表
        return result;
    }

    public static <T> Result<T> success(String msg, T data) {
        Result<T> result = new Result<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }



    // ========== 未授权响应（401） ==========
    public static <T> Result<T> unAuth() {
        Result<T> result = new Result<>();
        result.setCode(401);
        result.setMsg("未授权，请登录");
        return result;
    }

    public static <T> Result<T> unAuth(String msg) {
        Result<T> result = new Result<>();
        result.setCode(401);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> unAuth(T data) {
        Result<T> result = new Result<>();
        result.setCode(401);
        result.setMsg("未授权，请登录");
        result.setData(data);
        return result;
    }

    // ========== 权限不足响应（403） ==========
    public static <T> Result<T> forbidden() {
        Result<T> result = new Result<>();
        result.setCode(403);
        result.setMsg("权限不足，禁止访问");
        return result;
    }

    public static <T> Result<T> forbidden(T data) {
        Result<T> result = new Result<>();
        result.setCode(403);
        result.setMsg("权限不足，禁止访问");
        result.setData(data);
        return result;
    }

    // ========== 服务器错误响应（500） ==========
    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.setCode(500);
        result.setMsg(msg);
        return result;
    }
}