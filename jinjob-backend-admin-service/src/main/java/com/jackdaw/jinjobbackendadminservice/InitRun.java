//package com.jackdaw.jinjobbackendadminservice;
//
//
//import com.jackdaw.jinjobbackendcommon.config.RedisUtils;
//import com.jackdaw.jinjobbackendcommon.exception.BusinessException;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.data.redis.RedisConnectionFailureException;
//import org.springframework.stereotype.Component;
//
//import javax.annotation.Resource;
//import javax.sql.DataSource;
//
//@Component("initRun")
//public class InitRun implements ApplicationRunner {
//
//    private static final Logger logger = LoggerFactory.getLogger(InitRun.class);
//
//    @Resource
//    private DataSource dataSource;
//
//    @Resource
//    private RedisUtils redisUtils;
//
//    @Override
//    public void run(ApplicationArguments args) {
//        try {
//            dataSource.getConnection();
//            redisUtils.get("test");
//            logger.error("服务启动成功，可以开始愉快的开发了");
//        } catch (RedisConnectionFailureException e) {
//            logger.error("redi配置错误，请仔细检查redis配置");
//            throw new BusinessException(API_REQUEST_ERROR，"服务启动失败");
//        }catch (Exception e) {
//            logger.error("数据库配置错误，请检查配置");
//            throw new BusinessException(API_REQUEST_ERROR，"服务启动失败");
//        }
//    }
//}
