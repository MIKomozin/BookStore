package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class MainPageSeleniumTests {

    private static ChromeDriver driver;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Максим\\Desktop\\УЧЕБА по IT\\SkillBox\\WebDriver\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

    @Test
    public void testMainPageAccess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause();

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }

    @Test
    public void testMainPageSearchByQuery() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .setUpSearchToken("King")
                .pause()
                .submit()
                .pause();

        assertTrue(driver.getPageSource().contains("King of Kings"));
    }

    @Test
    public void testClickByBookPicture() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .click("/html/body/div/div/main/div[1]/div/div[2]/div[1]/div/div/div[1]/div/div/a")//имитация щелчка мыши по картинике книги "Patton" (по xpath)
                .pause();

        assertTrue(driver.getPageSource().contains("Patton"));
        assertTrue(driver.getPageSource().contains("2330₽"));
    }

    @Test
    public void testClickByGenresAndClickFantastic() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .click("//*[@id=\"navigate\"]/ul/li[2]/a")//кликаем на жанры
                .pause()
                .click("/html/body/div/div/main/div/div[1]/div/div[2]/div/div/a")//кликаем на фантастику
                .pause();

        assertTrue(driver.getPageSource().contains("Фантастика"));
        assertTrue(driver.getPageSource().contains("Sunset Strip"));
        assertEquals(7, driver.findElements(By.className("Card")).size());//должен найти 7 книг
    }

    @Test
    public void testClickByNewsAndChangeDatePub() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .click("//*[@id=\"navigate\"]/ul/li[3]/a")
                .pause()
                .setUpPubDateToken("fromdaterecent", "06.07.2020")
                .pause()
                .setUpPubDateToken("enddaterecent", "14.09.2022")
                .pause();

        assertEquals(4, driver.findElements(By.className("Card")).size());//в этот промежуток дат публикации должно быть четыре книги
        assertTrue(driver.getPageSource().contains("Mallrats"));//включая "Mallrats"
    }

    @Test
    public void testClickPopularAndClickPopularBook() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .click("//*[@id=\"navigate\"]/ul/li[4]/a")
                .pause()
                .click("/html/body/div/div/main/div/div[2]/div[1]/div[1]/div[1]/a/img")//кликаем на книгу "1066"
                .pause();

        assertTrue(driver.getPageSource().contains("1066"));
        assertTrue(driver.getPageSource().contains("443₽"));
    }

    @Test
    public void testClickAuthorsAndCliclSomeAuthor() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .click("//*[@id=\"navigate\"]/ul/li[5]/a")
                .pause()
                .click("/html/body/div/div/main/div/div/div[2]/div/div/a")//кликаем на автора Aldwin Maddison
                .pause()
                .click("/html/body/div/div/main/footer/a")//кликаем на все книги автора
                .pause()
                .click("/html/body/div/div/main/div/div/div[2]/a")//показать еще, чтобы отобразить все книги
                .pause();

        assertTrue(driver.getPageSource().contains("King of Kings"));
        assertTrue(driver.getPageSource().contains("Winning"));
        assertEquals(12, driver.findElements(By.className("Card")).size());//Aldwin Maddison опубликовал 12 книг
    }

    @Test
    public void testCartWork() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .click("/html/body/div/div/main/div[1]/div/div[2]/div[1]/div/div/div[1]/div/div/a/img")//кликаем на "Patton"
                .pause()
                .click("/html/body/div/div/main/div/div[1]/div[2]/div[3]/div[1]/div[2]/button")//кликаем на кнопку купить
                .pause()
                .click("/html/body/div/div/main/ul/li[1]/a")//переходим на главную
                .pause()
                .click("/html/body/div/div/main/div[1]/div/div[4]/div[1]/div/div/div[1]/div/div/a")///кликаем на "Mallrats"
                .pause()
                .click("/html/body/div/div/main/div/div[1]/div[2]/div[3]/div[1]/div[2]/button")//кликаем накнопку купить
                .pause()
                .click("/html/body/header/div[1]/div/div/div[3]/div/a[2]")//переходим в корзину
                .pause();

        assertTrue(driver.getPageSource().contains("Patton"));
        assertTrue(driver.getPageSource().contains("Mallrats"));
        assertEquals(2, driver.findElements(By.className("Cart-product")).size());//в корзине должно быть 2 книги
    }

    @Test
    public void testSignIn() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .click("/html/body/header/div[1]/div/div/div[3]/div/a[3]")//signin
                .pause()
                .click("/html/body/div/div[2]/main/form/div/div[1]/div[2]/div/div[2]/label/input")//email
                .pause()
                .setUpSignInToken("mail", "m.i.komozin@gmail.com")
                .pause()
                .clickButton("sendauth")//далкк
                .pause()
                .setUpSignInToken("mailcode", "12345678")
                .pause()
                .clickButton("toComeInMail")//войти
                .pause()
                .click("/html/body/header/div[1]/div/div/div[3]/div/a[4]/span[1]")//profile
                .pause();
        assertTrue(driver.getPageSource().contains("Max"));
        assertTrue(driver.getPageSource().contains("m.i.komozin@gmail.com"));
        assertTrue(driver.getPageSource().contains("+7 (222) 222-22-22"));
    }
}