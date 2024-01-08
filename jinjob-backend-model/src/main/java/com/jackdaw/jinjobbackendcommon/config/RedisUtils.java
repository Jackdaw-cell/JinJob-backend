package com.jackdaw.jinjobbackendcommon.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component("redisUtils")
public class RedisUtils<V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
            }
        }
    }

    public V get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    /**
     * 位图插入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setBitTrue(String key, int value) {
        try {
            redisTemplate.opsForValue().setBit(key, value, true);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    /**
     * 位图插入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean setBitFalse(String key, int value) {
        try {
            redisTemplate.opsForValue().setBit(key, value, false);
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    /**
     * 位图获取
     *
     * @param key   键
     * @return true成功 false失败
     */
    public String getBit(String key,int value) {
        try {
            //统计截止到value天的位图，返回位图列表
            // bitfield key get uvalue 0  （uvalue表示无符号查询第value位的位图值，0是偏移地址即从0开始统计）
            BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create()
                    .get(BitFieldSubCommands.BitFieldType.unsigned(value))
                    .valueAt(0);
            Long bitFields = redisTemplate.opsForValue().bitField(key, bitFieldSubCommands).get(0);
            String binaryString = String.format("%"+value+"s", Long.toBinaryString(bitFields)).replace(' ', '0');
            return binaryString;
        } catch (Exception e) {
            logger.error("获取redisKey:{}失败", key);
            return null;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean setex(String key, V value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error("设置redisKey:{},value:{}失败", key, value);
            return false;
        }
    }

    public long increment(String key, long delta, long time) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        long result = redisTemplate.opsForValue().increment(key, delta);
        if (result == 1) {
            expire(key, time);
        }
        return result;
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
