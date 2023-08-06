package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.DataGenerator;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.val;
import org.junit.jupiter.api.*;
import page.PaymentPage;
import page.PurchasePage;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.*;
import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    PaymentPage paymentPage;
    PurchasePage purchasePage;

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    void setup() {
        paymentPage = open("http://localhost:8080/", PaymentPage.class);
        purchasePage = new PurchasePage();
    }

    @AfterAll
    public static void tearDownAll() {
        databaseCleanUp();
        SelenideLogger.removeListener("allure");
    }

    @Nested
    //Тесты на оплату и получения кредита по валидной карте:
    public class ValidCard {

        @Test
        @DisplayName("Покупка валидной картой")
        public void shouldPaymentValidCard() {
            paymentPage.cardPayment();
            var info = getApprovedCard();
            purchasePage.sendingData(info);
            var expected = "APPROVED";
            assertAll(() -> purchasePage.bankApproved(),
                    //Проверка соответствия статуса в базе данных в таблице покупок:
                    () -> assertEquals(expected, getPaymentInfo().getStatus()),
                    //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
                    () -> assertEquals(getPaymentInfo().getTransaction_id(), getOrderInfo().getPayment_id()));
        }

        @Test
        @DisplayName("Получение кредита на покупку по валидной карте")
        public void shouldCreditValidCard() {
            paymentPage.cardCredit();
            var info = getApprovedCard();
            purchasePage.sendingData(info);
            var expected = "APPROVED";
            assertAll(() -> purchasePage.bankApproved(),
                    //Проверка соответствия статуса в базе данных в таблице запросов кредита:
                    () -> assertEquals(expected, getCreditInfo().getStatus()),
                    //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
                    () -> assertEquals(getCreditInfo().getBank_id(), getOrderInfo().getCredit_id()));
        }
    }

    @Nested
    //Тесты на оплату и получения кредита по не валидной карте:
    public class InvalidCard {

        @Test
        @DisplayName("Покупка не валидной картой")
        public void shouldPaymentInvalidCard() {
            paymentPage.cardPayment();
            var info = getDeclinedCard();
            purchasePage.sendingData(info);
            var expected = "DECLINED";
            assertAll(() -> purchasePage.bankDeclined(),
                    //Проверка соответствия статуса в базе данных в таблице покупок:
                    () -> assertEquals(expected, getPaymentInfo().getStatus()),
                    //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
                    () -> assertEquals(getPaymentInfo().getTransaction_id(), getOrderInfo().getPayment_id()));

        }

        @Test
        @DisplayName("Получение кредита на покупку по не валидной карте")
        public void shouldCreditInvalidCard() {
            paymentPage.cardCredit();
            var info = getDeclinedCard();
            purchasePage.sendingData(info);
            var expected = "DECLINED";
            assertAll(() -> purchasePage.bankDeclined(),
                    //Проверка соответствия статуса в базе данных в таблице запросов кредита:
                    () -> assertEquals(expected, getCreditInfo().getStatus()),
                    //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
                    () -> assertEquals(getCreditInfo().getBank_id(), getOrderInfo().getCredit_id()));
        }
    }

    @Nested
    //Тесты на валидацию полей платежной формы:
    public class PaymentFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            paymentPage.cardPayment();

        }

        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmptyCardFields() {
            val emptyForm = DataGenerator.getEmptyCardInfo();
            purchasePage.sendingData(emptyForm);
            purchasePage.invalidCardNumberField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Номер карты', пустое поле")
        public void shouldEmptyCardNumberField() {
            var info = getEmptyCardNumber();
            purchasePage.sendingData(info);
            purchasePage.invalidCardNumberField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Номер карты', не полный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            var info = getCardWithIncompleteCardNumber();
            purchasePage.sendingData(info);
            purchasePage.invalidCardNumberField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            var info = getCardWithEmptyMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            var info = getCardWithOverdueMonth();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            var info = getCardWithLowerMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            var info = getCardWithGreaterMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            var info = getCardWithEmptyYearValue();
            purchasePage.sendingData(info);
            purchasePage.invalidYearField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            var info = getCardWithOverdueYear();
            purchasePage.sendingData(info);
            purchasePage.invalidYearField("Истёк срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            var info = getCardWithYearFromFuture();
            purchasePage.sendingData(info);
            purchasePage.invalidYearField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            var info = getCardWithEmptyOwnerValue();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var info = getCardWithSpaceOrHyphenOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            var info = getCardWithSpecialSymbolsOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            var info = getCardWithNumbersOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            var info = getCardWithEmptyCVC();
            purchasePage.sendingData(info);
            purchasePage.invalidCVCField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            var info = getCardWithIncompleteCVC();
            purchasePage.sendingData(info);
            purchasePage.invalidCVCField("Неверный формат");
        }
    }

    @Nested
    //Тесты на валидацию полей кредитной формы:
    public class CreditFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            paymentPage.cardCredit();
        }

        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmptyCardFields() {
            var emptyForm = DataGenerator.getEmptyCardInfo();
            purchasePage.sendingData(emptyForm);
            purchasePage.invalidCardNumberField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Номер карты', пустое поле")
        public void shouldEmptyCardNumberField() {
            var info = getEmptyCardNumber();
            purchasePage.sendingData(info);
            purchasePage.invalidCardNumberField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Номер карты', не полный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            var info = getCardWithIncompleteCardNumber();
            purchasePage.sendingData(info);
            purchasePage.invalidCardNumberField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            var info = getCardWithEmptyMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            var info = getCardWithOverdueMonth();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            var info = getCardWithLowerMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            var info = getCardWithGreaterMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            var info = getCardWithEmptyYearValue();
            purchasePage.sendingData(info);
            purchasePage.invalidYearField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            var info = getCardWithOverdueYear();
            purchasePage.sendingData(info);
            purchasePage.invalidYearField("Истёк срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            var info = getCardWithYearFromFuture();
            purchasePage.sendingData(info);
            purchasePage.invalidYearField("Неверно указан срок действия карты");
        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            var info = getCardWithEmptyOwnerValue();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var info = getCardWithSpaceOrHyphenOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            var info = getCardWithSpecialSymbolsOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            var info = getCardWithNumbersOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField("Неверный формат");
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            var info = getCardWithEmptyCVC();
            purchasePage.sendingData(info);
            purchasePage.invalidCVCField("Поле обязательно для заполнения");
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            var info = getCardWithIncompleteCVC();
            purchasePage.sendingData(info);
            purchasePage.invalidCVCField("Неверный формат");
        }
    }
}


