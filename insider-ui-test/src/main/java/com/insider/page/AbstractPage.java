package com.insider.page;

import com.insider.driver.browser.Browser;
import com.insider.provider.AppProvider;
import com.insider.provider.WebTestContextProvider;
import org.openqa.selenium.support.PageFactory;
import org.springframework.context.ApplicationContext;

public class AbstractPage {

  protected final Browser browser;
  protected ApplicationContext context = AppProvider.getContext();

  protected AbstractPage() {
    this.browser = WebTestContextProvider.get().getBrowser();
    PageFactory.initElements(browser.getRemoteWebDriver(), this);
  }

}
