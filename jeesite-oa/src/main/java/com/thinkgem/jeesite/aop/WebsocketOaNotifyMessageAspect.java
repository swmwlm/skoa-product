package com.thinkgem.jeesite.aop;

import com.google.common.collect.Lists;
import com.thinkgem.jeesite.common.utils.Collections3;
import com.thinkgem.jeesite.modules.oa.entity.OaNotify;
import com.thinkgem.jeesite.modules.sys.entity.User;
import com.thinkgem.jeesite.modules.sys.utils.JPushUtils;
import com.thinkgem.jeesite.modules.sys.utils.UserUtils;
import com.thinkgem.jeesite.websocket.SystemWebSocketHandler;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 在发布通知通告的时候,对该通知通告进行aop,使得websocket来发布该条通告给在线的相关用户;
 * 如果直接在OaNotifyService.save(oaNotify)中,来调用websocket,因为由事务一致性原因,总是得不到最新的未通知数;
 */
@Component
@Aspect
public class WebsocketOaNotifyMessageAspect {
    private static Logger logger = LoggerFactory.getLogger(WebsocketOaNotifyMessageAspect.class);

    @Autowired
    private SystemWebSocketHandler systemWebSocketHandler;

    /**
     * 匹配切入点 OaNotifyService
     */
    @Pointcut(value = "target(com.thinkgem.jeesite.modules.oa.service.OaNotifyService)")
    private void oaNotifyServicePointcut() {
    }

    @Pointcut(value = "(execution(* save(*)))")
    private void saveOaNotifyPointcut() {
    }

	/**
     * 拦截
     * OaNotifyService.updateReadFlag(OaNotify oaNotify) 带一个参数的方法 或者
     * OaNotifyService.updateReadFlag(OaNotify oaNotify,User user) 带两个参数的方法
     */
    @Pointcut(value = "(execution(* updateReadFlag(..)))")
    private void updateReadFlagOaNotifyPointcut() {
    }

	/**
     * 通知保存(save)时,判断是否是发布状态,若是,则通过websocket发送该条通知的未读数给通知的接收人列表,并且接收人必须是websocket在线用户 ;
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "oaNotifyServicePointcut() && saveOaNotifyPointcut()")
    public Object saveOaNotifyAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Object arg = pjp.getArgs().length >= 1 ? pjp.getArgs()[0] : null;
        Object result = null;
        try {
            result = pjp.proceed();
            OaNotify oaNotify = (OaNotify) arg;
            //websocket推送给在线用户 未阅读的消息数(该通知必须是发布状态status=1,必须存在接收人);
            if (oaNotify != null && "1".equals(oaNotify.getStatus()) && !Collections3.isEmpty(oaNotify.getOaNotifyRecordList())) {
                List<User> userList = Collections3.extractToList(oaNotify.getOaNotifyRecordList(), "user");
                systemWebSocketHandler.sendOaNotifyCountMessageToUser(userList);
                //app消息推送
                JPushUtils.sendPushToUsers(Collections3.extractToList(userList, "id"), oaNotify.getContent());
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("saveOaNotifyAdvice()", e);
        }
        return result;
    }

    /**
     * 通知被阅读(updateReadFlag)时,需要更新当前用户的未读通知数,通过websocket发送 未读的通知数 给阅读该条通知的人,即系统当前用户;
     * 通知被阅读多次,就执行多次;这里不做颗粒度太细的检测;
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "oaNotifyServicePointcut() && updateReadFlagOaNotifyPointcut()")
    public Object updateReadFlagOaNotifyAdvice(ProceedingJoinPoint pjp) throws Throwable {
        Object arg1 = pjp.getArgs().length >= 1 ? pjp.getArgs()[0] : null;
        Object arg2 = pjp.getArgs().length == 2 ? pjp.getArgs()[1] : UserUtils.getUser();
        Object result = null;
        try {
            result = pjp.proceed();
            OaNotify oaNotify = (OaNotify) arg1;
            User user = (User) arg2;
            //websocket推送 未阅读的消息数 给 当前阅读通知的用户 (该通知必须是发布状态status=1);
            if (oaNotify!=null&&"1".equals(oaNotify.getStatus())) {
                //一条通知可能被查看许多次,这里就websocket通知多次;推送给当前用户,即阅读该通知的用户;
                List<User> userList= Lists.newArrayList(user);
                systemWebSocketHandler.sendOaNotifyCountMessageToUser(userList);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("saveOaNotifyAdvice()", e);
        }
        return result;
    }
}
