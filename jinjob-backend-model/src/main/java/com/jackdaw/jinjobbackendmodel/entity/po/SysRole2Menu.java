package com.jackdaw.jinjobbackendmodel.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


/**
 * 角色对应的菜单权限表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SysRole2Menu implements Serializable {


	/**
	 * 角色ID
	 */
	private Integer roleId;

	/**
	 * 菜单ID
	 */
	private Integer menuId;

	/**
	 * 0:半选 1:全选
	 */
	private Integer checkType;

	@Override
	public String toString (){
		return "角色ID:"+(roleId == null ? "空" : roleId)+"，菜单ID:"+(menuId == null ? "空" : menuId)+"，0:半选 1:全选:"+(checkType == null ? "空" : checkType);
	}
}
