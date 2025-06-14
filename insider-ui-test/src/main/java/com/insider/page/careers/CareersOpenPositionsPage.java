package com.insider.page.careers;

import com.insider.annotations.component.Page;
import com.insider.page.PageActions;
import com.insider.page.homepage.UseInsiderHomePage.UseInsiderHomePageUrls;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Page
public class CareersOpenPositionsPage extends PageActions<CareersOpenPositionsPage> {

  public static final String ISTANBUL_TURKIYE = "Istanbul, Turkiye";
  public static final String QUALITY_ASSURANCE = "Quality Assurance";

  @FindBy(className = "select2-selection__arrow")
  private List<WebElement> selection;

  @FindBy(xpath = "//ul[@id='select2-filter-by-location-results']//li[@role='option']")
  private List<WebElement> locationOptions;

  @FindBy(xpath = "//ul[@id='select2-filter-by-department-results']//li[@role='option']")
  private List<WebElement> departmentOptions;

  @FindBy(className = "position-department")
  private List<WebElement> positionDepartments;

  @FindBy(className = "position-location")
  private List<WebElement> positionLocations;

  @FindBy(xpath = "//a[text()='View Role']")
  private List<WebElement> viewRole;

  public CareersOpenPositionsPage selectLocation(String location) {
    browser.sleepSecond(10);
    browser.click(selection.getFirst());
    browser.sleepSecond(1);

    locationOptions.stream()
        .filter(option -> option.getText().equals(location))
        .findFirst()
        .ifPresent(browser::click);
    return this;
  }

  public CareersOpenPositionsPage selectDepartment(String department) {
    browser.sleepSecond(1);
    browser.click(selection.getLast());
    browser.sleepSecond(1);

    departmentOptions.stream()
        .filter(option -> option.getText().equals(department))
        .findFirst()
        .ifPresent(browser::click);
    return this;
  }

  public boolean allPositionDepartments(String department) {
    return positionDepartments.stream()
        .allMatch(element -> element.getText().equals(department));
  }

  public boolean allPositionLocations(String locations) {
    return positionLocations.stream()
        .allMatch(element -> element.getText().equals(locations));
  }

  public String clickViewRole(int index) {
    ((JavascriptExecutor) browser.getRemoteWebDriver()).executeScript(
        "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", positionDepartments.get(index));
    browser.mouseHover(positionDepartments.get(index));
    var href = browser.getElementAttribute(viewRole.get(index), "href");
    browser.click(viewRole.get(index));
    return href;
  }
}
