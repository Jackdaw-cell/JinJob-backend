package com.jackdaw.jinjobbackendmodel.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 菜单表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("serial")
public class SysMenuVO implements Serializable {
    
    private static final long serialVersionUID = 851203620520311526L;
    /**
     * 菜单名
     */
    private String menuName;
    /**
     * 菜单跳转到的地址
     */
    private String menuUrl;

    private String icon;

    private List<SysMenuVO> children = new ArrayList<>();

}
