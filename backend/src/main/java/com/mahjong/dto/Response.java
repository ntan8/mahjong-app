package com.mahjong.dto;

import java.util.ArrayList;
import java.util.List;

public class Response<T> {
    private T data;
    private List<String> errors;
    
    public Response() {
        this.errors = new ArrayList<>();
    }
    
    public Response(T data) {
        this.data = data;
        this.errors = new ArrayList<>();
    }
    
    public Response(T data, List<String> errors) {
        this.data = data;
        this.errors = errors;
    }
    
    public static <T> Response<T> success(T data) {
        return new Response<>(data);
    }
    
    public static <T> Response<T> error(String error) {
        Response<T> response = new Response<>();
        response.addError(error);
        return response;
    }
    
    public static <T> Response<T> errors(List<String> errors) {
        return new Response<>(null, errors);
    }
    
    public T getData() {
        return data;
    }
    
    public void setData(T data) {
        this.data = data;
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
    
    public void addError(String error) {
        this.errors.add(error);
    }
    
    public boolean hasErrors() {
        return errors != null && !errors.isEmpty();
    }
}