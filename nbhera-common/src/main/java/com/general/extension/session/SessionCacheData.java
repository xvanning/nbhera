package com.general.extension.session;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;
import java.util.Stack;

/**
 * @author xvanning
 * date: 2021/5/29 22:52
 * desc: 业务身份缓存数据
 */
@Data
public class SessionCacheData {

    /**
     * 当前 session Id
     */
    private String currentSessionId;

    /**
     * 当前session
     */
    private BizSession currentBizSession;

    /**
     * 当前线程的session栈
     */
    private Stack<String> sessionIdStack = new Stack<>();

    /**
     * session 的缓存map
     */
    private Map<String, BizSession> sessionMap = Maps.newConcurrentMap();

    /**
     * 把指定 sessionId 到 session 信息放入栈顶
     *
     * @param sessionId sessionId
     */
    public void pushSessionId(String sessionId) {
        this.sessionIdStack.push(sessionId);
        this.currentSessionId = sessionId;
        this.currentBizSession = this.sessionMap.get(this.currentSessionId);
    }

    /**
     * 删除栈顶的值。再返回当前栈顶的值
     *
     * @return 栈顶元素
     */
    public BizSession popThenPeek() {
        if (!this.sessionIdStack.isEmpty()) {
            this.sessionIdStack.pop();
        }
        if (!this.sessionIdStack.isEmpty()) {
            this.currentSessionId = this.sessionIdStack.peek();
            this.currentBizSession = this.sessionMap.get(this.currentSessionId);
        } else {
            this.currentSessionId = null;
            this.currentBizSession = null;
        }

        return this.currentBizSession;
    }

    /**
     * 根据sessionId 移除session
     *
     * @param sessionId sessionID
     */
    public void removeSessionById(String sessionId) {
        this.sessionMap.remove(sessionId);
        if (sessionId.equals(this.currentSessionId)) {
            this.currentSessionId = null;
            this.currentBizSession = null;
        }
    }


    /**
     * 保存session信息
     *
     * @param sessionId  sessionId
     * @param bizSession session信息
     */
    public void storeBizSession(String sessionId, BizSession bizSession) {
        this.sessionMap.put(sessionId, bizSession);
        if (sessionId.equals(this.currentSessionId)) {
            this.currentBizSession = bizSession;
        }
        if (this.sessionMap.size() == 1 && this.currentBizSession == null) {
            this.currentSessionId = sessionId;
            this.currentBizSession = bizSession;
        }
    }
}
