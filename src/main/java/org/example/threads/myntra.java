package org.example.threads;

import org.example.product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class myntra extends Thread{
    WebDriver driver;
    String link;
    static volatile List<product> items;
    public myntra(WebDriver driver, String link, List<product> items){
        myntra.items = items;
        this.link = link;
        this.driver = driver;
    }

    @Override
    public void run() {
        try{
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless=new");
            driver = new ChromeDriver(options);
            driver.get(link);

            WebElement cardList = driver.findElement(By.cssSelector(".results-base"));
            List<WebElement> cards = cardList.findElements(By.cssSelector(".product-base"));

            for (WebElement card : cards) {
                String brand = card.findElement(By.cssSelector(".product-brand")).getText();
                String product = card.findElement(By.cssSelector(".product-product")).getText();
                String price = card.findElement(By.cssSelector(".product-price")).getText();

                product p = new product(brand, product, price);
                items.add(p);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if(driver!=null){
                driver.close();
                driver.quit();
            }
        }
    }

    public List<product> getProductList() {
        return items;
    }
}
