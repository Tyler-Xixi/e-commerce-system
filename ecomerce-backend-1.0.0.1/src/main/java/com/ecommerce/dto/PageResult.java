package com.ecommerce.dto;

import org.springframework.data.domain.Page;

import java.util.List;

public class PageResult<T> {
    private List<T> records;
    private List<T> content;
    private long total;
    private long totalElements;
    private int page;
    private int size;
    private int pages;
    private int totalPages;

    public PageResult() {
    }

    public PageResult(List<T> records, long total, int page, int size, int pages) {
        this.records = records;
        this.content = records;
        this.total = total;
        this.totalElements = total;
        this.page = page;
        this.size = size;
        this.pages = pages;
        this.totalPages = pages;
    }

    public static <T> PageResult<T> of(Page<T> page) {
        return new PageResult<>(
            page.getContent(),
            page.getTotalElements(),
            page.getNumber() + 1,
            page.getSize(),
            page.getTotalPages()
        );
    }

    public static <T> PageResult<T> of(List<T> records, long total, int page, int size) {
        int pages = size <= 0 ? 0 : (int) Math.ceil((double) total / size);
        return new PageResult<>(records, total, page, size, pages);
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
        this.content = records;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
        this.records = content;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
        this.totalElements = total;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
        this.total = totalElements;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
        this.totalPages = pages;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        this.pages = totalPages;
    }
}
