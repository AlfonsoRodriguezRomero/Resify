
package org.springframework.samples.petclinic.interfaz;

import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU19NegativoUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testVisitaSanitariaNeg() throws Exception {
		driver.get("http://localhost:" + this.port);
		driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("manager5");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("manager5");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		driver.findElement(By.xpath("//a[contains(@href, '/visitas-sanitarias')]")).click();
		driver.findElement(By.xpath("//a[contains(text(),'Añadir\n			nueva visita')]")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		Assert.assertEquals("Nueva Visita Sanitaria", driver.findElement(By.xpath("//h2")).getText());
		Assert.assertEquals("no puede estar vacío",
				driver.findElement(By.xpath("//form[@id='visitaSanitaria']/div/div/div/span[2]")).getText());
		Assert.assertEquals("no puede estar vacío",
				driver.findElement(By.xpath("//form[@id='visitaSanitaria']/div/div[2]/div/span[2]")).getText());
		Assert.assertEquals("no puede estar vacío",
				driver.findElement(By.xpath("//form[@id='visitaSanitaria']/div/div[3]/div/span[2]")).getText());
		Assert.assertEquals("no puede ser null",
				driver.findElement(By.xpath("//form[@id='visitaSanitaria']/div/div[4]/div/span[2]")).getText());
		Assert.assertEquals("no puede ser null",
				driver.findElement(By.xpath("//form[@id='visitaSanitaria']/div/div[5]/div/span[2]")).getText());
	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
