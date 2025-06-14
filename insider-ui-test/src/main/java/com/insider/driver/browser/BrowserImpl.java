package com.insider.driver.browser;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.interactions.Actions;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
public class BrowserImpl implements Browser {

    @Getter
    private RemoteWebDriver remoteWebDriver;

    @Getter
    private List<RemoteWebDriver> remoteWebDrivers = new ArrayList<>();

    private WebDriverWait wait;
    private Actions actions;

    public BrowserImpl(RemoteWebDriver remoteWebDriver) {
        setRemoteWebDriver(remoteWebDriver);
    }

    @Override
    public void setRemoteWebDriver(RemoteWebDriver remoteWebDriver) {
        this.remoteWebDriver = remoteWebDriver;
        this.wait = new WebDriverWait(remoteWebDriver, Duration.ofSeconds(10));
        this.actions = new Actions(remoteWebDriver);
    }

    // Navigation Methods
    @Override
    public void get(String url) {
        log.info("Navigating to URL: {}", url);
        remoteWebDriver.get(url);
    }

    @Override
    public void back() {
        log.info("Navigating back");
        remoteWebDriver.navigate().back();
    }

    @Override
    public <T> T goToPage(T page, String url) {
        get(url);
        waitForPageLoads();
        return page;
    }

    @Override
    public void goToPage(String url) {
        get(url);
        waitForPageLoads();
    }

    @Override
    public String getCurrentUrl() {
        return remoteWebDriver.getCurrentUrl();
    }

    @Override
    public String getTitle() {
        return remoteWebDriver.getTitle();
    }

    // Window/Tab Operations
    @Override
    public Set<String> getWindowHandles() {
        return remoteWebDriver.getWindowHandles();
    }

    @Override
    public String getWindowHandle() {
        return remoteWebDriver.getWindowHandle();
    }

    @Override
    public void tabChange(int index) {
        ArrayList<String> tabs = new ArrayList<>(getWindowHandles());
        remoteWebDriver.switchTo().window(tabs.get(index));
    }

    @Override
    public void close() {
        remoteWebDriver.close();
    }

    @Override
    public void quit() {
        remoteWebDriver.quit();
    }

    // Element Operations
    @Override
    public void click(WebElement element) {
        waitUntilElementClickable(element);
        element.click();
    }

    @Override
    public void sendKeys(WebElement webElement, CharSequence... keys) {
        clear(webElement);
        webElement.sendKeys(keys);
    }

    @Override
    public void sendKeysWithoutClear(WebElement webElement, CharSequence... keys) {
        webElement.sendKeys(keys);
    }

    @Override
    public void clear(WebElement webElement) {
        webElement.clear();
    }

    @Override
    public String getText(WebElement element) {
        return element.getText();
    }

    @Override
    public String getElementAttribute(WebElement element, String attribute) {
        return element.getAttribute(attribute);
    }

    // Element State Checks
    @Override
    public boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isDisplayed(WebElement element, int timeSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(remoteWebDriver, Duration.ofSeconds(timeSeconds));
            return customWait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isElementPresent(WebElement element) {
        try {
            return element != null;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isElementPresent(WebElement element, int timeSeconds) {
        try {
            WebDriverWait customWait = new WebDriverWait(remoteWebDriver, Duration.ofSeconds(timeSeconds));
            customWait.until(ExpectedConditions.visibilityOf(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Wait Operations
    @Override
    public void waitUntilVisibilityOfElement(WebElement element) {
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    @Override
    public void waitUntilInvisibilityOfElement(WebElement element) {
        wait.until(ExpectedConditions.invisibilityOf(element));
    }

    @Override
    public void waitUntilElementClickable(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    @Override
    public void waitForPageLoads() {
        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
                .executeScript("return document.readyState").equals("complete"));
    }

    // Mouse Actions
    @Override
    public void mouseHover(WebElement element) {
        actions.moveToElement(element).perform();
    }

    @Override
    public void doubleClickAction(WebElement element) {
        actions.doubleClick(element).perform();
    }

    @Override
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) remoteWebDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @Override
    public void scrollToTop() {
        ((JavascriptExecutor) remoteWebDriver).executeScript("window.scrollTo(0, 0);");
    }

    @Override
    public void scrollToBottom() {
        ((JavascriptExecutor) remoteWebDriver).executeScript("window.scrollTo(0, document.body.scrollHeight);");
    }

    // Select Operations
    @Override
    public void selectByText(WebElement element, String text) {
        Select select = new Select(element);
        select.selectByVisibleText(text);
    }

    @Override
    public void selectByIndex(WebElement element, int index) {
        Select select = new Select(element);
        select.selectByIndex(index);
    }

    @Override
    public void selectByValue(WebElement element, String value) {
        Select select = new Select(element);
        select.selectByValue(value);
    }

    // Page Actions
    @Override
    public void pageRefresh() {
        remoteWebDriver.navigate().refresh();
    }

    // Sleep Operations
    @Override
    public void sleepSecond(Integer second) {
        try {
            Thread.sleep(second * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sleep interrupted", e);
        }
    }

    @Override
    public void sleepMillis(Integer millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Sleep interrupted", e);
        }
    }
} 