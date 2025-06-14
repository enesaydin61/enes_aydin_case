package com.insider.configuration;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Getter
@Component
public class ScreenshotProperties {

  @Value("#{systemProperties['take.screenshot'] ?: true }")
  private boolean takeScreenshot;

  @Value("#{systemProperties['user.dir'].concat('/temp/screenshot') }")
  private String screenshotPath;


}
