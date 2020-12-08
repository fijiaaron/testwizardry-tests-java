package testwizardry.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Properties;

public class HomePage extends TestWizardryPage
{
	public String path = "/index.html";
	public String title = "TestWizardry";

	public HomePage(WebDriver driver, Properties env)
	{
		super(driver, env);
	}

	public HomePage open()
	{
		driver.get(baseUrl + path);
		return whenLoaded();
	}

	public HomePage whenLoaded()
	{
		if (isLoaded())
		{
			return this;
		}

		throw new PageException("not on page: " + this);
	}

	public ApplicationPage clickApplyNow()
	{
		apply.click();
		ApplicationPage applicationPage = new ApplicationPage(driver, env);
		return applicationPage.whenLoaded();
	}

	@FindBy(linkText = "Apply now")
	public WebElement apply;
}
