package com.educp.common.filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;

class RequestIdFilterTest {

    private RequestIdFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private MockFilterChain chain;

    @BeforeEach
    void setUp() {
        filter = new RequestIdFilter();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        chain = new MockFilterChain();
    }

    @Test
    void doFilter_shouldGenerateRequestIdWhenHeaderMissing() throws Exception {
        filter.doFilter(request, response, chain);

        String requestId = response.getHeader("X-Request-Id");
        assertNotNull(requestId);
        assertEquals(8, requestId.length());
    }

    @Test
    void doFilter_shouldUseExistingRequestIdHeader() throws Exception {
        request.addHeader("X-Request-Id", "abc12345");

        filter.doFilter(request, response, chain);

        assertEquals("abc12345", response.getHeader("X-Request-Id"));
    }

    @Test
    void doFilter_shouldGenerateNewIdWhenHeaderIsBlank() throws Exception {
        request.addHeader("X-Request-Id", "   ");

        filter.doFilter(request, response, chain);

        String requestId = response.getHeader("X-Request-Id");
        assertNotNull(requestId);
        assertEquals(8, requestId.length());
        assertNotEquals("   ", requestId);
    }

    @Test
    void doFilter_shouldClearMdcAfterRequest() throws Exception {
        filter.doFilter(request, response, chain);

        assertNull(MDC.get("requestId"));
    }

    @Test
    void doFilter_shouldSetResponseHeader() throws Exception {
        filter.doFilter(request, response, chain);

        assertNotNull(response.getHeader("X-Request-Id"));
    }
}
