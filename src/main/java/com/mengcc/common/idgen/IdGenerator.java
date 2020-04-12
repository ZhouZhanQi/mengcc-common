package com.mengcc.common.idgen;

/**
 * 主键ID生成器
 *
 * @author zhouzq
 * @date 2019-10-31
 */
public interface IdGenerator {

    /**
     * 生成主键ID
     *
     * @return 生成的主键ID
     */
    long generateId();
}
