package com.jackdaw.jinjobbackendadminservice.service.impl;

import com.jackdaw.jinjobbackendadminservice.mapper.SysMenuMapper;
import com.jackdaw.jinjobbackendadminservice.service.SysMenuService;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.po.SysMenu;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.query.SysMenuQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * 菜单表 业务接口实现
 */
@Service("sysMenuService")
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper<SysMenu, SysMenuQuery> sysMenuMapper;

    private static final String ROOT_MENU_NAME = "所有菜单";

    public static final Integer DEFAULT_ROOT_MENUID = 0;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<SysMenu> findListByParam(SysMenuQuery param) {
        List<SysMenu> sysMenuList = this.sysMenuMapper.selectList(param);
        if (param.getFormate2Tree() != null && param.getFormate2Tree()) {
            SysMenu root = new SysMenu();
            root.setMenuId(DEFAULT_ROOT_MENUID);
            root.setPId(-1);
            root.setMenuName(ROOT_MENU_NAME);
            sysMenuList.add(root);
            sysMenuList = convertLine2Tree4Menu(sysMenuList, -1);
        }
        return sysMenuList;
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(SysMenuQuery param) {
        return this.sysMenuMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<SysMenu> findListByPage(SysMenuQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<SysMenu> list = this.findListByParam(param);
        PaginationResultVO<SysMenu> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(SysMenu bean) {
        return this.sysMenuMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<SysMenu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.sysMenuMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<SysMenu> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.sysMenuMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(SysMenu bean, SysMenuQuery param) {
        StringTools.checkParam(param);
        return this.sysMenuMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(SysMenuQuery param) {
        StringTools.checkParam(param);
        return this.sysMenuMapper.deleteByParam(param);
    }

    /**
     * 根据MenuId获取对象
     */
    @Override
    public SysMenu getSysMenuByMenuId(Integer menuId) {
        return this.sysMenuMapper.selectByMenuId(menuId);
    }

    /**
     * 根据MenuId修改
     */
    @Override
    public Integer updateSysMenuByMenuId(SysMenu bean, Integer menuId) {
        return this.sysMenuMapper.updateByMenuId(bean, menuId);
    }

    /**
     * 根据MenuId删除
     */
    @Override
    public Integer deleteSysMenuByMenuId(Integer menuId) {
        SysMenuQuery sysMenuQuery = new SysMenuQuery();
        sysMenuQuery.setPId(menuId);
        Integer count = sysMenuMapper.selectCount(sysMenuQuery);
        if (count > 0) {
            throw new BusinessException("请先删除子菜单");
        }
        return this.sysMenuMapper.deleteByMenuId(menuId);
    }

    @Override
    public void saveMenu(SysMenu sysMenu) {
        if (sysMenu.getMenuId() == null) {
            this.sysMenuMapper.insert(sysMenu);
        } else {
            this.sysMenuMapper.updateByMenuId(sysMenu, sysMenu.getMenuId());
        }
    }

    public List<SysMenu> convertLine2Tree4Menu(List<SysMenu> dataList, Integer pId) {
        List<SysMenu> children = new ArrayList<>();
        for (SysMenu m : dataList) {
            if (m.getMenuId() != null && m.getPId() != null && m.getPId().equals(pId)) {
                m.setChildren(convertLine2Tree4Menu(dataList, m.getMenuId()));
                children.add(m);
            }
        }
        return children;
    }


    @Override
    public List<SysMenu> selectAllMenuByRoleIds(String roleIds) {
        if (StringTools.isEmpty(roleIds)) {
            return new ArrayList<>();
        }
        int[] roleIdArray = Arrays.stream(roleIds.split(",")).mapToInt(Integer::valueOf).toArray();
        List<SysMenu> allMenus = this.sysMenuMapper.selectMenusByRoleIds(roleIdArray);
        return allMenus;
    }
}