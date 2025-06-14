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
public class CareersPage extends PageActions<CareersPage> {

  public abstract static class CareersPageUrls {

    public static final String CAREERS = UseInsiderHomePageUrls.INSIDER_HOME_PAGE + "/careers";
    public static final String CAREERS_QUALITY_ASSURANCE =
        UseInsiderHomePageUrls.INSIDER_HOME_PAGE + "/careers/quality-assurance";

  }

  public static final Set<String> LOCATIONS = new HashSet<>(Arrays.asList(
      "Singapore",
      "Taipei",
      "Manila",
      "Kuala Lumpur",
      "Dubai",
      "Amsterdam",
      "Tokyo",
      "London",
      "Seoul",
      "Lima",
      "Buenos Aires",
      "Helsinki",
      "Istanbul",
      "New York",
      "Warsaw",
      "Sao Paulo",
      "Ho Chi Minh City",
      "Bogota",
      "Bangkok",
      "Santiago",
      "Jakarta",
      "Ankara",
      "Paris",
      "Sydney",
      "Mexico City"
  ));

  @FindBy(css = "#career-find-our-calling .loadmore")
  private WebElement loadMore;

  @FindBy(css = "#career-find-our-calling .job-image")
  private List<WebElement> jobImages;

  @FindBy(css = "#career-our-location a:nth-child(2)")
  private WebElement nextButton;

  @FindBy(css = "#career-our-location .location-info p")
  private List<WebElement> locations;

  @FindBy(css = ".swiper-slide-active")
  private WebElement activeSwiperSlide;

  @FindBy(className = "swiper-slide-active")
  private WebElement swiperSlideActive;

  @FindBy(xpath = "//a[text()='See all QA jobs']")
  private WebElement seeAllQAJobs;

  public int getJobImageSize() {
    return jobImages.size();
  }

  //TODO: JS action lar ayrı bir kütüphane haline getirilebilir.
  public CareersPage scrollLoadMoreButton() {
    ((JavascriptExecutor) browser.getRemoteWebDriver())
        .executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
            loadMore);
    return this;
  }

  public CareersPage clickLoadMoreButton() {
    browser.click(loadMore);
    browser.sleepSecond(2);
    return this;
  }

  public boolean isDisplayedLoadMoreButton() {
    return browser.isDisplayed(loadMore);
  }

  public List<String> getLocationTexts() {
    return locations.stream()
        .map(WebElement::getText)
        .collect(Collectors.toList());
  }

  public Set<String> allLocationTexts() {
    browser.waitForPageLoads();
    ((JavascriptExecutor) browser.getRemoteWebDriver())
        .executeScript("arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});",
            nextButton);

    return IntStream.range(0, 23)
        .mapToObj(i -> {
          browser.sleepSecond(1);
          browser.click(nextButton);
          return getLocationTexts();
        })
        .flatMap(List::stream)
        .filter(text -> text != null && !text.trim().isEmpty())
        .map(this::normalizeText)
        .collect(Collectors.toSet());
  }

  private String normalizeText(String input) {
    if (input == null) {
      return null;
    }
    String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
    return normalized.replaceAll("\\p{M}", "").trim();
  }

  public CareersPage scrollToActiveSwiperSlide() {
    ((JavascriptExecutor) browser.getRemoteWebDriver()).executeScript(
        "arguments[0].scrollIntoView({block: 'center', inline: 'nearest'});", activeSwiperSlide);
    browser.sleepSecond(1);
    return this;
  }

  public boolean isDisplayedSliderSwipeActive() {
    return browser.isDisplayed(swiperSlideActive);
  }

  public CareersOpenPositionsPage clickSeeAllQAJobs() {
    browser.click(seeAllQAJobs);
    return context.getBean(CareersOpenPositionsPage.class);
  }

}
