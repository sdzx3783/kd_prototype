package com.kingdee.prototype.util;

import java.util.concurrent.TimeUnit;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedisLockUtil {

    static Logger logger = LoggerFactory.getLogger(RedisLockUtil.class);

    /**
     * 加锁
     *
     * @param lockKey
     * @return
     */
    public static RLock getLock(String lockKey) {
        RedissonClient redissonClient = ApplicationContextUtil.getBean(RedissonClient.class);
        RLock rLock = redissonClient.getLock(lockKey);
        if (rLock != null && rLock.tryLock()) {
            return rLock;
        }
        throw new RuntimeException("无法获取分布式锁");
    }

    /**
     * 加锁
     *
     * @param lockKey
     * @param waitTime 锁等待时间
     * @return
     */
    public static RLock getLock(String lockKey, long waitTime) {
        RedissonClient redissonClient = ApplicationContextUtil.getBean(RedissonClient.class);
        RLock rLock = redissonClient.getLock(lockKey);
        if (rLock != null) {
            try {
                if (rLock.tryLock(waitTime, TimeUnit.MILLISECONDS)) {
                    return rLock;
                }
                logger.warn("分布式锁被其他进程占用，还未释放，lockKey:{}", lockKey);
                throw new RuntimeException("无法获得分布式锁");
            } catch (InterruptedException e) {
                logger.error("获取分布式锁发生了InterruptedException异常", e);
                if (rLock.tryLock()) {
                    return rLock;
                }
                logger.warn("获取分布式锁发生了InterruptedException后尝试获得锁不成功");
                throw new RuntimeException("无法获得分布式锁");
            }
        } else {
            logger.warn("无法获取rLock对象，lockKey:{}", lockKey);
            throw new RuntimeException("无法获得分布式锁");
        }
    }

    /**
     * 解锁
     *
     * @param rLock
     */
    public static void unLock(RLock rLock) {
        if (rLock != null && rLock.isLocked()) {
            rLock.unlock();
        }
        rLock = null;
    }

    /**
     * 关闭RedissonClient
     */
    public static void destory() {
        RedissonClient redissonClient = ApplicationContextUtil.getBean(RedissonClient.class);
        if (redissonClient != null) {
            redissonClient.shutdown();
        }
    }

}
