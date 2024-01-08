package com.jackdaw.jinjobbackendmodel.entity.vo.app;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


/**
 * app发布
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppUpdateVO implements Serializable {

    private Integer id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 更新描述
     */
    private List<String> updateList;

    private Long size;


    public void setUpdateList(List<String> updateList) {
        this.updateList = updateList;
    }
}
