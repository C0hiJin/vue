package com.example.vuespringjava.common.lang;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result implements Serializable {
//状态码
    private int code;
//    信息
    private String msg;
//
    private Object data;
//    成功
    public static Result succ(Object data){
        return succ(200,"操作成功",data);
    }
    public static Result fail(String msg){
        return fail(400,msg,null);
    }
    public static Result succ(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
    public static Result fail(int code,String msg,Object data){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }



}
