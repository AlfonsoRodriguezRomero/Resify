
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
public class HU1PositivoUITest {

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

	//El usuario manager accede al registro de una residencia,
	//completa el formulario y puede ver el show de su residencia
	//recién creada
	@Test
	public void testHU1PositivoUI() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//a[contains(@href, '/login')]")).click();
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("manager3");
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("manager3");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/residencias/new')]")).click();
		this.driver.findElement(By.id("nombre")).click();
		this.driver.findElement(By.id("nombre")).clear();
		this.driver.findElement(By.id("nombre")).sendKeys("Residencia Prueba");
		this.driver.findElement(By.id("direccion")).clear();
		this.driver.findElement(By.id("direccion")).sendKeys("direccion");
		this.driver.findElement(By.id("descripcion")).clear();
		this.driver.findElement(By.id("descripcion")).sendKeys("descripcion");
		this.driver.findElement(By.id("aforo")).clear();
		this.driver.findElement(By.id("aforo")).sendKeys("20");
		this.driver.findElement(By.id("telefono")).clear();
		this.driver.findElement(By.id("telefono")).sendKeys("987654321");
		this.driver.findElement(By.id("correo")).clear();
		this.driver.findElement(By.id("correo")).sendKeys("resi@mail.com");
		this.driver.findElement(By.id("horaApertura")).clear();
		this.driver.findElement(By.id("horaApertura")).sendKeys("09:00");
		this.driver.findElement(By.id("horaCierre")).clear();
		this.driver.findElement(By.id("horaCierre")).sendKeys("21:00");
		this.driver.findElement(By.id("edadMaxima")).clear();
		this.driver.findElement(By.id("edadMaxima")).sendKeys("90");
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
