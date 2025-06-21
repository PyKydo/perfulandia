package com.duoc.msvc.producto.dtos;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
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
