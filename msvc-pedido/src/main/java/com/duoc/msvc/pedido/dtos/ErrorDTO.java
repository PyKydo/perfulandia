package com.duoc.msvc.pedido.dtos;

import java.util.Date;
import java.util.Map;

public class ErrorDTO {
    private Integer status;
    private Date date;
    private Map<String, String> errors;

    @Override
    public String toString(){
        return "{"+
                "status=" + status +
                ", date=" + date +
                ", errors=" + errors +
                '{';
    }
}
