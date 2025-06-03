package com.awbd.awbd.aspect;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LoggingAspectTest {

    private LoggingAspect loggingAspect;
    private ProceedingJoinPoint joinPoint;

    @BeforeEach
    void setup() {
        loggingAspect = new LoggingAspect();
        joinPoint = mock(ProceedingJoinPoint.class);
    }

    @Test
    void testLogAroundMethod_returnsResult() throws Throwable {
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("String someMethod()");
        when(joinPoint.getSignature()).thenReturn(signature);

        when(joinPoint.getArgs()).thenReturn(new Object[] {"arg1", 123});
        when(joinPoint.proceed()).thenReturn("result");

        Object returned = loggingAspect.logAroundMethod(joinPoint);

        assertEquals("result", returned);
        verify(joinPoint, times(1)).proceed();
    }

    @Test
    void testLogAroundMethod_throwsException() throws Throwable {
        Signature signature = mock(Signature.class);
        when(signature.toShortString()).thenReturn("void someMethod()");
        when(joinPoint.getSignature()).thenReturn(signature);

        when(joinPoint.getArgs()).thenReturn(new Object[0]);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("failure"));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> loggingAspect.logAroundMethod(joinPoint));
        assertEquals("failure", thrown.getMessage());

        verify(joinPoint, times(1)).proceed();
    }
}
