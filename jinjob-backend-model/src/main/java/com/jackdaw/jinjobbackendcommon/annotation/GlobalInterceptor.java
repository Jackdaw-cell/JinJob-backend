package com.jackdaw.jinjobbackendcommon.annotation;

import com.jackdaw.jinjobbackendmodel.enums.PermissionCodeEnum;
import com.jackdaw.jinjobbackendmodel.enums.RequestFrequencyTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface GlobalInterceptor {

    boolean checkLogin() default false;

    /**
     * 权限编码
     */
    PermissionCodeEnum permissionCode() default PermissionCodeEnum.NO_PERMISSION;

    /**
     * 校验参数
     *
     * @return
     */
    boolean checkParams() default true;

    /**
     * 频次阈值
     *
     * @return
     */
    int reqeustFrequencyThreshold() default 0;

    /**
     * 限制类型
     *
     * @return
     */
    RequestFrequencyTypeEnum frequencyType() default RequestFrequencyTypeEnum.NO_LIMIT;


}
