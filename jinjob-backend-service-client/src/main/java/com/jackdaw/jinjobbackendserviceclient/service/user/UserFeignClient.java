package com.jackdaw.jinjobbackendserviceclient.service.user;

import com.jackdaw.jinjobbackendmodel.entity.po.AppUserCollect;
import com.jackdaw.jinjobbackendmodel.entity.po.AppUserInfo;
import com.jackdaw.jinjobbackendmodel.entity.vo.app.AppUserInfoVO;
import org.springframework.beans.BeanUtils;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 用户服务
 *
 * @author Jackdaw
 * @from https://github.com/Jackdaw-cell
 */
@FeignClient(name = "jinjob-backend-user-service", path = "/api/user/inner")
public interface UserFeignClient {

    /**
     * 根据 id 获取用户
     *
     * @param userId
     * @return
     */
    @GetMapping("/get/id")
    AppUserInfo getById(@RequestParam("userId") String userId);


    /**
     * 根据 token 获取用户
     *
     * @param token
     * @return
     */
    @GetMapping("/get/token")
    AppUserInfo getByToken(@RequestParam("token") String token);

    /**
     * 根据 token 获取用户
     *
     * @param token
     * @return
     */
    @GetMapping("/get/userCheckin")
    String getUserCheckin(@RequestParam("token") String token);

    /**
     *
     * @param token
     * @return
     */
    @GetMapping("/get/setCountOjQuestion")
    public boolean setCountOjQuestion(@RequestParam("token") String token);

    /**
     *
     * @param token
     * @return
     */
    @GetMapping("/get/setCountExamQuestion")
    public boolean setCountExamQuestion(@RequestParam("token") String token,
                                        @RequestParam("score") Float score);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    default AppUserInfoVO getUserVO(AppUserInfo user) {
        if (user == null) {
            return null;
        }
        AppUserInfoVO userVO = new AppUserInfoVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @GetMapping("/saveCollect")
    void saveCollect(@RequestParam("userId") String userId,@RequestParam("objectId") String objectId,@RequestParam("collectType") Integer collectType);

    @GetMapping("/deleteAppUserCollectByUserIdAndObjectIdAndCollectType")
    Integer deleteAppUserCollectByUserIdAndObjectIdAndCollectType(@RequestParam("userId") String userId,@RequestParam("objectId") String objectId,@RequestParam("collectType") Integer collectType);

    @GetMapping("/getAppUserCollectByUserIdAndObjectIdAndCollectType")
    AppUserCollect getAppUserCollectByUserIdAndObjectIdAndCollectType(@RequestParam("userId") String userId, @RequestParam("objectId") String objectId, @RequestParam("collectType") Integer collectType);
}
