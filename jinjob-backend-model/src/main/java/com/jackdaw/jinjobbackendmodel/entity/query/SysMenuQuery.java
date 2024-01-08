package com.jackdaw.jinjobbackendmodel.entity.query;


import com.jackdaw.jinjobbackendmodel.common.BaseParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单表参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysMenuQuery extends BaseParam {


    /**
     * menu_id，自增主键
     */
    private Integer menuId;

    /**
     * 菜单名
     */
    private String menuName;

    private String menuNameFuzzy;

    /**
     * 菜单类型 0：菜单 1：按钮
     */
    private Integer menuType;

    /**
     * 菜单跳转到的地址
     */
    private String menuUrl;

    private String menuUrlFuzzy;

    /**
     * 上级菜单ID
     */
    private Integer pId;

    /**
     * 菜单排序
     */
    private Integer sort;

    /**
     * 权限编码
     */
    private String permissionCode;

    private String permissionCodeFuzzy;

    /**
     * 图标
     */
    private String icon;

    private String iconFuzzy;

    private Boolean formate2Tree;

    public Boolean getFormate2Tree() {
        return formate2Tree;
    }


}
