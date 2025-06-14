package com.insider;

import static com.insider.page.careers.CareersOpenPositionsPage.ISTANBUL_TURKIYE;
import static com.insider.page.careers.CareersOpenPositionsPage.QUALITY_ASSURANCE;
import static com.insider.page.careers.CareersPage.LOCATIONS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.AbstractTestData;
import com.insider.annotations.test.WebTest;
import com.insider.page.careers.CareersOpenPositionsPage;
import com.insider.page.careers.CareersPage;
import com.insider.page.careers.CareersPage.CareersPageUrls;
import com.insider.page.homepage.UseInsiderHomePage.UseInsiderHomePageUrls;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.annotation.Description;

public class InsiderCareersQAJobListingsTest extends AbstractTestData {

  @BeforeEach
  public void before() {
    useInsiderHomePage.go(UseInsiderHomePageUrls.INSIDER_HOME_PAGE);

    cookie.addCookieViewedCookiePolicy();
  }

  @WebTest
  @Description("""
      Navbar'dan Company menüsü altındaki Careers seçeneğine tıklayarak Careers sayfasına geçiş yapılır, 
      sayfa URL'i ve lokasyon bilgileri doğrulanır, Load More butonu ile iş ilanları yükleme işlevi test edilir
      """)
  public void testCareersPageNavigationAndJobListingsLoadMore() {
    CareersPage careersPage = useInsiderHomePage
        .mouseHoverNavbarDropdownMenuLink()
        .clickCareers();

    assertThat(careersPage.getCurrentUrl()).startsWith(CareersPageUrls.CAREERS);
    assertThat(careersPage.allLocationTexts()).containsExactlyInAnyOrderElementsOf(LOCATIONS);

    var jobImageSize = careersPage
        .scrollLoadMoreButton()
        .getJobImageSize();

    careersPage.clickLoadMoreButton();

    assertFalse(careersPage.isDisplayedLoadMoreButton());
    assertThat(jobImageSize).isLessThan(careersPage.getJobImageSize());

    careersPage.scrollToActiveSwiperSlide();

    assertTrue(careersPage.isDisplayedSliderSwipeActive());
  }

  @WebTest
  @Description("""
      Quality Assurance careers sayfasına gidilir, "See all QA jobs" butonuna tıklanır,
      işler "Istanbul, Turkey" lokasyonu ve "Quality Assurance" departmanı ile filtrelenir,
      tüm işlerin pozisyon, departman ve lokasyon bilgileri doğrulanır,
      "View Role" butonuna tıklanarak Lever başvuru sayfasına yönlendirme kontrol edilir
      """)
  public void testQualityAssuranceJobsFilteringAndLeverRedirection() {
    CareersPage careersPage = useInsiderHomePage
        .go(CareersPageUrls.CAREERS_QUALITY_ASSURANCE, CareersPage.class);

    CareersOpenPositionsPage careersOpenPositionsPage = careersPage
        .clickSeeAllQAJobs();

    careersOpenPositionsPage
        .selectLocation(ISTANBUL_TURKIYE)
        .selectDepartment(QUALITY_ASSURANCE)
        .sleepSecond(3);

    assertTrue(careersOpenPositionsPage.allPositionLocations(ISTANBUL_TURKIYE));
    assertTrue(careersOpenPositionsPage.allPositionDepartments(QUALITY_ASSURANCE));

    var jobLink = careersOpenPositionsPage
        .clickViewRole(0);

    careersOpenPositionsPage
        .tabChange(1);

    assertEquals(jobLink, careersOpenPositionsPage.getCurrentUrl());
  }

}
