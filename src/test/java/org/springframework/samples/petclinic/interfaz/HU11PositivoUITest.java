
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU11PositivoUITest {

	@LocalServerPort
	private int				port;

	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	// El usuario manager accede a la lista de actividades, clica en el boton para
	// crear una nueva y rellena todos los campos con datos validos, por lo que se
	// registra la actividad y aparece el titulo de esta en la lista actualizada
	@Test
	public void testRegistrarActividadPos() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manager1");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manager1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/actividades')]")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Añadir nueva actividad')]")).click();
		this.driver.findElement(By.id("titulo")).click();
		this.driver.findElement(By.id("titulo")).clear();
		this.driver.findElement(By.id("titulo")).sendKeys("Test Interfaz");
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("Test Interfaz");
		this.driver.findElement(By.id("fechaInicio")).click();
		this.driver.findElement(By.id("fechaInicio")).sendKeys("2020/08/10");
		this.driver.findElement(By.id("horaInicio")).click();
		this.driver.findElement(By.id("horaInicio")).clear();
		this.driver.findElement(By.id("horaInicio")).sendKeys("10:00");
		this.driver.findElement(By.id("horaFin")).clear();
		this.driver.findElement(By.id("horaFin")).sendKeys("13:00");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assert.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}
}
