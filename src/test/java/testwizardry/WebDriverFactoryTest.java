package testwizardry;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import testwizardry.automation.WebDriverFactory;
import testwizardry.automation.pages.HomePage;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class WebDriverFactoryTest
{
	@Test
	public void shouldGetInstance() throws MalformedURLException
	{
		Properties config = new Properties();
		config.setProperty("browser", "CHROME");
		config.setProperty("timeout", "30");
		config.setProperty("driver", "LOCAL");
		WebDriverFactory factory = new WebDriverFactory(config);

		assertThat(factory).isNotNull();

		System.out.println(factory.getProperty("browser", "DEFAULT"));
		System.out.println(factory.getProperty("driver", "DEFAULT"));
		System.out.println(factory.getProperty("timeout", "DEFAULT"));

		System.out.println(factory.getBrowser().getClass().getName() + " : " + factory.getBrowser());
		System.out.println(factory.getDriverType().getClass().getName() + " : " + factory.getDriverType());
		System.out.println(factory.getTimeout().getClass().getName() + " : " + factory.getTimeout());

		WebDriver driver = factory.getInstance();
		System.out.println(driver.getClass().getName() + " : " + driver);

		Properties env = new Properties();
		env.setProperty("baseUrl", "http://localhost:8080");
		env.setProperty("timeout", "30");

		HomePage homePage = new HomePage(driver, env);
		homePage.open();

		driver.quit();
	}

	@Test
	public void shouldGetInstanceWithConfigFile() throws IOException
	{
		Properties config = new Properties();

		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println(classLoader);

		InputStream inputStream = classLoader.getResourceAsStream("test.properties");
		System.out.println("inputStream: " + inputStream);

		config.load(inputStream);
		System.out.println("config properties: " + config);

		WebDriverFactory factory = new WebDriverFactory(config);

		assertThat(factory).isNotNull();

		System.out.println(factory.getProperty("browserType", "DEFAULT"));
		System.out.println(factory.getProperty("driverType", "DEFAULT"));
		System.out.println(factory.getProperty("timeout", "DEFAULT"));
		System.out.println(factory.getProperty("baseUrl", "DEFAULT"));

		System.out.println(factory.getBrowser().getClass().getName() + " : " + factory.getBrowser());
		System.out.println(factory.getDriverType().getClass().getName() + " : " + factory.getDriverType());
		System.out.println(factory.getTimeout().getClass().getName() + " : " + factory.getTimeout());
		System.out.println(factory.getBaseUrl().getClass().getName() + " : " + factory.getBaseUrl());

		String baseUrl = "http://localhost:8080/";

		WebDriver driver = factory.getInstance();
		System.out.println(driver.getClass().getName() + " : " + driver);

		HomePage homePage = new HomePage(driver, config);
		homePage.open();

		System.out.println(driver.getTitle());

		driver.quit();
	}
}
