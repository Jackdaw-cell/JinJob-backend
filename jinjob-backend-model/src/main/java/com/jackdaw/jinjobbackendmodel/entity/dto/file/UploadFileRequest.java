package com.jackdaw.jinjobbackendmodel.entity.dto.file;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 文件上传请求
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UploadFileRequest implements Serializable {

    /**
     * 业务
     */
    private String biz;

    private static final long serialVersionUID = 1L;
}