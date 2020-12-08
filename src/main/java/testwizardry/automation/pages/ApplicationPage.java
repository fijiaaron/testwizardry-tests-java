package testwizardry.automation.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.Properties;

import static org.openqa.selenium.support.ui.ExpectedConditions.alertIsPresent;

public class ApplicationPage extends TestWizardryPage
{
	public String path ="/apply.html";
	public String title = "TestWizardry - Apply";

	public ApplicationPage(WebDriver driver, Properties env)
	{
		super(driver, env);
	}

	@FindBy(id="character_name")
	WebElement nameField;

	@FindBy(id="character_age")
	WebElement ageField;

	@FindBy(id="character_gender")
	WebElement genderSelect;

	@FindBy(name="character_status")
	List<WebElement> magicalToggle;

	@FindBy(css = "[value='Send Application']")
	public WebElement sendButton;

	public ApplicationPage enterName(String name)
	{
		System.out.println("enterName: " + name + " " + nameField);
		nameField.sendKeys(name);
		return this;
	}

	public ApplicationPage whenLoaded()
	{
		if (isLoaded())
		{
			return this;
		}

		throw new PageException("not on page: " + this);
	}

	public ApplicationPage enterAge(int age)
	{
		System.out.println("enterAge: " + age + " " + ageField);
		ageField.sendKeys(String.valueOf(age));
		return this;
	}

	public ApplicationPage selectGender(String gender)
	{
		System.out.println("selectGender: " + gender + " " + genderSelect);
		new Select(genderSelect).selectByValue(gender);
		return this;
	}

	public ApplicationPage isMagical(boolean value)
	{
		if (value == true)
		{
			return isMagical();
		}
		else
		{
			return isMuggle();
		}
	}

	public void sendApplication()
	{
		sendButton.click();

		pause(2);
		wait.until(alertIsPresent()).dismiss();
	}

	public ApplicationPage isMagical()
	{
		List<WebElement> options = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector("[name=character_status]")));
		WebElement magical = options.get(0);
		System.out.println("isMagical: " + magical.getAttribute("value"));

		options.get(0).click();

//		driver.findElements(By.name("character_status"))
//				.stream().filter(element ->
//				element.getAttribute("value").equalsIgnoreCase("Wizard")
//		).findAny().ifPresent(WebElement::click);

		return this;
	}

	public ApplicationPage isMuggle()
	{
		System.out.println("isMuggle");

		driver.findElements(By.name("character_status"))
				.stream().filter(element ->
				element.getAttribute("value").equalsIgnoreCase("Muggle")
		).findAny().ifPresent(WebElement::click);

		return this;
	}
}
