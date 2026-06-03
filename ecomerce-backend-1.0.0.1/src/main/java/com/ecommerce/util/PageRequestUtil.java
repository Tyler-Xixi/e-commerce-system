package com.ecommerce.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public final class PageRequestUtil {
    private static final int DEFAULT_PAGE = 1;
    private static final int DEFAULT_SIZE = 10;
    private static final int MAX_SIZE = 1000;

    private PageRequestUtil() {
    }

    public static Pageable of(Integer page, Integer size) {
        return of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    }

    public static Pageable of(Integer page, Integer size, Sort sort) {
        int pageNumber = page == null || page < 1 ? DEFAULT_PAGE : page;
        int pageSize = size == null || size < 1 ? DEFAULT_SIZE : Math.min(size, MAX_SIZE);
        return PageRequest.of(pageNumber - 1, pageSize, sort);
    }
}
