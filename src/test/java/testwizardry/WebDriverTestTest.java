package testwizardry;

import org.junit.Test;

public class WebDriverTestTest extends WebDriverTest
{
	@Test
	public void shouldLaunchWebDriver()
	{
		driver.get("http://localhost:8080");
	}
}
