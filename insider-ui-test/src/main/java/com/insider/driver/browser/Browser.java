package com.insider.driver.browser;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.util.List;
import java.util.Set;

public interface Browser {

    RemoteWebDriver getRemoteWebDriver();

    void setRemoteWebDriver(RemoteWebDriver remoteWebDriver);

    void get(String url);

    String getCurrentUrl();

    Set<String> getWindowHandles();

    void tabChange(int index);

    void click(WebElement element);

    void sendKeys(WebElement webElement, CharSequence... keys);

    void clear(WebElement webElement);

    String getElementAttribute(WebElement element, String attribute);

    boolean isDisplayed(WebElement element);

    void waitUntilElementClickable(WebElement element);

    void waitForPageLoads();

    void mouseHover(WebElement element);

    void selectByText(WebElement element, String text);

    void sleepSecond(Integer second);

}