package testwizardry.automation;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.devtools.v84.io.IO;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class DriverFactory
{
	public Properties config;
	public BrowserType browserType;
	public DriverType driverType;
	public String seleniumServerUrl;

	final org.slf4j.Logger log;

	public DriverFactory() throws IOException
	{
		log = LoggerFactory.getLogger(getClass());
		configure();
	}

	public DriverFactory(Properties config) throws IOException
	{
		log = LoggerFactory.getLogger(getClass());
		configure(config);
	}

	public void configure() throws IOException
	{
		configure(null); // use default properties file
	}

	public void configure(Properties config) throws IOException
	{
		if (config == null)
		{
			config = loadConfig();
		}
		
		log.info("configure() " + config);
		this.config = config;

		String browserType = config.getProperty("browserType", "CHROME");
		log.debug("browserType: " + browserType);
		this.browserType = BrowserType.valueOf(browserType.trim().toUpperCase());

		String driverType = config.getProperty("driverType", "LOCAL");
		log.debug("driverType: " + driverType);
		this.driverType = DriverType.valueOf(driverType.trim().toUpperCase());

		String seleniumServerUrl = config.getProperty("seleniumServerUrl", "http://localhost:4444/wd/hub");
		log.debug("seleniumServerUrl: " + seleniumServerUrl);
		this.seleniumServerUrl = seleniumServerUrl;

		String timeout = config.getProperty("timeout", "30");
		log.debug("timeout: " + timeout);

		String baseUrl = config.getProperty("baseUrl", "http://locahost:8080");
		log.debug("baseUrl: " + baseUrl);
	}

	static public Properties loadConfig() throws IOException
	{
		return loadConfig("test.properties");
	}

	static public  Properties loadConfig(String configFile) throws IOException
	{
		ClassLoader classLoader = DriverFactory.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(configFile);

		Properties config = new Properties();
		config.load(inputStream);

		return config;
	}

	public Properties getConfig()
	{
		return config;
	}

	public WebDriver getDriver() throws MalformedURLException
	{
		log.debug("getDriver() ");
		log.info("browserType: " + config.getProperty("browserType"));
		log.info("driverType: " + driverType);
		if (driverType.equals("REMOTE"))
		{
			return getRemoteWebDriver();
		}

		switch(browserType)
		{
			case CHROME:
				ChromeOptions chromeOptions = new ChromeOptions();
				return new ChromeDriver(chromeOptions);
			case SAFARI:
				SafariOptions safariOptions = new SafariOptions();
				return new SafariDriver(safariOptions);
			case FIREFOX:
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				return new FirefoxDriver(firefoxOptions);
			case EDGE:
				EdgeOptions edgeOptions = new EdgeOptions();
				return new EdgeDriver(edgeOptions);
			default:
				throw new RuntimeException("Unknown browserType: " + browserType);
		}
	}

	public RemoteWebDriver getRemoteWebDriver() throws MalformedURLException
	{
		log.debug("getRemoteWebDriver");
		log.info("browserType: " + browserType);
		log.info("seleniumServerUrl: " + seleniumServerUrl);

		URL url = new URL(seleniumServerUrl);
		Capabilities capabilities = new MutableCapabilities();

		switch(browserType)
		{
			case CHROME:
				ChromeOptions chromeOptions = new ChromeOptions();
				capabilities.merge(chromeOptions);
				break;
			case SAFARI:
				SafariOptions safariOptions = new SafariOptions();
				capabilities.merge(safariOptions);
				break;
			case FIREFOX:
				FirefoxProfile firefoxProfile = new FirefoxProfile();
				FirefoxOptions firefoxOptions = new FirefoxOptions();
				firefoxOptions.setProfile(firefoxProfile);
				capabilities.merge(firefoxOptions);
				break;
			case EDGE:
				EdgeOptions edgeOptions = new EdgeOptions();
				capabilities.merge(edgeOptions);
				break;
			default:
				throw new RuntimeException("Unknown browserType: " + browserType);
		}

		return new RemoteWebDriver(url, capabilities);
	}
}
