package com.educp.common.result;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PageRequestTest {

    @Test
    void defaultValues_shouldBePage1Size10() {
        PageRequest request = new PageRequest();
        assertEquals(1, request.getPage());
        assertEquals(10, request.getSize());
    }

    @Test
    void toPage_shouldConvertToMyBatisPlusPage() {
        PageRequest request = new PageRequest();
        request.setPage(3);
        request.setSize(20);

        var page = request.toPage();
        assertEquals(3, page.getCurrent());
        assertEquals(20, page.getSize());
    }

    @Test
    void toPage_withDefaults_shouldReturnPage1Size10() {
        PageRequest request = new PageRequest();
        var page = request.toPage();
        assertEquals(1, page.getCurrent());
        assertEquals(10, page.getSize());
    }
}
