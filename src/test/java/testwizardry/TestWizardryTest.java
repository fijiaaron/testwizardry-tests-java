package testwizardry;

import org.junit.After;
import org.junit.Before;
import org.openqa.selenium.WebDriver;
import testwizardry.automation.DriverFactory;
import testwizardry.automation.TestWizardrySite;

import java.io.IOException;
import java.util.Properties;


public class TestWizardryTest
{
	Properties env;
	WebDriver driver;
	TestWizardrySite testWizardry;

	@Before
	public void setup() throws IOException
	{
		env = DriverFactory.loadConfig("firefox.test.properties");
		driver = new DriverFactory(env).getDriver();
		testWizardry = new TestWizardrySite(driver, env);
	}

	@After
	public void cleanup()
	{
		driver.quit();
	}

	protected void pause(int seconds)
	{
		try {
			Thread.sleep(1000 * seconds);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
