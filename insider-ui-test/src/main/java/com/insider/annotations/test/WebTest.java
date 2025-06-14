package com.insider.annotations.test;

import com.insider.extensions.WebDriverExtension;
import com.insider.extensions.ScreenshotOnFailureExtension;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.OutputCaptureExtension;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith({ScreenshotOnFailureExtension.class, WebDriverExtension.class, OutputCaptureExtension.class})
@SpringBootTest
@Test
public @interface WebTest {

}
