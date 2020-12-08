package testwizardry.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Properties;

public class RejectionPage extends TestWizardryPage
{
	public String path = "/rejected.html";
	public String title = "TestWizardry - Rejected";

	@FindBy(linkText="Go back to start")
	public WebElement goBackButton;

	public RejectionPage(WebDriver driver, Properties env)
	{
		super(driver, env);
	}

	public RejectionPage whenLoaded()
	{
		if (isLoaded())
		{
			return this;
		}

		throw new PageException("not on page: " + this);
	}
}
