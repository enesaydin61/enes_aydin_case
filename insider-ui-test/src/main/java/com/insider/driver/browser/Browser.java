package com.insider.driver.browser;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import java.util.List;
import java.util.Set;

public interface Browser {

    RemoteWebDriver getRemoteWebDriver();

    void setRemoteWebDriver(RemoteWebDriver remoteWebDriver);

    List<RemoteWebDriver> getRemoteWebDrivers();

    // Navigation
    void get(String url);

    void back();

    <T> T goToPage(T page, String url);

    void goToPage(String url);

    String getCurrentUrl();

    String getTitle();

    // Window/Tab Operations
    Set<String> getWindowHandles();

    String getWindowHandle();

    void tabChange(int index);

    void close();

    void quit();

    // Element Operations
    void click(WebElement element);

    void sendKeys(WebElement webElement, CharSequence... keys);

    void sendKeysWithoutClear(WebElement webElement, CharSequence... keys);

    void clear(WebElement webElement);

    String getText(WebElement element);

    String getElementAttribute(WebElement element, String attribute);

    // Element State Checks
    boolean isDisplayed(WebElement element);

    boolean isDisplayed(WebElement element, int timeSeconds);

    boolean isElementPresent(WebElement element);

    boolean isElementPresent(WebElement element, int timeSeconds);

    // Wait Operations
    void waitUntilVisibilityOfElement(WebElement element);

    void waitUntilInvisibilityOfElement(WebElement element);

    void waitUntilElementClickable(WebElement element);

    void waitForPageLoads();

    // Mouse Actions
    void mouseHover(WebElement element);

    void doubleClickAction(WebElement element);

    void scrollToElement(WebElement element);

    void scrollToTop();

    void scrollToBottom();

    // Select Operations
    void selectByText(WebElement element, String text);

    void selectByIndex(WebElement element, int index);

    void selectByValue(WebElement element, String value);

    // Page Actions
    void pageRefresh();

    // Sleep Operations
    void sleepSecond(Integer second);

    void sleepMillis(Integer millis);
} 