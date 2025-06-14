package com.insider;

import com.insider.annotations.component.GetPage;
import com.insider.cookie.CookieManager;
import com.insider.page.homepage.UseInsiderHomePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;

@SpringBootTest(classes = WebUiTestApplication.class)
public class AbstractTestData {

  @Lazy
  @Autowired
  protected CookieManager cookie;

  @GetPage
  protected UseInsiderHomePage useInsiderHomePage;

}
