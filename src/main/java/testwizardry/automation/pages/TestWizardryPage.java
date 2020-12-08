package testwizardry.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public abstract class TestWizardryPage
{
	final Logger log;

	protected Properties env;
	protected String baseUrl;
	protected String path;
	protected String title;

	protected WebDriver driver;
	protected WebDriverWait wait;
	protected int timeout;

	public TestWizardryPage(WebDriver driver, Properties env)
	{
		log = LoggerFactory.getLogger(getClass());
		log.debug("creating page object: " + getClass().getSimpleName());

		configure(env);

		log.debug("baseUrl: " + baseUrl);
		log.debug("path: " + path);

		this.driver = driver;
		this.baseUrl = baseUrl;

		wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));

		log.debug("initElements PageFactory");
		PageFactory.initElements(driver, this);
	}

	public void configure(Properties env)
	{
		this.env = env;
		baseUrl = env.getProperty("baseUrl");
		timeout = Integer.valueOf(env.getProperty("timeout"));
	}

	public String getPath()
	{
		return this.path;
	}

	public String getExpectedTitle()
	{
		return this.title;
	}

	public boolean isLoaded()
	{
		log.debug("isLoaded()");

		String path = getPath();
		String title = getExpectedTitle();

		if (path != null)
		{
			boolean correctPath = wait.until(urlContains(getPath()));
			if (! correctPath) { return false; }
		}

		if (title != null)
		{
			boolean correctTitle = wait.until(titleContains(getExpectedTitle()));
			if (! correctTitle) { return false; }
		}

		log.debug("page is loaded");
		return true;
	}

	public WebElement whenPresent(By selector)
	{
		return wait.until(presenceOfElementLocated(selector));
	}

	public Boolean whenTextIsPresentIn(By selector, String text)
	{
		return wait.until(textToBePresentInElementLocated(selector, text));
	}

	public WebElement whenVisible(By selector)
	{
		return wait.until(visibilityOfElementLocated(selector));
	}

	public WebElement whenVisible(WebElement element)
	{
		return wait.until(visibilityOf(element));
	}

	public WebElement whenActive(By selector)
	{
		return wait.until(elementToBeClickable(selector));
	}

	public WebElement whenActive(WebElement element)
	{
		return wait.until(elementToBeClickable(element));
	}

	public void typeIn(By selector, String text)
	{
		whenActive(selector).sendKeys(text);
	}

	public void click(By selector)
	{
		whenActive(selector).click();
	}

	public void check(By selector)
	{
		WebElement element = whenActive(selector);
		if (! element.isSelected())
		{
			element.click();
		};
	}

	public void pause(int seconds)
	{
		try { Thread.sleep(2); } catch(InterruptedException e) { e.printStackTrace(); }
	}
}
