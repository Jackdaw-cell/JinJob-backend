package com.jackdaw.jinjobbackendadminservice.service.impl;

import com.jackdaw.jinjobbackendadminservice.mapper.SysAccountMapper;
import com.jackdaw.jinjobbackendadminservice.service.SysAccountService;
import com.jackdaw.jinjobbackendadminservice.service.SysMenuService;
import com.jackdaw.jinjobbackendcommon.config.AppConfig;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.CopyTools;
import com.jackdaw.jinjobbackendcommon.utils.StringTools;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.po.SysAccount;
import com.jackdaw.jinjobbackendmodel.entity.po.SysMenu;
import com.jackdaw.jinjobbackendmodel.entity.query.SimplePage;
import com.jackdaw.jinjobbackendmodel.entity.query.SysAccountQuery;
import com.jackdaw.jinjobbackendmodel.entity.query.SysMenuQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.SysMenuVO;
import com.jackdaw.jinjobbackendmodel.enums.MenuTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PageSize;
import com.jackdaw.jinjobbackendmodel.enums.SysAccountStatusEnum;
import com.jackdaw.jinjobbackendmodel.enums.UserStatusEnum;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * 账号信息 业务接口实现
 */
@Service("sysAccountService")
public class SysAccountServiceImpl implements SysAccountService {

    @Resource
    private SysAccountMapper<SysAccount, SysAccountQuery> sysAccountMapper;

    @Resource
    private SysMenuService sysMenuService;


    @Resource
    private AppConfig appConfig;

    /**
     * 根据条件查询列表
     */
    @Override
    public List<SysAccount> findListByParam(SysAccountQuery param) {
        return this.sysAccountMapper.selectList(param);
    }

    /**
     * 根据条件查询列表
     */
    @Override
    public Integer findCountByParam(SysAccountQuery param) {
        return this.sysAccountMapper.selectCount(param);
    }

    /**
     * 分页查询方法
     */
    @Override
    public PaginationResultVO<SysAccount> findListByPage(SysAccountQuery param) {
        int count = this.findCountByParam(param);
        int pageSize = param.getPageSize() == null ? PageSize.SIZE15.getSize() : param.getPageSize();

        SimplePage page = new SimplePage(param.getPageNo(), count, pageSize);
        param.setSimplePage(page);
        List<SysAccount> list = this.findListByParam(param);
        PaginationResultVO<SysAccount> result = new PaginationResultVO(count, page.getPageSize(), page.getPageNo(), page.getPageTotal(), list);
        return result;
    }

    /**
     * 新增
     */
    @Override
    public Integer add(SysAccount bean) {
        return this.sysAccountMapper.insert(bean);
    }

    /**
     * 批量新增
     */
    @Override
    public Integer addBatch(List<SysAccount> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.sysAccountMapper.insertBatch(listBean);
    }

    /**
     * 批量新增或者修改
     */
    @Override
    public Integer addOrUpdateBatch(List<SysAccount> listBean) {
        if (listBean == null || listBean.isEmpty()) {
            return 0;
        }
        return this.sysAccountMapper.insertOrUpdateBatch(listBean);
    }

    /**
     * 多条件更新
     */
    @Override
    public Integer updateByParam(SysAccount bean, SysAccountQuery param) {
        StringTools.checkParam(param);
        return this.sysAccountMapper.updateByParam(bean, param);
    }

    /**
     * 多条件删除
     */
    @Override
    public Integer deleteByParam(SysAccountQuery param) {
        StringTools.checkParam(param);
        return this.sysAccountMapper.deleteByParam(param);
    }

    /**
     * 根据UserId获取对象
     */
    @Override
    public SysAccount getSysAccountByUserId(Integer userId) {
        return this.sysAccountMapper.selectByUserId(userId);
    }

    /**
     * 根据UserId修改
     */
    @Override
    public Integer updateSysAccountByUserId(SysAccount bean, Integer userId) {
        return this.sysAccountMapper.updateByUserId(bean, userId);
    }

    /**
     * 根据UserId删除
     */
    @Override
    public Integer deleteSysAccountByUserId(Integer userId) {
        return this.sysAccountMapper.deleteByUserId(userId);
    }

