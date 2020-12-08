package testwizardry.remote;

import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import testwizardry.automation.pages.HomePage;
import testwizardry.automation.DriverFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class HogwartsRemoteAcceptanceTests
{
	WebDriver driver;
	WebDriverWait wait;

	@Rule
	public TestName test = new TestName();

	final org.slf4j.Logger log;

	String browserType = "chrome"; // chrome | firefox | safari | edge | ie
	String driverType = "remote"; // remote | local
	String seleniumServerUrl = "http://localhost:4444/wd/hub";

	String baseUrl = "http://localhost:8080";

	static protected Properties config;

	public HogwartsRemoteAcceptanceTests()
	{
		log = LoggerFactory.getLogger(getClass());
	}

	@BeforeClass
	static public void init()
	{
		config = new Properties();
		config.setProperty("browserType", "chrome");
		config.setProperty("driverType", "remote");
		config.setProperty("remoteWebDriverUrl", "http://localhost:4444/wd/hub");
		config.setProperty("baseUrl", "http://localhost:8080");
	}


	@Before
	public void setup() throws IOException
	{
		log.debug(test.getMethodName());
		log.debug("config: " + config);

		driver = new DriverFactory().getDriver();
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
	}

	@After
	public void cleanup()
	{
		pause(2.5);
		driver.quit();
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
