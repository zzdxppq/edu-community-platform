package com.educp.common.result;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PageResultTest {

    @Test
    void of_shouldCreatePageResult() {
        List<String> list = Arrays.asList("a", "b", "c");
        PageResult<String> result = PageResult.of(list, 100, 1, 10);

        assertEquals(list, result.getList());
        assertEquals(100, result.getTotal());
        assertEquals(1, result.getPage());
        assertEquals(10, result.getSize());
    }

    @Test
    void of_withEmptyList_shouldReturnEmptyResult() {
        PageResult<String> result = PageResult.of(Collections.emptyList(), 0, 1, 10);

        assertTrue(result.getList().isEmpty());
        assertEquals(0, result.getTotal());
        assertEquals(1, result.getPage());
        assertEquals(10, result.getSize());
    }

    @Test
    void of_shouldPreserveGenericType() {
        List<Integer> list = Arrays.asList(1, 2, 3);
        PageResult<Integer> result = PageResult.of(list, 50, 2, 20);

        assertEquals(Integer.valueOf(1), result.getList().get(0));
        assertEquals(50, result.getTotal());
        assertEquals(2, result.getPage());
        assertEquals(20, result.getSize());
    }
}
