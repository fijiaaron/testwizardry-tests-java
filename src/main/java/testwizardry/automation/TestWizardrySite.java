package testwizardry.automation;

import org.openqa.selenium.WebDriver;
import testwizardry.automation.pages.AcceptancePage;
import testwizardry.automation.pages.ApplicationPage;
import testwizardry.automation.pages.HomePage;
import testwizardry.automation.pages.RejectionPage;

import java.util.Properties;

public class TestWizardrySite
{
	WebDriver driver;
	Properties env;
	String baseUrl;

	public TestWizardrySite(WebDriver driver, Properties env)
	{
		this.driver = driver;
		this.env = env;

		baseUrl = env.getProperty("baseUrl");
	}

	public HomePage start()
	{
		return homePage();
	}

	public HomePage homePage()
	{
		HomePage page = new HomePage(driver, env);

		if (page.isLoaded())
		{
			return page;
		}
		else
		{
			throw new RuntimeException("Not on expected page: " + page);
		}
	}

	public ApplicationPage applicationPage()
	{
		ApplicationPage page = new ApplicationPage(driver, env);

		if (page.isLoaded())
		{
			return page;
		}
		else
		{
			throw new RuntimeException("Not on expected page: " + page);
		}
	}

	public AcceptancePage acceptancePage()
	{
		AcceptancePage page = new AcceptancePage(driver, env);
		if (page.isLoaded())
		{
			return page;
		}
		else
		{
			throw new RuntimeException("Not on expected page: " + page);
		}
	}

	public RejectionPage rejectionPage()
	{
		RejectionPage page = new RejectionPage(driver, env);

		if (page.isLoaded())
		{
			return page;
		}
		else
		{
			throw new RuntimeException("Not on expected page: " + page);
		}
	}
}
