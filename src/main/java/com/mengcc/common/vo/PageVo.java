package com.mengcc.common.vo;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.mengcc.common.exceptions.ConversionException;

import java.util.List;

/**
 * 分页查询结果
 *
 * @author zhouzq
 * @date 2017-12-15
 */
@JsonPropertyOrder({"empty", "start", "pageSize", "totalCount", "currentPageNo", "totalPageCount"})
public class PageVo<T> {

    /**
     * 每页数据容量的默认：20
     */
    private static final int DEFAULT_PAGE_SIZE = 20;

    /**
     * 本页数据在数据库中的起始位置
     */
    private int start;

    private int pageSize;

    /**
     * 数据库中总记录条数
     */
    private int totalCount;

    /**
     * 本页包含的数据
     */
    private List<T> result;

    /**
     * 构造分页查询结果对象
     *
     * @param start      本页数据在数据库中的起始位置
     * @param totalCount 数据库中总记录条数
     * @param pageSize   本页容量
     * @param result     本页包含的数据
     * @param <E>
     * @return
     */
    public static <E> PageVo<E> build(int start, int totalCount, int pageSize, List<E> result) {
        if (pageSize <= 0 || start < 0 || totalCount < 0) {
            throw new ConversionException("构造分页查询结果对象失败: 非法参数");
        }
        PageVo<E> page = new PageVo<>();
        page.setStart(start);
        page.setTotalCount(totalCount);
        page.setPageSize(pageSize);
        page.setResult(result);
        return page;
    }

    public PageVo() {
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    /**
     * 获取总页码
     *
     * @return 总页码
     */
    public int getTotalPageCount() {
        return totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;
    }

    /**
     * 取当前页码,页码默认1
     *
     * @return 当前页码
     */
    public int getCurrentPageNo() {
        return start / pageSize + 1;
    }

    /**
     * 数据是否为空
     *
     * @return 为空返回true, 否则返回false
     */
    public boolean isEmpty() {
        return result == null || result.isEmpty();
    }

    /**
     * 获取任一页第一条数据在整个结果中的位置（每页数据容量使用默认）
     *
     * @param pageNo 页码，从1开始
     * @return 位置序号（从0开始）
     */
    protected static int getStartOfPage(int pageNo) {
        return getStartOfPage(pageNo, DEFAULT_PAGE_SIZE);
    }

    /**
     * 获取任一页第一条数据在整个结果中的位置（每页数据容量使用指定）
     *
     * @param pageNo   页码，从1开始
     * @param pageSize 页面数据容量
     * @return 位置序号（从0开始）
     */
    public static int getStartOfPage(int pageNo, int pageSize) {
        return (pageNo - 1) * pageSize;
    }
}
