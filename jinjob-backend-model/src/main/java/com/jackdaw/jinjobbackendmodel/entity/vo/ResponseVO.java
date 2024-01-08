package com.jackdaw.jinjobbackendmodel.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseVO<T> {
    private String status;
    private Integer code;
    private String message;
    private T data;

}
