package com.jackdaw.jinjobbackendmodel.common;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 删除请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@Data
public class DeleteBatchRequest implements Serializable {

    /**
     * id
     */
    private ArrayList<Long> ids;

    private static final long serialVersionUID = 1L;
}