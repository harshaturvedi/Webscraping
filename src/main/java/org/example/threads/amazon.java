package org.example.threads;

import org.example.product;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

public class amazon extends Thread{
    WebDriver driver;
    String link;
    static volatile List<product> items;
    public amazon(WebDriver driver, String link, List<product> items){
        amazon.items = items;
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

            List<WebElement> cards = driver.findElements(By.xpath("//div[@data-component-type='s-search-result']"));

            for (WebElement card : cards) {
                String brand = card.findElement(By.cssSelector("div.a-row h2.a-size-mini span.a-size-base-plus")).getText();
                String product = card.findElement(By.cssSelector("span.a-text-normal")).getText();
                String price = card.findElement(By.cssSelector("div.s-price-instructions-style")).getText();

                org.example.product p = new product(brand, product, price);
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
