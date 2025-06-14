package com.insider.cookie;

import com.insider.provider.WebTestContextProvider;
import jakarta.annotation.PostConstruct;
import java.lang.reflect.Field;
import java.util.Date;
import lombok.SneakyThrows;
import org.apache.commons.lang3.time.DateUtils;
import org.openqa.selenium.Cookie;
import org.springframework.stereotype.Component;

@Component
public class CookieManager {

  @PostConstruct
  public void init() {
    new CookieBuilder().build(this);
  }

  @Cookies(key = "viewed_cookie_policy", value = "true")
  private Cookie viewedCookiePolicy;

  @Cookies(key = "cookielawinfo-checkbox-advertisement", value = "true")
  private Cookie cookieInfoCheckboxAdvertisement;

  @Cookies(key = "cookielawinfo-checkbox-analytics", value = "true")
  private Cookie cookieInfoCheckboxAnalytics;

  @Cookies(key = "cookielawinfo-checkbox-functional", value = "true")
  private Cookie cookieInfoCheckboxFunctional;

  @Cookies(key = "cookielawinfo-checkbox-others", value = "true")
  private Cookie cookieInfoCheckboxOthers;

  public void addCookie(Cookie cookie) {
    WebTestContextProvider.get().getBrowser().getRemoteWebDriver().manage().addCookie(cookie);
  }

  public void addCookieViewedCookiePolicy() {
    addCookie(viewedCookiePolicy);
    addCookie(cookieInfoCheckboxAdvertisement);
    addCookie(cookieInfoCheckboxAnalytics);
    addCookie(cookieInfoCheckboxFunctional);
    addCookie(cookieInfoCheckboxOthers);
  }


  public static class CookieBuilder {

    @SneakyThrows
    public void build(Object instance) {
      Field[] fields = instance.getClass().getDeclaredFields();

      for (Field field : fields) {
        field.setAccessible(true);

        if (field.getType() == Cookie.class) {
          Cookies cookies = field.getAnnotation(Cookies.class);

          field.set(instance, new Cookie(cookies.key(),
              cookies.value(),
              cookies.domain(), cookies.path(),
              cookies.expiry() == 0 ? null : DateUtils.addDays(new Date(), cookies.expiry())));
        }
      }
    }
  }

}
