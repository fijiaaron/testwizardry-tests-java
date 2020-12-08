package testwizardry;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;
import testwizardry.automation.WebDriverFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.Properties;

public class WebDriverTest
{
	WebDriver driver;

	// defaults
	static String CONFIG_FILE = "test.properties";
	static String SITE_URL = "http://localhost:8080/";
	static String PAGE_TIMEOUT = "30"; // seconds

	static Properties config;

	@BeforeClass
	public static void init() throws IOException
	{
		Properties config = getConfig();
		System.out.println("config properties: " + config);

		WebDriverFactory factory = new WebDriverFactory(config);
		System.out.println("webdriver factory: " + factory);

		// get environment variables first
		SITE_URL = System.getenv("SITE_URL");
		PAGE_TIMEOUT = System.getenv("PAGE_TIMEOUT");


		if (SITE_URL == null)
		{
			SITE_URL = config.getProperty("baseUrl", "http://localhost:8080/");
		}
		if (PAGE_TIMEOUT == null)
		{
			PAGE_TIMEOUT = config.getProperty("timeout", "30" );
		}
	}

	public static Properties getConfig() throws IOException
	{
		Properties config = new Properties();

		ClassLoader classLoader = WebDriverTest.class.getClassLoader();
		System.out.println("classLoader: " + classLoader);

		InputStream inputStream = classLoader.getResourceAsStream("test.properties");
		System.out.println("inputStream: " + inputStream);

		config.load(inputStream);

		System.out.println("in getConfig() config: " + config);
		return config;
	}

	@Before
	public void setup() throws MalformedURLException
	{
		driver = new WebDriverFactory(config).getInstance();
	}

	@After
	public void cleanup()
	{
		if (driver != null)
		{
			driver.quit();
		}
	}
}
