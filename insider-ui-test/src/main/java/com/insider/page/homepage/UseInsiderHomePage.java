package com.insider.page.homepage;

import com.insider.annotations.component.Page;
import com.insider.page.PageActions;
import com.insider.page.careers.CareersPage;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

@Page
public class UseInsiderHomePage extends PageActions<UseInsiderHomePage> {

  public abstract static class UseInsiderHomePageUrls {

    public static final String INSIDER_HOME_PAGE = "https://useinsider.com";

  }

  @FindBy(css = "#navbarNavDropdown li:nth-child(6)")
  private WebElement navbarDropdownMenuLink;

  @FindBy(xpath = "//div[contains(@class,'show')]//a[text()='Careers']")
  private WebElement careers;

  public UseInsiderHomePage mouseHoverNavbarDropdownMenuLink() {
    browser.mouseHover(navbarDropdownMenuLink);
    return this;
  }

  public CareersPage clickCareers() {
    browser.click(careers);
    return context.getBean(CareersPage.class);
  }

}
