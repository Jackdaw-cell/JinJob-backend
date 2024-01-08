package com.jackdaw.jinjobbackendmodel.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 删除请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}