package com.mengcc.common.idgen;

import com.google.common.base.Preconditions;
import com.mengcc.common.utils.JacksonUtils;
import com.mengcc.common.vo.IdVo;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

/**
 * Twitter的分布式自增ID雪花算法，长度为64位。
 *
 * <pre>
 * SnowFlake的结构如下(每部分用-分开):
 *
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 *
 * 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 *
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)得到的值，
 * 这里的开始时间截固定为EPOCH的值。
 * 41位的时间截，可以使用69年，年T = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69年
 *
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId
 *
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号
 *
 * 加起来刚好64位，为一个Long型。(转换成字符串长度为18)
 *
 * SnowFlake的优点是，整体上按照时间自增排序，并且整个分布式系统内不会产生ID碰撞(由数据中心ID和机器ID作区分)，并且效率较高，经测试，SnowFlake每秒能够产生26万ID左右。
 * </pre>
 *
 * @author zhouzq
 * @date 2019-10-31
 */
public class SnowflakeIdGenerator implements IdGenerator {

    /**
     * 时间偏移量(开始时间截)
     */
    private static final long EPOCH;

    /**
     * 自增量占用位数
     */
    private static final long SEQUENCE_BITS = 12L;

    /**
     * 数据标识id所占的位数
     */
    private static final long DATACENTER_ID_BITS = 5L;

    /**
     * 机器id所占的位数
     */
    private static final long WORKER_ID_BITS = 5L;

    /**
     * 生成自增量的掩码，这里为4095 (0b111111111111=0xfff=4095)
     */
    private static final long SEQUENCE_MASK = -1L ^ (-1L << SEQUENCE_BITS);

    /**
     * 支持的最大数据标识id，结果是31
     */
    private static final long DATACENTER_ID_MAX_VALUE = -1L ^ (-1L << DATACENTER_ID_BITS);

    /**
     * 支持的最大机器id，结果是31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
     */
    private static final long WORKER_ID_MAX_VALUE = -1L ^ (-1L << WORKER_ID_BITS);

    /**
     * 机器id左移位数(左移12位)
     */
    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    /**
     * 数据标识id左移位数(左移5+12=17位)
     */
    private static final long DATACENTER_ID_LEFT_SHIFT_BITS = WORKER_ID_BITS + SEQUENCE_BITS;

    /**
     * 时间戳左移位数(左移5+5+12=22位)
     */
    private static final long TIMESTAMP_LEFT_SHIFT_BITS = DATACENTER_ID_BITS + WORKER_ID_BITS + SEQUENCE_BITS;

    /**
     * 数据标识id，值的范围在[0,31]之间，一般可以设置机房的IDC
     */
    private long datacenterId;
    /**
     * 机器id，值的范围在[0,31]之间，一般可以设置机器编号
     */
    private long workerId;
    /**
     * 毫秒内序列(0~4095)
     */
    private long sequence;
    /**
     * 上次生成ID的时间截
     */
    private long lastTime;


    static {
        // 初始化时间偏移量，固定为2019年10月31日零点
        LocalDateTime dateTime = LocalDateTime.of(2018, Month.AUGUST, 1, 0, 0, 0, 0);
        ZoneId zone = ZoneId.systemDefault();
        EPOCH = dateTime.atZone(zone).toInstant().toEpochMilli();
    }

    public SnowflakeIdGenerator() {
    }

    public SnowflakeIdGenerator(long datacenterId, long workerId) {
        Preconditions.checkArgument(datacenterId >= 0 && datacenterId <= DATACENTER_ID_MAX_VALUE,
                "datacenter Id can't be greater than %d or less than 0", DATACENTER_ID_MAX_VALUE);
        Preconditions.checkArgument(workerId >= 0L && workerId <= WORKER_ID_MAX_VALUE,
                "worker Id can't be greater than %d or less than 0", WORKER_ID_MAX_VALUE);
        this.datacenterId = datacenterId;
        this.workerId = workerId;
    }

    @Override
    public synchronized long generateId() {
        long currentMillis = getCurrentMillis();
        //如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
        Preconditions.checkState(lastTime <= currentMillis,
                "Clock is moving backwards, last time is %d milliseconds, current time is %d milliseconds", lastTime, currentMillis);

        if (lastTime == currentMillis) {
            // 如果是同一时间生成的，则进行毫秒内序列
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)) {
                // 毫秒内序列溢出，阻塞到下一个毫秒，获得新的时间戳
                currentMillis = waitUntilNextTime(currentMillis);
            }
        } else {
            // 时间戳改变，毫秒内序列重置
            sequence = 0;
        }

        // 上次生成ID的时间截
        lastTime = currentMillis;

        // 移位并通过或运算拼到一起组成64位的ID
        return ((currentMillis - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS)
                | (datacenterId << DATACENTER_ID_LEFT_SHIFT_BITS)
                | (workerId << WORKER_ID_LEFT_SHIFT_BITS)
                | sequence;
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     *
     * @param lastTime 上次生成ID的时间截
     * @return 当前时间戳
     */
    private long waitUntilNextTime(final long lastTime) {
        long time = getCurrentMillis();
        while (time <= lastTime) {
            time = getCurrentMillis();
        }
        return time;
    }

    private long getCurrentMillis() {
        ZoneId zone = ZoneId.systemDefault();
        return  LocalDateTime.now().atZone(zone).toInstant().toEpochMilli();
    }


    public static IdVo analysisId(Long snowFlakeId) {
        long sequence = snowFlakeId & SEQUENCE_MASK;

        long workId = (snowFlakeId >>> WORKER_ID_LEFT_SHIFT_BITS) & WORKER_ID_MAX_VALUE;

        long datacenterId = (snowFlakeId >>> DATACENTER_ID_LEFT_SHIFT_BITS) & DATACENTER_ID_MAX_VALUE;

        long timestamp = (snowFlakeId >>> TIMESTAMP_LEFT_SHIFT_BITS) + EPOCH;

        IdVo idVo = new IdVo(timestamp, workId, sequence, datacenterId);

        return idVo;
    }
}
