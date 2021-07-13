package com.general.common.thread;

import com.general.common.util.TracedUtils;
import com.general.extension.session.BizSession;
import com.general.extension.session.BizSessionScope;
import com.google.common.collect.Maps;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author xvanning
 * date: 2021/5/29 21:40
 * desc: 传递线程上下文的线程池，会传扩展点session
 */
@NoArgsConstructor
@Slf4j
public final class ExecutorServiceBuilder {

    /**
     * 创建过的线程池缓存，结合定时器，1分钟输出一次线程池负载信息
     */
    private static final Map<String, ThreadPoolExecutor> POOL_CACHE = Maps.newConcurrentMap();

    /**
     * 当前容器是否支持调用链
     */
    private static final Boolean SUPPORT_TRACE = TracedUtils.getInstance().getSupportTraced();

    /**
     * 定时任务，用来输出线程池使用情况
     */
    private static ScheduledExecutorService scheduled;

    /**
     * 是否正在使用
     */
    private static Boolean running = false;

    /**
     * 线程池名称
     */
    private String threadPoolName;

    /**
     * 默认核心线程数量等于处理器核数
     */
    private Integer corePoolSize = Runtime.getRuntime().availableProcessors();

    /**
     * 默认线程池最大线程数为核心线程数5倍
     */
    private Integer maximumPoolSize = this.corePoolSize * 5;

    /**
     * 默认的队列数为100
     */
    private Integer maxQueueSize = 100;

    /**
     * 线程回收等待时间，默认为3分钟
     */
    private Integer keepAliveTimeSeconds = 3 * 60;

    /**
     * 默认使用的拒绝策略，默认直接抛异常
     */
    private RejectedExecutionHandler rejectedHandler = new ThreadPoolExecutor.AbortPolicy();

    /**
     * 工作队列
     */
    private BlockingQueue<Runnable> workQueue;

    /**
     * 构造函数
     *
     * @param threadPoolName 线程池名称
     */
    private ExecutorServiceBuilder(String threadPoolName) {
        this.threadPoolName = threadPoolName;
    }

    /**
     * 设置线程池名称
     *
     * @param threadPoolName threadPoolName
     * @return ExecutorServiceBuilder
     */
    public ExecutorServiceBuilder setThreadPoolName(String threadPoolName) {
        this.threadPoolName = threadPoolName;
        return this;
    }

