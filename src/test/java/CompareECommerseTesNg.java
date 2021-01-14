import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.*;

public class CompareECommerseTesNg {
    public String baseUrl = "https://www.amazon.in/";
    public String driverPath = "src/test/resources/chromedriver";
    public WebDriver driver;

    @BeforeTest
    public void launchBrowser() {

        System.setProperty("webdriver.chrome.driver", driverPath);
        driver = new ChromeDriver();
        driver.get(baseUrl);
    }

    @Test
    public void comparePrices() throws InterruptedException {
        driver.get("https://www.amazon.in/");
        Thread.sleep(1000);
        driver.findElement(By.id("twotabsearchtextbox")).sendKeys("iPhone 11" + Keys.ENTER);
        List<WebElement> amazonSearchResults = driver.findElements(By.xpath("//*[@data-component-type='s-search-result']"));
        HashMap<String, Integer> resultsMap = new HashMap<String, Integer>();
        for (WebElement amazonResult : amazonSearchResults) {
            String productName;
            int price;
            String linkToTheProduct;
            productName = amazonResult.findElement(By.xpath(".//span[@class='a-size-medium a-color-base a-text-normal']")).getText();
            try {
                price = Integer.parseInt(amazonResult.findElement(By.xpath(".//span[@class='a-price-whole']")).getText().replaceAll(",", ""));
            } catch (Exception e) {
                price = 0;
            }

            System.out.println(productName);
            System.out.println(price);
            linkToTheProduct = amazonResult.findElement(By.xpath(".//a[@class='a-link-normal a-text-normal']")).getAttribute("href");
            System.out.println(linkToTheProduct);
            resultsMap.put(productName + "|" + "amazon" + "|" + linkToTheProduct, price);
        }
        driver.get("https://www.flipkart.com/");
        Thread.sleep(1000);
        driver.findElement(By.xpath("//button[@class='_2KpZ6l _2doB4z']")).click();
        driver.findElement(By.name("q")).sendKeys("iPhone 11" + Keys.ENTER);
        Thread.sleep(5000);
        List<WebElement> flipkartSearchResults = driver.findElements(By.xpath("//div[contains(@data-tkid,'SEARCH')]"));
        for (WebElement flipkartResult : flipkartSearchResults) {
            String productName;
            int price;
            String linkToTheProduct;
            productName = flipkartResult.findElement(By.xpath(".//div[@class='_4rR01T']")).getText();
            price = Integer.parseInt(flipkartResult.findElement(By.xpath(".//div[@class='_30jeq3 _1_WHN1']")).getText().replaceAll(",", "").replaceAll("₹", ""));
            System.out.println(productName);
            System.out.println(price);
            linkToTheProduct = flipkartResult.findElement(By.xpath(".//a[@class='_1fQZEK']")).getAttribute("href");
            System.out.println(linkToTheProduct);
            resultsMap.put(productName + "|" + "flipkart" + "|" + linkToTheProduct, price);
        }

        Map<String, Integer> hm1 = sortByValue(resultsMap);

        // print the sorted hashmap
        for (Map.Entry<String, Integer> en : hm1.entrySet()) {
            String website = en.getKey().split("\\|")[1];
            String productName = en.getKey().split("\\|")[0];
            String linkToProduct = en.getKey().split("\\|")[2];

            System.out.println("Price: " + en.getValue() + " " +
                    "Website: " + website + " " +
                    "Product Name: " + productName + " " +
                    "Link: " + linkToProduct);
        }
    }

    @AfterTest
    public void terminateBrowser() {
        driver.close();
    }

    public HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer>> list =
                new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}
