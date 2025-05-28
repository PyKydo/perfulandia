package com.duoc.msvc.pago.dtos;

import lombok.*;

import java.util.Date;
import java.util.Map;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
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
