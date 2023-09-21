package org.example;

import org.example.threads.amazon;
import org.example.threads.myntra;
import org.openqa.selenium.WebDriver;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:/chromedriver.exe");
        WebDriver driver = null;

        String userQuery = "white shirt";

        String myntraLink = "https://www.myntra.com/" + convertUrlForMyntra(userQuery);
        String amazonLink = "https://www.amazon.in/s?k=" + convertUrlForAmazon(userQuery);
//        String flipcartLink = "https://www.flipkart.com/search?q=" + convertUrlForMyntra(userQuery);
//        String purplleLink = "https://www.purplle.com/search?q=" + convertUrlForMyntra(userQuery);
//        String meeshoLink = "https://www.meesho.com/search?q=" + convertUrlForMyntra(userQuery);

        List<List<product>> allProducts = new ArrayList<>();

        List<product> myntraProducts = new ArrayList<>();
        myntra myntra = new myntra(driver, myntraLink, myntraProducts);

        List<product> amazonProducts = new ArrayList<>();
        amazon amazon = new amazon(driver, amazonLink, amazonProducts);

        try {
            myntra.start();
            amazon.start();

            myntra.join();
            myntraProducts = myntra.getProductList();
            allProducts.add(myntraProducts);

            amazon.join();
            amazonProducts = amazon.getProductList();
            allProducts.add(amazonProducts);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        for(List<product> ls: allProducts){
            System.out.println(ls.size());
        }
    }

    static String convertUrlForMyntra(String userQuery){
        char[] uqArr = userQuery.toCharArray();
        List<Character> uq = new ArrayList<>();
        for(char c : uqArr){
            uq.add(c);
        }
        for(int i = 0; i < uq.size(); i++){
            if(uq.get(i) == ' '){
                uq.remove(i);
                uq.add(i, '%');
                uq.add(i+1, '2');
                uq.add(i+2, '0');
            }
        }
        userQuery = uq.stream().map(Objects::toString).collect(Collectors.joining(""));
        return userQuery;
    }

    static String convertUrlForAmazon(String userQuery){
        char[] uqArr = userQuery.toCharArray();
        List<Character> uq = new ArrayList<>();
        for(char c : uqArr){
            uq.add(c);
        }
        for(int i = 0; i < uq.size(); i++){
            if(uq.get(i) == ' '){
                uq.remove(i);
                uq.add(i, '+');
            }
        }
        userQuery = uq.stream().map(Objects::toString).collect(Collectors.joining(""));
        return userQuery;
    }
}