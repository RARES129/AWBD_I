package com.awbd.awbd;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mockStatic;

@SpringBootTest
class AwbdApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void mainMethod_ShouldCallSpringApplicationRun() {
		try (MockedStatic<SpringApplication> mockedStatic = mockStatic(SpringApplication.class)) {
			AwbdApplication.main(new String[]{});
			mockedStatic.verify(() -> SpringApplication.run(AwbdApplication.class, new String[]{}));
		}
	}
}
