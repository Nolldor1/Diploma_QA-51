package page;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;

import static com.codeborne.selenide.Selenide.$;

public class PaymentPage {
    //Кнопка "Купить" и заголовок "Оплата по карте":
    private final SelenideElement buyButton = $(byText("Купить"));
    private final SelenideElement buyHeading = $(byText("Оплата по карте"));
    //Кнопка "Купить в кредит" и заголовок "Кредит по данным карты":
    private final SelenideElement creditButton = $(byText("Купить в кредит"));
    private final SelenideElement creditHeading = $(byText("Кредит по данным карты"));

    public void cardPayment() {
        buyButton.click();
        buyHeading.shouldBe(visible);
    }

    public void cardCredit() {
        creditButton.click();
        creditHeading.shouldBe(visible);
    }

}
