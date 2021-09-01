package com.msr.better.interceptor;

import com.msr.better.cache.UserBlackListCache;
import com.msr.better.config.BlackListProperties;
import com.msr.better.exception.BusinessException;
import com.msr.better.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 恶意用户检测拦截器
 *
 * @author MaiShuRen
 * @site https://www.maishuren.top
 * @since 2021-09-01 21:49:02
 */
@Component
public class UserInterceptor implements HandlerInterceptor {
    private static final String USER_REQUEST_TIMES_PREFIX = "user_request_times_";
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private UserBlackListCache userBlackListCache;
    @Autowired
    private BlackListProperties blackListProperties;


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    // 手机号正则表达式
    // private static Pattern pattern =
    // Pattern.compile("^1[3|4|5|7|8][0-9]{9}$");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1.获取用户手机号
        String mobile = request.getParameter("mobile");

        // 匹配手机号是否是正常手机号
        // Matcher matcher = pattern.matcher(mobile);
        // // 2. 验证用户是否在黑名单中
        // if (!matcher.find() || userBlackListCache.isIn(mobile))
        // {
        // throw new BusinessException("抢购已经结束啦");
        // }

        // 2. 验证用户是否在黑名单中
        if (userBlackListCache.isIn(mobile)) {
            throw new BusinessException("抢购已经结束啦");
        }

        // 查询该用户访问记录
        List<UserRequestRecord> requestRecords = redisUtil.lrange(USER_REQUEST_TIMES_PREFIX + mobile, 0,
                blackListProperties.getIpBlackTimes() - 1, UserRequestRecord.class);

        // 超过限定时间内的访问频率
        if (requestRecords.size() + 1 >= blackListProperties.getIpBlackTimes() && (System.currentTimeMillis()
                - requestRecords.get(requestRecords.size() - 1).timestamp < blackListProperties.getUserBlackSeconds() * 1000)) {
            // 模拟加入IP黑名单，实际应用时这里要优化入库，下次重启服务时重新加载
            userBlackListCache.addInto(mobile);

            // 清空访问记录缓存
            redisUtil.delete(USER_REQUEST_TIMES_PREFIX + mobile);
            throw new BusinessException("抢购已经结束啦");
        } else {
            UserRequestRecord requestRecord = new UserRequestRecord();
            requestRecord.setMobile(mobile);
            requestRecord.setTimestamp(System.currentTimeMillis());
            // 如果第一次设定访问次数，则增加过期时间
            redisUtil.lpush(USER_REQUEST_TIMES_PREFIX + mobile, requestRecord);
        }
        return true;
    }

    /**
     * 用户访问记录
     *
     * @category @author xiangyong.ding@weimob.com
     * @since 2017年3月30日 下午4:20:47
     */
    public static class UserRequestRecord {
        /**
         * 手机号，唯一标志用户身份
         */
        private String mobile;

        /**
         * 时间戳
         */
        private long timestamp;

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(long timestamp) {
            this.timestamp = timestamp;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("UserRequestRecord [mobile=");
            builder.append(mobile);
            builder.append(", timestamp=");
            builder.append(timestamp);
            builder.append("]");
            return builder.toString();
        }

    }
}
