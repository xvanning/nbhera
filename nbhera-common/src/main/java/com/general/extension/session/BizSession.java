package com.general.extension.session;

import com.general.common.exception.SystemException;
import com.general.common.util.SequenceUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @author xvanning
 * date: 2021/5/29 22:51
 * desc: 业务身份session
 */
@Data
@AllArgsConstructor
public class BizSession {
    protected static final ThreadLocal<SessionCacheData> SESSION_THREAD_LOCAL = ThreadLocal.withInitial(SessionCacheData::new);

    /**
     * sessionId
     */
    private String sessionId;

    /**
     * 场景
     */
    private String scenario;

    /**
     * 业务code
     */
    private String bizCode;

    /**
     * 业务身份参数
     */
    private Map<String, Object> bizData;

    /**
     * 创建session
     *
     * @param sessionId sessionId
     * @param bizCode   业务code
     * @param scenario  场景
     * @param bizData   身份参数
     * @return BizSession
     */
    protected static BizSession create(String sessionId, String bizCode, String scenario, Map<String, Object> bizData) {
        if (StringUtils.isBlank(sessionId)) {
            sessionId = SequenceUtils.generateUniqueId();
        }
        BizSession bizSession = new BizSession(sessionId, bizCode, scenario, bizData);
        bizSession.store(sessionId, bizSession);
        return bizSession;
    }

    /**
     * 保存session
     *
     * @param sessionId  sessionId
     * @param bizSession 业务身份
     */
    private void store(String sessionId, BizSession bizSession) {
        SessionCacheData sessionCacheData = SESSION_THREAD_LOCAL.get();
        sessionCacheData.storeBizSession(sessionId, bizSession);
    }

    /**
     * 当前线程是否有业务session
     *
     * @return 业务session
     */
    public static Boolean hasSession() {
        SessionCacheData sessionCacheData = SESSION_THREAD_LOCAL.get();
        BizSession currentBizSession = sessionCacheData.getCurrentBizSession();
        if (currentBizSession == null) {
            Map<String, BizSession> sessionMap = sessionCacheData.getSessionMap();
            return !sessionMap.isEmpty();
        }
        return true;
    }

    /**
     * 获取当前的 session
     *
     * @return 当前的 session
     */
    public static BizSession currentSession() {
        SessionCacheData sessionCacheData = SESSION_THREAD_LOCAL.get();
        BizSession currentBizSession = sessionCacheData.getCurrentBizSession();

        if (currentBizSession == null) {
            Map<String, BizSession> sessionMap = sessionCacheData.getSessionMap();
            if (sessionMap != null && sessionMap.size() == 1) {
                return sessionMap.values().iterator().next();
            } else {
                throw new SystemException("SESSION_REQUIRED", "未识别到业务");
            }
        }

        return currentBizSession;
    }

    /**
     * 根据 sessionId 获取 业务身份session
     *
     * @param sessionId sessionId
     * @return 业务身份session
     */
    public static BizSession getBizSessionById(String sessionId) {
        SessionCacheData sessionCacheData = SESSION_THREAD_LOCAL.get();
        return sessionCacheData.getSessionMap().get(sessionId);
    }

    /**
     * 获取最后的业务session场景
     *
     * @return 场景code
     */
    public String getLastBizSessionScenario() {
        SessionCacheData sessionCacheData = SESSION_THREAD_LOCAL.get();
        return sessionCacheData.getCurrentBizSession().getScenario();
    }

    /**
     * 获取当前业务身份的场景
     *
     * @return 当前业务身份的场景
     */
    public String getCurrentBizSessionScenario() {
        SessionCacheData sessionCacheData = SESSION_THREAD_LOCAL.get();
        return sessionCacheData.getCurrentBizSession().getScenario();
    }

    /**
     * 根据sessionId 销毁session
     *
     * @param sessionId sessionId
     */
    public static void destroySession(String sessionId) {
        BizSession bizSession = getBizSessionById(sessionId);
        if (bizSession == null) {
            return;
        }
        SESSION_THREAD_LOCAL.get().removeSessionById(sessionId);
    }

    /**
     * 加入到 SessionCacheData 的session栈中
     *
     * @param sessionId sessionId
     */
    public void pushSessionId(String sessionId) {
        SESSION_THREAD_LOCAL.get().pushSessionId(sessionId);
    }

    /**
     * 删除栈顶的值。再返回当前栈顶的值
     *
     * @return 栈顶元素
     */
    public BizSession popThenPeek() {
        return SESSION_THREAD_LOCAL.get().popThenPeek();
    }
}
