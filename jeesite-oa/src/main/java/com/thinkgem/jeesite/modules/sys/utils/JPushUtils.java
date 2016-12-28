package com.thinkgem.jeesite.modules.sys.utils;

import com.google.common.base.Splitter;
import com.thinkgem.jeesite.common.config.Global;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.common.utils.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.jiguang.commom.ClientConfig;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.*;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

import java.util.List;

/**
 * 极光推送工具类
 */
public class JPushUtils {


    protected static final Logger LOG = LoggerFactory.getLogger(JPushUtils.class);

    public static final String Default_alertContent = "您有一条新的未读消息";
    public static final Integer Default_extraType = 0;

    private static final String AndroidNotification_TITLE = "首科OA新消息";

    public static void main(String[] args) {
//        String alias = "1";
//        String alertContent = "你好,这是一条测试内容。";
//        Integer extraType = 0;//此标识字段目前暂未使用
//        System.out.println(sendPush(alias, alertContent, extraType));

        System.out.println(sendDefultPush("f9b44369477643f0addb0068cf6c6020"));
    }


    public static void sendPushToUsers(List<String> userIds, String content) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            for (String atUserId : userIds) {
                sendPush(atUserId, content, Default_extraType);
            }
        }
    }

    public static void sendDefaultPushToUsers(List<String> userIds) {
        if (CollectionUtils.isNotEmpty(userIds)) {
            for (String atUserId : userIds) {
                sendDefultPush(atUserId);
            }
        }
    }

    public static void sendDefaultPushToUsers(String userIds) {
        if (StringUtils.isBlank(StringUtils.strip(userIds, ",")))
            return;
        List<String> atUserIdList = Splitter.on(',').trimResults().omitEmptyStrings().splitToList(userIds);
        if (CollectionUtils.isNotEmpty(atUserIdList)) {
            for (String atUserId : atUserIdList) {
                sendDefultPush(atUserId);
            }
        }
    }

    public static Boolean sendDefultPush(String userId) {
        return sendPush(userId, Default_alertContent, Default_extraType);
    }

    public static Boolean sendPush(String userId, String alertContent, Integer extraType) {
        ClientConfig clientConfig = ClientConfig.getInstance();
        JPushClient jpushClient = new JPushClient(Global.getConfig("jpush.masterSecret"), Global.getConfig("jpush.appKey"), null, clientConfig);

        PushPayload payload = buildPushObject_android_and_ios(userId, alertContent, extraType);

        try {
            PushResult result = jpushClient.sendPush(payload);
            LOG.info("Got result - " + result);
            return result.isResultOK();
        } catch (APIConnectionException e) {
            LOG.error("Connection error. Should retry later. ", e);
        } catch (APIRequestException e) {
            LOG.error("Error response from JPush server. Should review and fix it. ", e);
            LOG.info("HTTP Status: " + e.getStatus());
            LOG.info("Error Code: " + e.getErrorCode());
            LOG.info("Error Message: " + e.getErrorMessage());
            LOG.info("Msg ID: " + e.getMsgId());
        }
        return false;
    }

    private static PushPayload buildPushObject_android_and_ios(String alias, String alertContent, Integer extraType) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(alertContent)
                        .addPlatformNotification(AndroidNotification.newBuilder().setTitle(AndroidNotification_TITLE)
                                .addExtra("type", extraType).build())
                        .addPlatformNotification(IosNotification.newBuilder().autoBadge()
                                .addExtra("type", extraType).build())
                        .build())
                .setOptions(Options.newBuilder()
                        .setApnsProduction(Global.getConfig("isProductionEnvironment").equals("true"))//true表示生产环境，false表示开发环境。不指定默认表示开发环境。
                        .build())
                .build();
    }


}
