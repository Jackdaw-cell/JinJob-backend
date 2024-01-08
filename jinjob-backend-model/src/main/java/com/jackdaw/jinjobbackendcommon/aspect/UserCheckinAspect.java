package com.jackdaw.jinjobbackendcommon.aspect;


import com.jackdaw.jinjobbackendcommon.annotation.DailyCheckin;
import com.jackdaw.jinjobbackendmodel.common.ErrorCode;
import com.jackdaw.jinjobbackendcommon.config.RedisUtils;
import com.jackdaw.jinjobbackendmodel.exception.BusinessException;
import com.jackdaw.jinjobbackendcommon.utils.JWTUtil;
import com.jackdaw.jinjobbackendmodel.constant.Constants;
import com.jackdaw.jinjobbackendmodel.entity.dto.appUser.AppUserLoginDto;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component("userCheckinAspect")
@Aspect
public class UserCheckinAspect {

    private static Logger logger = LoggerFactory.getLogger(UserCheckinAspect.class);

    @Resource
    private JWTUtil<AppUserLoginDto> jwtUtil;

    @Resource
    private RedisUtils redisUtils;

    /**
     * 方法一：环绕通知获取返回值
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@annotation(com.jackdaw.jinjobbackendcommon.annotation.DailyCheckin)")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = joinPoint.proceed();
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        DailyCheckin interceptor = method.getAnnotation(DailyCheckin.class);
        if (null == interceptor) {
            return result;
        }
        if (interceptor.checkLogin()) {
            AppUserLoginDto appUserLoginDto = checkLogin();
            //签到用户键值对  jinjobBackendQuestiob:用户ID:年月 = 位图
            LocalDate nowDate =LocalDate.now();
            String[] keyElement = {
                    "spring",
                    "userCheckin",
                    "jinjobBackendQuestiob",
                    appUserLoginDto.getUserId(),
                    nowDate.format(DateTimeFormatter.ofPattern("yyyyMM"))
            };
            String redisKey = StringUtils.join(keyElement, ":");
            int dayOfMonth = LocalDateTime.now().getDayOfMonth();
            redisUtils.setBitTrue(redisKey,dayOfMonth-1);
            return result;
        }
        return result;
    }

//    /**
//     * 方法二：AfterReturn获取返回值
//     * @param joinPoint
//     * @param result
//     */
//    @AfterReturning(value = "@annotation(com.jackdaw.jinjobbackendcommon.annotation.DailyCheckin)", returning = "result")
//    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
//        // 获取方法返回值并进行处理
//        System.out.println("方法返回值：" + result);
//    }

    private AppUserLoginDto checkLogin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("Authorization");
        AppUserLoginDto userAppDto = jwtUtil.getTokenData(Constants.JWT_KEY_LOGIN_TOKEN, token, AppUserLoginDto.class);
        if (userAppDto == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userAppDto;
    }
}