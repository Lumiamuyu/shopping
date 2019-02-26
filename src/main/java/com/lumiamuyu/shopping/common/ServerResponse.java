package com.lumiamuyu.shopping.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;



@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@Data
public class ServerResponse<T> {
    private int status;//返回到前端的状态码
    private T data;//返回给前端的数据
    private String msg;//当status!=0时，封装了错误信息

    public ServerResponse() {
    }

    public ServerResponse(int status, T data, String msg) {
        this.status = status;
        this.data = data;
        this.msg = msg;
    }

    public ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    public ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public ServerResponse(int status) {
        this.status = status;
    }


    /*调用接口成功时回调*/
    public static ServerResponse serverResponseBySuccess(){
        return new ServerResponse(ResponseCode.SUCCESS);
    }

    public static<T> ServerResponse serverResponseBySuccess(T data){
        return new ServerResponse(ResponseCode.SUCCESS,data);
    }

    public static<T> ServerResponse serverResponseBySuccess(T data,String msg){
        return new ServerResponse(ResponseCode.SUCCESS,data,msg);
    }


    /*接口调用失败时回调*/
    public static ServerResponse serverResponseByError(){
        return new ServerResponse(ResponseCode.ERROR);
    }

    public static ServerResponse serverResponseByError(int stutus){
        return new ServerResponse(stutus);
    }

    public static ServerResponse serverResponseByError(String msg){
        return new ServerResponse(ResponseCode.ERROR,msg);
    }

    public static ServerResponse serverResponseByError(int stutus,String msg){
        return new ServerResponse(stutus,msg);
    }


    @Override
    public String toString() {
        return "ServerResponse{" +
                "status=" + status +
                ", data=" + data +
                ", msg='" + msg + '\'' +
                '}';
    }

    /*判断接口是否正确返回
    * status=0
    * */
    @JsonIgnore
    public boolean isSuccess(){
        return this.status==ResponseCode.SUCCESS;
    }

    public static void main(String[] args) {
        ServerResponse serverResponse = ServerResponse.serverResponseBySuccess("QWER",null);
        System.out.println(serverResponse);
    }

}
