package com.awbd.awbd.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

class SecurityUtilTest {

    private SecurityContext securityContextMock;

    @BeforeEach
    void setup() {
        securityContextMock = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContextMock);
    }

    @AfterEach
    void teardown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getSessionUsername_returnsUsername_whenAuthenticated() {
        Authentication authenticationMock = mock(Authentication.class);
        when(authenticationMock.getName()).thenReturn("user123");
        when(securityContextMock.getAuthentication()).thenReturn(authenticationMock);

        String username = SecurityUtil.getSessionUsername();

        assertEquals("user123", username);
    }

    @Test
    void getSessionUsername_returnsNull_whenAnonymousAuthentication() {
        Authentication anonymousAuthMock = mock(AnonymousAuthenticationToken.class);
        when(securityContextMock.getAuthentication()).thenReturn(anonymousAuthMock);

        String username = SecurityUtil.getSessionUsername();

        assertNull(username);
    }
}

