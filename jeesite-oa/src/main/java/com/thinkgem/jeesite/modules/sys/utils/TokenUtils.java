package com.thinkgem.jeesite.modules.sys.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.CacheUtils;
import com.thinkgem.jeesite.common.utils.DateUtils;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.Set;

/**
 * API的TOKE工具类
 */
public class TokenUtils {

    private static final String TOKEN_CACHE = "jwtTokenCache";
    private static final String TOKEN_CACHE_ID_ = "userId_";

    private static final String Claim_USER_Id = "c_userId";
    private static final String Claim_CREATE_TIME = "c_createTime";
    private static final String Claim_RANDOM = "c_random";

    public static void main(String[] args) {
        System.out.println("111");
    }

    /**
     * 根据用户id，生成TOKEN
     *
     * @param userId
     * @return
     */
    public static String createToken(String userId) {
        String token = "";
        if (StringUtils.isBlank(userId)) {
            return token;
        }
        try {
            //token中payload的自定义数据为（userId，签发日期,随机数）
            token = JWT.create()
                    .withClaim(Claim_USER_Id, userId)
                    .withClaim(Claim_CREATE_TIME, DateUtils.getCurrentDateTime())
                    .withClaim(Claim_RANDOM, Math.random())
                    .sign(Algorithm.HMAC256(Global.getConfig("jwtSecret")));
        } catch (JWTCreationException e) {
            //Invalid Signing configuration / Couldn't convert Claims.
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        if (StringUtils.isNotBlank(token)) {
            //保存TOKEN到缓存中
            Set<String> tokenSet = new HashSet<>();
            tokenSet.add(token);
            Set<String> cacheTokenSet = (Set<String>) CacheUtils.get(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId);
            if (CollectionUtils.isNotEmpty(cacheTokenSet)) {
                tokenSet.addAll(cacheTokenSet);
                CacheUtils.remove(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId);
            }
            CacheUtils.put(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId, tokenSet);
            System.out.println("【createToken】用户:" + userId + " 获取新的token。当前TOKEN数目:" + tokenSet.size());

        }

        return token;
    }

    /**
     * 校验token，成功返回用户id，否则返回null
     *
     * @param token
     * @return
     */
    public static String getUserIdByToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        try {
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(Global.getConfig("jwtSecret"))).build();
            JWT jwt = (JWT) verifier.verify(token);
            Claim claim = jwt.getClaim(Claim_USER_Id);
            Set<String> cacheTokenSet = (Set<String>) CacheUtils.get(TOKEN_CACHE, TOKEN_CACHE_ID_ + claim.asString());
            if (CollectionUtils.isNotEmpty(cacheTokenSet)) {
                System.out.println("【getUserIdByToken】用户:" + claim.asString() + " 当前TOKEN数目:" + cacheTokenSet.size());
                if (cacheTokenSet.contains(token)) {
                    return claim.asString();
                }
            }
        } catch (JWTVerificationException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户id，移除缓存中该用户的所有TOKEN
     *
     * @param userId
     */
    public static void removeTokensByUserId(String userId) {
        if (StringUtils.isBlank(userId)) {
            return;
        }
        Set<String> cacheTokenSet = (Set<String>) CacheUtils.get(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId);
        if (CollectionUtils.isNotEmpty(cacheTokenSet)) {
            CacheUtils.remove(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId);
            System.out.println("【removeTokensByUserId】用户:" + userId + " 的所有TOKEN被清除，数目:" + cacheTokenSet.size());
        }
    }

    /**
     * 用户退出时候，清除token。
     *
     * @param userId
     * @param token
     */
    public static Boolean removeToken(String userId, String token) {
        if (StringUtils.isBlank(token)) {
            return false;
        }
        Set<String> cacheTokenSet = (Set<String>) CacheUtils.get(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId);
        if (CollectionUtils.isEmpty(cacheTokenSet)) {
            return false;
        }
        if (!cacheTokenSet.contains(token)) {
            return false;
        }
        cacheTokenSet.remove(token);
        CacheUtils.remove(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId);
        if (CollectionUtils.isNotEmpty(cacheTokenSet)) {
            CacheUtils.put(TOKEN_CACHE, TOKEN_CACHE_ID_ + userId, cacheTokenSet);
        }
        System.out.println("【removeToken】用户:" + userId + " 退出系统，清除token：" + token);
        return true;
    }


}
