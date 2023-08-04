package page;

import com.codeborne.selenide.SelenideElement;
import data.DataGenerator.CardInfo;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class PurchasePage {

    //Форма для ввода данных, со всеми полями ввода, ошибками полей и кнопкой "Продолжить":
    private final SelenideElement cardNumberField = $("input[placeholder='0000 0000 0000 0000']");
    private final SelenideElement cardNumberFieldError = $x("//*[text()='Номер карты']/..//*[@class='input__sub']");
    private final SelenideElement monthField = $("input[placeholder='08']");
    private final SelenideElement monthFieldError = $x("//*[text()='Месяц']/..//*[@class='input__sub']");
    private final SelenideElement yearField = $("input[placeholder='22']");
    private final SelenideElement yearFieldError = $x("//*[text()='Год']/..//*[@class='input__sub']");
    private final SelenideElement ownerField = $(byText("Владелец")).parent().$("input");
    private final SelenideElement ownerFieldError = $x("//*[text()='Владелец']/..//*[@class='input__sub']");
    private final SelenideElement cvcField = $("input[placeholder='999']");
    private final SelenideElement cvcFieldError = $x("//*[text()='CVC/CVV']/..//*[@class='input__sub']");
    private final SelenideElement notificationSuccessfully = $(".notification_status_ok");
    private final SelenideElement notificationError = $(".notification_status_error");
    private final SelenideElement continueButton = $("form button");

    public void sendingData(CardInfo info) {
        cardNumberField.setValue(info.getNumberCard());
        monthField.setValue(info.getMonth());
        yearField.setValue(info.getYear());
        ownerField.setValue(info.getOwner());
        cvcField.setValue(info.getCvc());
        continueButton.click();
    }

    public void invalidCardNumberField(String expectedErrorMessage) {
        cardNumberFieldError.shouldHave(exactText(expectedErrorMessage)).shouldBe(visible);
    }

    public void invalidMonthField(String expectedErrorMessage) {
        monthFieldError.shouldHave(exactText(expectedErrorMessage)).shouldBe(visible);
    }

    public void invalidYearField(String expectedErrorMessage) {
        yearFieldError.shouldHave(exactText(expectedErrorMessage)).shouldBe(visible);

    }

    public void invalidCVCField(String expectedErrorMessage) {
        cvcFieldError.shouldHave(exactText(expectedErrorMessage)).shouldBe(visible);
    }

    public void invalidOwnerField(String expectedErrorMessage) {
        ownerFieldError.shouldHave(exactText(expectedErrorMessage)).shouldBe(visible);

    }


    public void bankApproved() {
        notificationSuccessfully.shouldBe(visible, Duration.ofSeconds(15));
    }

    public void bankDeclined() {
        notificationError.shouldBe(visible, Duration.ofSeconds(15));
    }
}