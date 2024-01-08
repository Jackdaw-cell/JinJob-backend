package com.jackdaw.jinjobbackendcommon.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jackdaw.jinjobbackendcommon.config.AppConfig;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

@Component("jwtUtil")
public class JWTUtil<T> {

    @Resource
    private AppConfig appConfig;

    /**
     * 签名生成
     *
     * @return
     */
    public String createToken(String key, T data, Integer expireDay) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + expireDay * 24 * 60 * 60 * 1000);
            token = JWT.create()
                    .withClaim(key, JsonUtils.convertObj2Json(data))
                    .withExpiresAt(expiresAt)
                    .sign(Algorithm.HMAC256(appConfig.getJwtCommonSecret()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return token;
    }

    /**
     * 签名验证
     *
     * @param token
     * @return
     */
    public <T> T getTokenData(String key, String token, Class<T> classz) {
        try {
            if (StringTools.isEmpty(token)) {
                return null;
            }
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(appConfig.getJwtCommonSecret())).build();
            DecodedJWT jwt = verifier.verify(token);
            String jsonData = jwt.getClaim(key).asString();
            return JsonUtils.convertJson2Obj(jsonData, classz);
        } catch (Exception e) {
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        String TOKEN_SECRET = "1245";
        String TOKEN_KEY = "key";
        String token = JWT.create()
                .withClaim(TOKEN_KEY, "test")
                .withExpiresAt(new Date(System.currentTimeMillis() + 10000))
                .sign(Algorithm.HMAC256(TOKEN_SECRET));
        System.out.println(token);
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(TOKEN_SECRET)).build();
        DecodedJWT jwt = verifier.verify(token);
        String jsonData = jwt.getClaim(TOKEN_KEY).asString();
        System.out.println(jsonData);
    }
}