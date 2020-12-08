package testwizardry.automation.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.Properties;

public class AcceptancePage extends TestWizardryPage
{
	public String path = "/accepted.html";
	public String title = "TestWizardry - Accepted";

	public AcceptancePage(WebDriver driver, Properties env)
	{
		super(driver, env);
	}

	@FindBy(className="letter")
	public WebElement letter;

	@FindBy(id="salutation")
	public WebElement salutation;

	@FindBy(id="surname")
	public WebElement surname;

	@FindBy(partialLinkText="Diagon Alley")
	public WebElement diagonAlleyButton;

	@Override
	public boolean isLoaded()
	{
		return (super.isLoaded() && driver.getTitle().contains("Accepted"));
	}

	public AcceptancePage whenLoaded()
	{
		if (isLoaded())
		{
			return this;
		}

		throw new PageException("not on page: " + this);
	}

	public String getLetter()
	{
		return letter.getText();
	}
}
