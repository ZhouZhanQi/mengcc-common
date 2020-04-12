package com.mengcc.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhouzq
 * @date 2019/10/31
 * @desc 雪花算法生成的Id
 */
@Data
@AllArgsConstructor
public class IdVo {

    /**
     * 时间戳
     */
    private long timeStamp;

    /**
     * 机器ID
     */
    private long workerId;

    /**
     * 毫秒内序列
     */
    private long sequence;

    /**
     * 数据标识
     */
    private long datacenterId;
}
