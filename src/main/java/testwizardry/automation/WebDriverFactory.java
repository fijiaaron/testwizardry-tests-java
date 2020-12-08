package testwizardry.automation;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class WebDriverFactory
{
	Properties config;

	public WebDriverFactory(Properties config)
	{
		this.config = config;
	}

	public WebDriver getInstance() throws MalformedURLException
	{
		BrowserType browserType = getBrowser();
		System.out.println("browserType:" + browserType);

		DriverType driverType = getDriverType();
		System.out.println("driverType:" + driverType);

		if (driverType == DriverType.LOCAL) {
			switch (browserType) {
				case CHROME:
					ChromeOptions options = new ChromeOptions();
					return new ChromeDriver(options);
				case FIREFOX:
					return new FirefoxDriver();
				case IE:
					return new InternetExplorerDriver();
				case EDGE:
					return new EdgeDriver();
				case SAFARI:
					return new SafariDriver();
				default:
					return new ChromeDriver();
			}
		}
		else if (driverType == DriverType.REMOTE)
		{
			Capabilities capabilities = new MutableCapabilities();
			// add custom capabilities here

			switch (browserType) {
				case CHROME:
					ChromeOptions chromeOptions = new ChromeOptions();
					// add chrome options here
					capabilities.merge(new ChromeOptions());
				case FIREFOX:
					FirefoxOptions firefoxOptions = new FirefoxOptions();
					// add firefox options here
					FirefoxProfile firefoxProfile = new FirefoxProfile();
					// specify firefox profile here
					firefoxOptions.setProfile(firefoxProfile);
					capabilities.merge(firefoxOptions);
				case IE:
					InternetExplorerOptions ieOptions = new InternetExplorerOptions();
					capabilities.merge(ieOptions);
				case EDGE:
					EdgeOptions edgeOptions = new EdgeOptions();
					// add edge options here
					capabilities.merge(edgeOptions);
				case SAFARI:
					SafariOptions safariOptions = new SafariOptions();
					// add safari options here
					capabilities.merge(safariOptions);
				default:
					URL url = getRemoteWebDriverUrl();
					return new RemoteWebDriver(url, capabilities);
			}
		}

		throw new RuntimeException("could not create driver instance with config: " + config);
	}

	public BrowserType getBrowser()
	{
		System.out.println("Getting BrowserType");
		return BrowserType.valueOf(getProperty("browser", "CHROME"));
	}

	public DriverType getDriverType()
	{
		return DriverType.valueOf(getProperty("driver", "LOCAL"));
	}

	public Integer getTimeout()
	{
		return Integer.valueOf(getProperty("timeout", "60"));
	}

	public String getBaseUrl()
	{
		return getProperty("baseURL", "http://localhost:8080");
	}

	public URL getRemoteWebDriverUrl() throws MalformedURLException
	{
		String url = getProperty("remoteWebDriverUrl", "https://localhost:4444/wd/hub");
		return new URL(url);
	}

	public String getProperty(String key, String DEFAULT)
	{
		System.out.println("getting property: " + key + " with default: " + DEFAULT);

		String value = config.getProperty(key);
		
		if (value == null)
		{
			System.out.println("property not set, using default: " + DEFAULT);
			return DEFAULT;
		}

		System.out.println("got property value: " + value);
		return value.trim().toUpperCase();
	}
}
