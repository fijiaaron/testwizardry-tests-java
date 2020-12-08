package testwizardry.config;

import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import testwizardry.automation.DriverFactory;
import testwizardry.automation.pages.HomePage;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class HogwartsLocalConfigAcceptanceTests
{
	WebDriver driver;
	WebDriverWait wait;

	public String baseUrl;
	public int timeout;

	@Rule
	public TestName test = new TestName();

	final org.slf4j.Logger log;

	static protected Properties config;

	public HogwartsLocalConfigAcceptanceTests()
	{
		log = LoggerFactory.getLogger(getClass());
		log.debug("constructor");

		String baseUrl = config.getProperty("baseUrl", "http://locahost:8080");
		log.debug("baseUrl: " + baseUrl);
		this.baseUrl = baseUrl;

		String timeout = config.getProperty("timeout", "30");
		log.debug("timeout: " + timeout);
		this.timeout = Integer.valueOf(timeout);
	}

	@BeforeClass
	static public void init() throws IOException
	{
		config = loadConfig("local.test.properties");
	}

	static public Properties loadConfig(String configFile) throws IOException
	{
		ClassLoader classLoader = HogwartsLocalConfigAcceptanceTests.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(configFile);

		config = new Properties();
		config.load(inputStream);

		return config;
	}

	@Before
	public void setup() throws IOException
	{
		log.debug("setup()");
		log.debug(test.getMethodName());
		log.debug("config: " + config);

		driver = new DriverFactory(config).getDriver();
		log.debug("driver:" + driver);

		wait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	@Test
	public void applyToHogwarts()
	{
		HomePage homePage = new HomePage(driver, config);
		homePage.open();
		whenTitleContains("Test Wizardry");
		String title = driver.getTitle();
		log.info("got title: " + title);

		assertThat(title).isEqualTo("Test Wizardry");
		driver.findElement(By.linkText("Apply now")).click();
		whenTitleContains("Apply");
		title = driver.getTitle();
		log.info("got title: " + title);
	}

	@After
	public void cleanup()
	{
		log.debug("cleanup()");
		pause(2.5);
		if (driver != null)
		{
			driver.quit();
		}
	}

	public void pause(Double seconds)
	{
		try
		{
			Thread.sleep((long) (1000 * seconds));
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public WebElement whenPresent(By locator)
	{
		return wait.until(presenceOfElementLocated(locator));
	}

	public boolean whenUrlContains(String text)
	{
		return wait.until(urlContains(text));
	}

	public boolean whenTitleContains(String text)
	{
		return wait.until(titleContains(text));
	}

}
