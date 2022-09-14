package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class MainPage {

    private String url = "http://localhost:8085/";
    private ChromeDriver driver;

    public MainPage(ChromeDriver driver) {
        this.driver = driver;
    }

    public MainPage callPage() {
        driver.get(url);
        return this;
    }

    public MainPage pause() throws InterruptedException {
        Thread.sleep(2000);
        return this;
    }

    public MainPage setUpSearchToken(String token) {
        WebElement element = driver.findElement(By.id("query"));
        element.sendKeys(token);
        return this;
    }

    public MainPage submit() {
        WebElement element = driver.findElement(By.id("search"));
        element.submit();
        return this;
    }

    public MainPage click(String xPath) {
        WebElement element = driver.findElement(By.xpath(xPath));
        element.click();
        return this;
    }

    public MainPage setUpPubDateToken(String id, String token) {
        WebElement element = driver.findElement(By.id(id));
        element.clear();//значения в полях заполенния дат стоял по умолчанию, очищаем данные поля
        element.sendKeys(token);
        return this;
    }

    public MainPage setUpSignInToken(String id, String token) {
        WebElement element = driver.findElement(By.id(id));
        element.sendKeys(token);
        return this;
    }

    public MainPage clickButton(String id) {
        WebElement element = driver.findElement(By.id(id));
        element.click();
        return this;
    }
}