    /**
     * 设置 核心线程数
     *
     * @param corePoolSize 核心线程数
     * @return ExecutorServiceBuilder
     */
    public ExecutorServiceBuilder setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
        return this;
    }

    /**
     * 设置 最大线程数
     *
     * @param maximumPoolSize 核心线程数
     * @return ExecutorServiceBuilder
     */
    public ExecutorServiceBuilder setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
        return this;
    }

    /**
     * 设置 工作队列
     *
     * @param workQueue 工作队列
     * @return ExecutorServiceBuilder
     */
    public ExecutorServiceBuilder setWorkQueueSize(BlockingQueue<Runnable> workQueue) {
        this.workQueue = workQueue;
        return this;
    }

    /**
     * 设置 最大队列大小
     *
     * @param maxQueueSize 最大队列大小
     * @return ExecutorServiceBuilder
     */
    public ExecutorServiceBuilder setWorkQueueSize(int maxQueueSize) {
        this.maxQueueSize = maxQueueSize;
        return this;
    }

    /**
     * 设置 活跃线程数存活时间
     *
     * @param keepAliveTimeSeconds 活跃线程数存活时间
     * @return ExecutorServiceBuilder
     */
    public ExecutorServiceBuilder setKeepAliveTimeSeconds(int keepAliveTimeSeconds) {
        this.keepAliveTimeSeconds = keepAliveTimeSeconds;
        return this;
    }

    /**
     * 设置 拒绝策略
     *
     * @param rejectedHandler 拒绝策略
     * @return ExecutorServiceBuilder
     */
    public ExecutorServiceBuilder setKeepAliveTimeSeconds(RejectedExecutionHandler rejectedHandler) {
        this.rejectedHandler = rejectedHandler;
        return this;
    }

    /**
     * 创建线程池
     *
     * @param threadPoolName 线程池名称
     * @return 线程池
     */
    private static ExecutorServiceBuilder create(String threadPoolName) {
        return new ExecutorServiceBuilder(threadPoolName);
    }

    /**
     * 构造线程池
     *
     * @return 线程池
     */
    public ThreadPoolExecutor build() {
        this.workQueue = Objects.nonNull(this.workQueue) ? this.workQueue : new LinkedBlockingDeque<>(this.maxQueueSize);
        this.rejectedHandler = Objects.nonNull(rejectedHandler) ? this.rejectedHandler : new ThreadPoolExecutor.AbortPolicy();
        ThreadPoolExecutor executor = this.createThreadPoolExecutor(this, getThreadFactory(this.threadPoolName));
        POOL_CACHE.put(this.threadPoolName, executor);
        return executor;
    }

    /**
     * 自定义线程池
     *
     * @param builder       构造器
     * @param threadFactory 线程工厂
     * @return 线程池
     */
    private ThreadPoolExecutor createThreadPoolExecutor(ExecutorServiceBuilder builder, ThreadFactory threadFactory) {
        // 在第一次调用的时候开启定时器
        if (!running) {
            synchronized (ExecutorServiceBuilder.class) {
                if (!running) {
                    scheduled = Executors.newSingleThreadScheduledExecutor();
                    scheduled.scheduleAtFixedRate(this::getThreadPoolMonitor, 1, 1, TimeUnit.MINUTES);
                    running = true;
                }
            }
        }
        return new ThreadPoolExecutor(builder.corePoolSize,
                builder.maximumPoolSize,
                builder.keepAliveTimeSeconds,
                TimeUnit.SECONDS, builder.workQueue,
                threadFactory, builder.rejectedHandler) {
            @Override
            public void execute(final Runnable command) {
                // 这边要换成对应的调用链中间件的上下文
                final Object rpcContext = SUPPORT_TRACE ? new Object() : null;
                // 获取当前线程扩展点session
                final BizSession session = this.currentSession();
                // 提交任务的调用方线程Id
                final long threadId = Thread.currentThread().getId();
                super.execute(() -> {
                    try {
                        if (SUPPORT_TRACE) {
//                            EagleEye.setRpcContext(rpcContext);
                        }
                        this.run(command, session);
                    } catch (RuntimeException exception) {
                        log.error("|THREAD_POOL| " + builder.threadPoolName + " | error", exception);
                        throw exception;
                    } finally {
                        // 1、如果任务线程就是调用方线程，那说明这个Executor是不开线程直接执行任务的，直接执行run方法
                        // 这个时候不能 clearRpcContext，因为调用方线程执行这个任务之后，可能还有后续链路执行
                        //  2、如果任务线程不是调用方线程，那就是普通的线程池模式，每次执行完，需要 clearRpcContext
                        if (SUPPORT_TRACE && threadId != Thread.currentThread().getId()) {
//                            EagleEye.clearRpcContext(rpcContext);
                        }
                    }
                });
            }

            /**
             * 提交线程执行过程，如果任务提交线程有 BizSession，则在子线程中也创建
             *
             * @param command 提交线程
             * @param session 扩展点session
             */
            private void run(final Runnable command, BizSession session) {
                if (session != null) {
                    new BizSessionScope<Void>(session.getBizCode(),
                            session.getScenario(),
                            null) {

                        @Override
                        protected Void execute() {
                            command.run();
                            return null;
                        }
                    }.invoke();
                } else {
                    command.run();
                }
            }

            /**
             * 获取当前线程扩展点session
             *
             * @return 扩展点session
             */
            private BizSession currentSession() {
                return BizSession.hasSession() ? BizSession.currentSession() : null;
            }
        };
    }

    /**
     * 线程池监控，1分钟输出1次线程池使用情况
     */
    private void getThreadPoolMonitor() {
        if (POOL_CACHE.isEmpty()) {
            return;
        }
        for (Map.Entry<String, ThreadPoolExecutor> entry : POOL_CACHE.entrySet()) {
            ThreadPoolExecutor executor = entry.getValue();
            log.info("[THREAD_MONITOR]|{}||{}||{}||{}||{}|", entry.getKey(),
                    executor.getCorePoolSize(),
                    executor.getPoolSize(),
                    executor.getActiveCount(),
                    executor.getQueue().size());
        }

    }

    /**
     * 获取线程工厂
     *
     * @param threadPoolName 线程名称
     * @return 线程工厂
     */
    private ThreadFactory getThreadFactory(String threadPoolName) {
        return new ThreadFactory() {
            private final AtomicInteger threadNumber = new AtomicInteger(0);

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, threadPoolName + "-" + threadNumber.incrementAndGet());
            }
        };
    }
}
