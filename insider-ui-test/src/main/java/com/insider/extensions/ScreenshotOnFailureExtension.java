package com.insider.extensions;

import com.insider.configuration.ScreenshotProperties;
import com.insider.provider.AppProvider;
import com.insider.provider.WebTestContextProvider;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

  private ScreenshotProperties screenshotProperties;

  private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern(
      "yyyy-MM-dd_HH-mm-ss");

  @Override
  public void afterTestExecution(ExtensionContext extensionContext) {
    var appContext = AppProvider.getContext();
    screenshotProperties = appContext.getBean(ScreenshotProperties.class);

    if (screenshotProperties.isTakeScreenshot() && extensionContext.getExecutionException()
        .isPresent()) {
      takeScreenshot(extensionContext);
    }
  }

  private void takeScreenshot(ExtensionContext context) {
    try {
      WebDriver driver = WebTestContextProvider.get().getBrowser().getRemoteWebDriver();

      createScreenshotDirectory();

      String fileName = generateScreenshotFileName(context);
      Path screenshotPath = Paths.get(screenshotProperties.getScreenshotPath(), fileName);

      File sourceFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
      Files.copy(sourceFile.toPath(), screenshotPath);

      log.info("Screenshot taken: {}", screenshotPath.toAbsolutePath());

    } catch (Exception e) {
      log.error("Error occurred while taking screenshot: {}", e.getMessage(), e);
    }
  }

  private void createScreenshotDirectory() throws IOException {
    Path screenshotDir = Paths.get(screenshotProperties.getScreenshotPath());
    if (!Files.exists(screenshotDir)) {
      Files.createDirectories(screenshotDir);
    }
  }

  private String generateScreenshotFileName(ExtensionContext context) {
    String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
    String className = context.getTestClass()
        .map(Class::getSimpleName)
        .orElse("UnknownClass");
    String methodName = context.getTestMethod()
        .map(method -> method.getName())
        .orElse("unknownMethod");

    return String.format("FAILED_%s_%s_%s.png", className, methodName, timestamp);
  }
}