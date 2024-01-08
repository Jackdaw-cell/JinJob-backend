package com.jackdaw.jinjobbackendmodel.entity.po;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.enums.MenuTypeEnum;
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
public class SysMenu implements Serializable {


    /**
     * menu_id，自增主键
     */
    private Integer menuId;

    /**
     * 菜单名
     */
    @VerifyParam(required = true, max = 32)
    private String menuName;

    /**
     * 菜单类型 0：菜单 1：按钮
     */
    @VerifyParam(required = true)
    private Integer menuType;

    /**
     * 菜单跳转到的地址
     */
    private String menuUrl;

    /**
     * 上级菜单ID
     */
    @VerifyParam(required = true)
    @JsonProperty("pId")
    private Integer pId;

    /**
     * 菜单排序
     */
    @VerifyParam(required = true)
    private Integer sort;

    /**
     * 权限编码
     */
    @VerifyParam(max = 50)
    private String permissionCode;

    /**
     * 图标
     */
    @VerifyParam(max = 50)
    private String icon;

    private Boolean disabled;

    private String menuTypeName;

    private List<SysMenu> children = new ArrayList<>();

    public String getMenuTypeName() {
        MenuTypeEnum menuTypeEnums = MenuTypeEnum.getMenuTypeByType(menuType);
        return menuTypeEnums == null ? "" : menuTypeEnums.getDesc();
    }

    @Override
    public String toString() {
        return "menu_id，自增主键:" + (menuId == null ? "空" : menuId) + "，菜单名:" + (menuName == null ? "空" : menuName) + "，菜单类型 0：菜单 1：按钮:" + (menuType == null ? "空" :
                menuType) + "，菜单跳转到的地址:" + (menuUrl == null ? "空" : menuUrl) + "，上级菜单ID:" + (pId == null ? "空" : pId) + "，菜单排序:" + (sort == null ? "空" : sort) +
                "，权限编码:" + (permissionCode == null ? "空" : permissionCode) + "，图标:" + (icon == null ? "空" :
                icon);
    }

    public boolean equals(Object obj) {
        if (obj instanceof SysMenu) {
            SysMenu name = (SysMenu) obj;
            return (menuId.equals(name.menuId));
        }
        return super.equals(obj);
    }

    public int hashCode() {
        return menuId.hashCode();

    }
}
