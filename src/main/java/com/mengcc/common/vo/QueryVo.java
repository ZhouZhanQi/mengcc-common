package com.mengcc.common.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 分页查询请求对象
 *
 * @author zhouzq
 * @date 2017-12-15
 */
public class QueryVo {

    /**
     * 默认的每页显示行数
     */
    public static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 当前页码, 首页为1
     */
    private int page = 1;

    /**
     * 每页要显示的行数, 不设置时默认为20
     */
    private int limit = DEFAULT_PAGE_SIZE;

    /**
     * 排序字段名
     */
    private String sort;

    /**
     * 排序方向 asc,desc
     */
    private String dir;

    public QueryVo() {
    }

    public int getPage() {
        // default to the first page
        return page > 0 ? page : 1;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit > 0 ? limit : DEFAULT_PAGE_SIZE;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    @JsonIgnore
    public int getOffset() {
        return (getPage() - 1) * getLimit();
    }

}
