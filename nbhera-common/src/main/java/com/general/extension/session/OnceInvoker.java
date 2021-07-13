package com.general.extension.session;

/**
 * @param <Resp> 泛型
 * @author xvanning
 * date: 2021/5/30 18:32
 * desc: 一次调用过程抽象
 */
public abstract class OnceInvoker<Resp> {

    /**
     * 调用过程， 维护session栈
     *
     * @return 返回值
     */
    public Resp invoke() {
        try {
            getEntry().entryCountIncrement();
            if (getEntry().getEntryCount() == 1) {
                entry();
            }
            return execute();
        } finally {
            getEntry().entryCountDecrement();

            if (getEntry().getEntryCount() == 0) {
                exit();
            }
        }
    }


    /**
     * Entry定义
     */
    protected interface Entry {
        /**
         * 当前深度
         *
         * @return 当前深度
         */
        int getEntryCount();

        /**
         * 深度增加
         */
        void entryCountIncrement();

        /**
         * 深度减少
         */
        void entryCountDecrement();
    }

    /**
     * 创建新的entry
     *
     * @return 新的entry
     */
    protected static Entry newEntryInstance() {
        return new EntryImpl();
    }

    /**
     * Entry定义实现类
     */
    private static class EntryImpl implements Entry {
        private int count = 0;

        @Override
        public int getEntryCount() {
            return count;
        }

        @Override
        public void entryCountIncrement() {
            count++;
        }

        @Override
        public void entryCountDecrement() {
            count--;
        }
    }

    /**
     * 获取 Entry
     *
     * @return Entry
     */
    protected abstract Entry getEntry();

    /**
     * 执行
     *
     * @return 执行结果
     */
    protected abstract Resp execute();

    /**
     * 获取类名
     *
     * @return 类名
     */
    public abstract Class getClassName();

    /**
     * 创建entry
     */
    protected abstract void entry();

    /**
     * 退出
     */
    protected abstract void exit();
}
