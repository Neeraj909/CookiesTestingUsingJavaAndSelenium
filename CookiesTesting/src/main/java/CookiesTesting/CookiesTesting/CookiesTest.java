package CookiesTesting.CookiesTesting;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.github.bonigarcia.wdm.WebDriverManager;

public class CookiesTest {
	public static long IMPLICIT_WAIT = 60;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		storingCookies();
		//retrievingCookie();
	}

	public static void storingCookies() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		driver.get("https://www.facebook.com/");
		driver.findElement(By.cssSelector("#email")).sendKeys("eamil");
		driver.findElement(By.cssSelector("#pass")).sendKeys("paas");
		driver.findElement(By.cssSelector("#loginbutton")).click();
	
		try { // Delete if any old file exists
			
			for (Cookie cook : driver.manage().getCookies()) {
				String writeup = cook.getName() + ";" + cook.getValue() + ";" + cook.getDomain() + ";" + cook.getPath()
						+ "" + ";" + cook.getExpiry() + ";" + cook.isSecure();
				WebDriverManager.chromedriver().setup();
				WebDriver driver1 = new ChromeDriver();
				driver1.manage().window().maximize();
				driver1.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
				Cookie ck = new Cookie(cook.getName(), cook.getValue(),  cook.getDomain(), cook.getPath(), cook.getExpiry(), cook.isSecure());
				driver1.manage().addCookie(ck);
				driver1.get("https://www.facebook.com/");
				driver1.close();
			}
		
		} catch (Exception exp) {
			exp.printStackTrace();
		}
		driver.close();
		
	}


	public static void retrievingCookie() {
		WebDriverManager.chromedriver().setup();
		WebDriver driver = new ChromeDriver();
		driver.manage().window().maximize();
		driver.manage().timeouts().implicitlyWait(IMPLICIT_WAIT, TimeUnit.SECONDS);
		try {
			File file = new File("Cookie.data");
			FileReader fileReader = new FileReader(file);
			BufferedReader Buffreader = new BufferedReader(fileReader);
			String strline;
			while ((strline = Buffreader.readLine()) != null) {
				StringTokenizer token = new StringTokenizer(strline, ";");
				while (token.hasMoreTokens()) {
					String name = token.nextToken();
					String value = token.nextToken();
					String domain = token.nextToken();
					String path = token.nextToken();
					DateFormat format = null;
					Date expiry=null;
					String val;
					if (!(val = token.nextToken()).equals("null")) {
						 format=new SimpleDateFormat("EE MMM dd HH:mm:ss z yyyy",
                                 Locale.ENGLISH);
						expiry = format.parse(val);  
					}
					Boolean isSecure = new Boolean(token.nextToken()).booleanValue();
					Cookie ck = new Cookie(name, value, domain, path, expiry, isSecure);
					driver.manage().addCookie(ck); // This will add the stored cookie to our current session
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		driver.get("https://www.facebook.com/");
	}

}
