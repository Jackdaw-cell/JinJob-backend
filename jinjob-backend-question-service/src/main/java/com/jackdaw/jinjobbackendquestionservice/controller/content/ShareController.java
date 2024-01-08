package com.jackdaw.jinjobbackendquestionservice.controller.content;

import com.jackdaw.jinjobbackendcommon.annotation.GlobalInterceptor;
import com.jackdaw.jinjobbackendcommon.annotation.VerifyParam;
import com.jackdaw.jinjobbackendmodel.common.BaseResponse;
import com.jackdaw.jinjobbackendmodel.common.ResultUtils;
import com.jackdaw.jinjobbackendmodel.entity.dto.SessionUserAdminDto;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserCollect;
import com.jackdaw.jinjobbackendmodel.entity.po.ShareInfo;
import com.jackdaw.jinjobbackendmodel.entity.query.ShareInfoQuery;
import com.jackdaw.jinjobbackendmodel.entity.vo.PaginationResultVO;
import com.jackdaw.jinjobbackendmodel.entity.vo.ResponseVO;
import com.jackdaw.jinjobbackendmodel.enums.CollectTypeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.PostStatusEnum;
import com.jackdaw.jinjobbackendquestionservice.controller.ABaseController;
import com.jackdaw.jinjobbackendquestionservice.service.ShareInfoService;
import com.jackdaw.jinjobbackendserviceclient.service.user.UserFeignClient;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/share")
public class ShareController extends ABaseController {

    @Resource
    private ShareInfoService shareInfoService;


//    @Resource
//    private AppUserCollectService appUserCollectService;

    @Resource
    private UserFeignClient userFeignClient;

    @GetMapping("/loadShareList")
    public BaseResponse<PaginationResultVO<ShareInfo>> loadShareList(@RequestParam Integer pageNo,
                                                                     @RequestParam Integer pageSize) {
        ShareInfoQuery query = new ShareInfoQuery();
        query.setPageNo(pageNo);
        query.setPageSize(pageSize);
        query.setOrderBy("share_id desc");
        query.setStatus(PostStatusEnum.POST.getStatus());
        query.setQueryTextContent(false);
//        return getSuccessResponseVO(shareInfoService.findListByPage(query));
        return ResultUtils.success(shareInfoService.findListByPage(query));
    }

    //TODO:新增接口 getShareDetail获取当前文章
    //TODO:新增评论系统；评论表，文章评论接口

    @GetMapping("/getShareDetailNext")
    @GlobalInterceptor
    public BaseResponse<ShareInfo> getShareDetailNext(@RequestHeader(value = "Authorization", required = false) String token,
                                                      @VerifyParam(required = true) @RequestParam Integer currentId) {
        ShareInfoQuery query = new ShareInfoQuery();
        query.setStatus(PostStatusEnum.POST.getStatus());
        ShareInfo shareInfo = shareInfoService.showShareDetailNext(query, currentId, true);
        shareInfo.setHaveCollect(false);
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        if (null != userAppDto) {
//            AppUserCollect appUserCollect = appUserCollectService.getAppUserCollectByUserIdAndObjectIdAndCollectType(userAppDto.getUserId(),
//                    shareInfo.getShareId().toString(), CollectTypeEnum.SHARE.getType());
            AppUserCollect appUserCollect = userFeignClient.getAppUserCollectByUserIdAndObjectIdAndCollectType(userAppDto.getUserId(),
                    shareInfo.getShareId().toString(), CollectTypeEnum.SHARE.getType());
            if (appUserCollect != null) {
                shareInfo.setHaveCollect(true);
            }
        }
        shareInfo.setContent(resetContentImg(shareInfo.getContent()));
//        return getSuccessResponseVO(shareInfo);
        return ResultUtils.success(shareInfo);
    }

    /**
     * 新增/修改
     */
    @PostMapping("/saveShareInfo")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_EDIT)
    public ResponseVO saveShareInfo(@RequestHeader(value = "Authorization", required = false) String token,
                                    @RequestBody ShareInfo shareInfo) {
        ShareInfo bean = new ShareInfo();
        bean.setShareId(shareInfo.getShareId());
        bean.setTitle(shareInfo.getTitle());
        bean.setCoverType(shareInfo.getCoverType());
        bean.setCoverPath(shareInfo.getCoverPath());
        bean.setContent(shareInfo.getContent());
        bean.setPostUserType(shareInfo.getPostUserType());
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        bean.setCreateUserId(String.valueOf(userAppDto.getUserId()));
        bean.setCreateUserName(userAppDto.getNickName());
        shareInfoService.saveShare(bean);
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delShare")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_DEL)
    public ResponseVO delShare(@RequestHeader(value = "Authorization", required = false) String token,
                               @VerifyParam(required = true) String shareIds) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        shareInfoService.delShareBatch(shareIds, userAppDto.getUserId());
        return getSuccessResponseVO(null);
    }

    @PostMapping("/delShareBatch")
    @GlobalInterceptor(permissionCode = PermissionCodeEnum.SHARE_DEL_BATCH)
    public ResponseVO delShareBatch(@RequestHeader(value = "Authorization", required = false) String token,
                                    @VerifyParam(required = true) String shareIds) {
        AppUserLoginDto userAppDto = getAppUserLoginInfoFromToken(token);
        shareInfoService.delShareBatch(shareIds,"");
        return getSuccessResponseVO(null);
    }
}