    /**
     * 根据Phone获取对象
     */
    @Override
    public SysAccount getSysAccountByPhone(String phone) {
        return this.sysAccountMapper.selectByPhone(phone);
    }

    /**
     * 根据Phone修改
     */
    @Override
    public Integer updateSysAccountByPhone(SysAccount bean, String phone) {
        return this.sysAccountMapper.updateByPhone(bean, phone);
    }

    /**
     * 根据Phone删除
     */
    @Override
    public Integer deleteSysAccountByPhone(String phone) {
        return this.sysAccountMapper.deleteByPhone(phone);
    }

    @Override
    public void saveSysAccount(SysAccount sysAccount) throws BusinessException {
        SysAccount phoneDb = sysAccountMapper.selectByPhone(sysAccount.getPhone());

        if (phoneDb != null && (sysAccount.getUserId() == null || !phoneDb.getUserId().equals(sysAccount.getUserId()))) {
            throw new BusinessException(String.format("手机号【%s】已经存在", sysAccount.getPhone()));
        }
        if (sysAccount.getUserId() == null) {
            sysAccount.setCreateTime(new Date());
            sysAccount.setStatus(UserStatusEnum.ENABLE.getStatus());
            sysAccount.setPassword(StringTools.encodeByMD5(sysAccount.getPassword()));
            this.sysAccountMapper.insert(sysAccount);
        } else {
            sysAccount.setPassword(null);
            sysAccount.setStatus(null);
            this.sysAccountMapper.updateByUserId(sysAccount, sysAccount.getUserId());
        }
    }

    @Override
    public SessionUserAdminDto login(String phone, String password) throws BusinessException {
        //检查用户可用性
        SysAccount sysAccount = this.sysAccountMapper.selectByPhone(phone);
        if (sysAccount == null) {
            throw new BusinessException("账号或者密码错误");
        }

        if (SysAccountStatusEnum.DISABLE.getStatus().equals(sysAccount.getStatus())) {
            throw new BusinessException("账号已禁用");
        }
        if (!sysAccount.getPassword().equals(password)) {
            throw new BusinessException("账号或者密码错误");
        }
        SessionUserAdminDto sessionUserDto = new SessionUserAdminDto();
        sessionUserDto.setUserId(sysAccount.getUserId());
        sessionUserDto.setUserName(sysAccount.getUserName());
        List<SysMenu> allMenus = new ArrayList<>();
        //查询用户可见菜单
        if (!StringTools.isEmpty(appConfig.getSuperAdminPhones()) && ArrayUtils.contains(appConfig.getSuperAdminPhones().split(","), phone)) {
            sessionUserDto.setSuperAdmin(true);
            SysMenuQuery query = new SysMenuQuery();
            query.setOrderBy("sort asc");
            allMenus = sysMenuService.findListByParam(query);
        } else {
            sessionUserDto.setSuperAdmin(false);
            allMenus = sysMenuService.selectAllMenuByRoleIds(sysAccount.getRoles());
        }

        List<SysMenu> menuList = new ArrayList<>();
        List<String> permissionCodeList = new ArrayList<>();
        sessionUserDto.setPermissionCodeList(permissionCodeList);
        for (SysMenu sysMenu : allMenus) {
            if (MenuTypeEnum.MEMU.getType().equals(sysMenu.getMenuType())) {
                menuList.add(sysMenu);
            }
            permissionCodeList.add(sysMenu.getPermissionCode());
        }
        menuList = sysMenuService.convertLine2Tree4Menu(menuList, 0);
        if (menuList.isEmpty()) {
            throw new BusinessException("请联系管理员分配角色");
        }
        List<SysMenuVO> menuVOList = new ArrayList<>();
        menuList.forEach(item -> {
            SysMenuVO menuVO = CopyTools.copy(item, SysMenuVO.class);
            menuVO.setChildren(CopyTools.copyList(item.getChildren(), SysMenuVO.class));
            menuVOList.add(menuVO);
        });
        sessionUserDto.setMenuList(menuVOList);
        return sessionUserDto;
    }
}