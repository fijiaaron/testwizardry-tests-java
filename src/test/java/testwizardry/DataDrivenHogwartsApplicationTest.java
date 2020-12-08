package testwizardry;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import testwizardry.automation.DriverFactory;
import testwizardry.automation.pages.AcceptancePage;
import testwizardry.automation.pages.ApplicationPage;
import testwizardry.automation.pages.HomePage;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.runners.Parameterized.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

@RunWith(Parameterized.class)
public class DataDrivenHogwartsApplicationTest
{
	WebDriver driver;
	WebDriverWait wait;

	Properties env;
	String baseUrl;
	Integer timeout;

	String name;
	Integer age;
	String gender;
	Boolean magical;
	String expected;

	public static boolean wizard = true;
	public static boolean witch = true;
	public static boolean muggle = false;

	public DataDrivenHogwartsApplicationTest(String name, Integer age, String gender, Boolean magical, String expected)
	{
		this.name = name;
		this.age = age;
		this.gender = gender;
		this.magical = magical;
		this.expected = expected;
	}

	@Before
	public void setup() throws IOException
	{
		env = DriverFactory.loadConfig("safari.test.properties");
		baseUrl = env.getProperty("baseUrl", "http://localhost:8080");
		timeout = Integer.valueOf(env.getProperty("timeout", "30"));

		driver = new DriverFactory().getDriver();
		wait = new WebDriverWait(driver, Duration.ofSeconds(timeout));
	}

	@Test
	public void Hogwarts_Acceptance_Test()
	{
		System.out.println("Applying to Hogwarts: ");
		System.out.println("Name: " + name + " Age: " + age + " Gender: " + gender + " Magical: " + magical + " Expected: " + expected);

		HomePage homePage = new HomePage(driver, env);
		homePage.open().apply.click();

		ApplicationPage application = new ApplicationPage(driver, env);
		application.enterName(name);
		application.enterAge(age);
		application.selectGender(gender);
		if (wizard)
		{
			application.isMagical();
		}
		else
		{
			application.isMuggle();
		}

		application.sendButton.click();
		wait.until(alertIsPresent()).dismiss();

		String title = driver.getTitle();
		System.out.println(title);
		assertThat(title).contains("Accepted");

		AcceptancePage acceptance = new AcceptancePage(driver, env);
		assertThat(acceptance.isLoaded());
		assertThat(acceptance.letter.getText().contains("Congratulations"));

		String surname = name.split(" ", 2)[1];
		System.out.println("surname: " + surname);
		assertThat(acceptance.letter.getText().contains(surname));
	}

	@After
	public void cleanup() throws InterruptedException
	{
		Thread.sleep(2000);
		driver.quit();
	}

	public void getConfig() throws IOException
	{
		Properties config = new Properties();

		ClassLoader classLoader = this.getClass().getClassLoader();
		System.out.println(classLoader);

		InputStream inputStream = classLoader.getResourceAsStream("test.properties");
		System.out.println("inputStream: " + inputStream);

		config.load(inputStream);
		System.out.println("config properties: " + config);
	}

	@Parameters(name="{index}: name: {0}, age: {1}, gender: {2}, magical: {3}, expected: {4} )")
	public static Iterable<Object[]> data() {
		return Arrays.asList(new Object[][] {
				{"Harry Potter", 11, "male", wizard, "accepted"},
				{"Ron Weasley", 11, "male", wizard, "accepted"},
				{"Hermione Granger", 11, "female", witch, "accepted"}, //
				{"Dudley Dursley", 11, "male", muggle, "rejected"}, // not magical
				{"Aaron Evans", 44, "male", wizard, "rejected"}, // too old
		});
	}
}
