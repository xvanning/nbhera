package com.general.test;

/**
 * @author xvanning
 * date: 2021/7/1 0:37
 * desc: 测试模式
 */
public class SessionContext {

    /**
     * 本地线程变量，存储session
     */
    private static final ThreadLocal<Session> threadLocal = new ThreadLocal<>();

    /**
     * 创建session
     *
     * @return session
     */
    public static Session createSession() {
        return createSession(false);
    }

    /**
     * 创建session
     *
     * @param test 是否测试模式
     * @return session
     */
    private static Session createSession(boolean test) {
        Session session = threadLocal.get();
        if (null == session) {
            session = new Session(test);
            threadLocal.set(session);
        }
        return session;
    }

    /**
     * 获取session
     *
     * @return 获取session
     */
    public static Session getSession() {
        return threadLocal.get();
    }

    /**
     * 清空当前session
     */
    public static void remove() {
        threadLocal.remove();
    }

    /**
     * 是否测试模式
     *
     * @return 是否测试模式
     */
    public static boolean isTest() {
        Session session = getSession();
        return null == session ? false : session.isTest();
    }

}
