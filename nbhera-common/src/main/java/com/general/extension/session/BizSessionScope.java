package com.general.extension.session;

import com.general.common.util.SequenceUtils;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * @param <Resp> 泛型
 * @author xvanning
 * date: 2021/5/30 18:32
 * desc: 业务session范围
 */
@Slf4j
public abstract class BizSessionScope<Resp> extends OnceInvoker<Resp> {
    private static ThreadLocal<Map<String, Entry>> SESSION_ENTRY_LOCAL = ThreadLocal.withInitial(Maps::newConcurrentMap);

    private String sessionId = SequenceUtils.generateUniqueId();

    private String scenario;

    private String bizCode;

    private Map<String, Object> bizData;

    /**
     * 构造函数
     *
     * @param bizCode  bizCode
     * @param scenario scenario
     * @param bizData  bizData
     */
    public BizSessionScope(String bizCode, String scenario, Map<String, Object> bizData) {
        this.bizCode = bizCode;
        this.scenario = scenario;
        this.bizData = bizData;
    }

    @Override
    protected Entry getEntry() {
        Map<String, Entry> sessionEntryMap = SESSION_ENTRY_LOCAL.get();
        Entry entry = sessionEntryMap.get(sessionId);
        if (null == entry) {
            entry = newEntryInstance();
            sessionEntryMap.put(sessionId, entry);
            SessionCacheData sessionCacheData = BizSession.SESSION_THREAD_LOCAL.get();
            sessionCacheData.pushSessionId(sessionId);
        }
        return entry;
    }

    /**
     * 创建session
     *
     * @param <T> 泛型
     * @return session
     */
    protected <T extends BizSession> T createSession() {
        log.info("[EXT_CR]||{}||{}||{}||{}|", sessionId, bizCode, scenario, bizData);

        BizSession bizSession = BizSession.create(sessionId, bizCode, scenario, bizData);
        bizSession.setBizCode(bizCode);
        return (T) bizSession;
    }

    /**
     * 销毁session
     */
    protected void destroySession() {
        BizSession.destroySession(sessionId);
        SESSION_ENTRY_LOCAL.get().remove(sessionId);
    }


    @Override
    public Class getClassName() {
        return BizSessionScope.class;
    }

    @Override
    protected void entry() {
        createSession();
    }

    @Override
    protected void exit() {
        destroySession();
        SessionCacheData sessionCacheData = BizSession.SESSION_THREAD_LOCAL.get();
        BizSession bizSession = sessionCacheData.popThenPeek();
        log.info("|EXT_EXIT| " + this.sessionId);
    }
}
