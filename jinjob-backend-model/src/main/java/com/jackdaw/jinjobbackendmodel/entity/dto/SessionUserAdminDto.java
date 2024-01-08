package com.jackdaw.jinjobbackendmodel.entity.dto;

import com.jackdaw.jinjobbackendmodel.entity.vo.SysMenuVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SessionUserAdminDto implements Serializable {

    private static final long serialVersionUID = 1690149993220674991L;
    private Integer userId;
    private String userName;
    private List<SysMenuVO> menuList;
    private List<String> permissionCodeList = new ArrayList<>();
    private Boolean superAdmin;

}
