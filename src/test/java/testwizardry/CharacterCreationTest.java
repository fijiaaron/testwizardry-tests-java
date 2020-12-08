package testwizardry;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CharacterCreationTest extends TestWizardryTest
{
	@Test
	public void wizard_should_be_accepted()
	{
		testWizardry.homePage()
				.open()
				.clickApplyNow();

		pause(2);

		testWizardry.applicationPage()
				.enterName("Harry Potter")
				.enterAge(11)
				.selectGender("male")
				.isMagical()
				.sendApplication();

		pause(2);

		assertThat(testWizardry.acceptancePage().isLoaded());
		String letter = testWizardry.acceptancePage().getLetter();
		assertThat(letter.contains("Congratulations"));
		assertThat(letter.contains("Mr. Potter"));
	}

	@Test
	public void muggle_should_be_rejected()
	{
		testWizardry.homePage()
				.open()
				.whenLoaded()
				.clickApplyNow();

		pause(2);

		testWizardry.applicationPage()
				.whenLoaded()
				.enterName("Dudley Dursley")
				.enterAge(11)
				.selectGender("male")
				.isMuggle()
				.sendApplication();

		pause(2);

		assertThat(testWizardry.rejectionPage().isLoaded());
	}
}
