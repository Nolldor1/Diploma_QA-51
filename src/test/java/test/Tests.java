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
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    PaymentPage paymentPage = new PaymentPage();
    PurchasePage purchasePage = new PurchasePage();

    @BeforeAll
    public static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @BeforeEach
    public void openPage() {
        open("http://localhost:8080");
    }

    @AfterAll
    public static void tearDownAll() {
        SelenideLogger.removeListener("allure");
        databaseCleanUp();
    }

    @Nested
    //Тесты на оплату и получения кредита по валидной карте:
    public class ValidCard {

    @Test
    @DisplayName("Покупка валидной картой")
    public void shouldPaymentValidCard() {
        var purchasePage = new PurchasePage();
        paymentPage.cardPayment();
        var info = getApprovedCard();
        purchasePage.sendingData(info);
        var expected = "APPROVED";
        var paymentInfo = getPaymentInfo();
        var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице покупок:
        assertEquals(expected, paymentInfo.getStatus());
        //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
        assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        purchasePage.bankApproved();
    }

    @Test
    @DisplayName("Получение кредита на покупку по валидной карте")
    public void shouldCreditValidCard() {
        var purchasePage = new PurchasePage();
        paymentPage.cardCredit();
        var info = getApprovedCard();
        purchasePage.sendingData(info);
        var expected = "APPROVED";
        var creditRequestInfo = getCreditInfo();
        var orderInfo = getOrderInfo();
        //Проверка соответствия статуса в базе данных в таблице запросов кредита:
        assertEquals(expected, creditRequestInfo.getStatus());
        //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
        assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
        //Проверка вывода соответствующего уведомления пользователю на странице покупок:
        purchasePage.bankApproved();
    }
}
    @Nested
    //Тесты на оплату и получения кредита по не валидной карте:
    public class InvalidCard {

        @Test
        @DisplayName("Покупка не валидной картой")
        public void shouldPaymentInvalidCard() {
            var purchasePage = new PurchasePage();
            paymentPage.cardPayment();
            var info = getDeclinedCard();
            purchasePage.sendingData(info);
            //Время отправки данных в базу данных, в секундах:
            //TimeUnit.SECONDS.sleep(10);
            var expected = "DECLINED";
            var paymentInfo = getPaymentInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в базе данных в таблице покупок:
            assertEquals(expected, paymentInfo.getStatus());
            //Проверка соответствия в базе данных id в таблице покупок и в таблице заявок:
            assertEquals(paymentInfo.getTransaction_id(), orderInfo.getPayment_id());
            //Проверка вывода соответствующего уведомления пользователю на странице покупок:
            purchasePage.bankDeclined();
        }

        @Test
        @DisplayName("Получение кредита на покупку по не валидной карте")
        public void shouldCreditInvalidCard() {
            var purchasePage = new PurchasePage();
            paymentPage.cardCredit();
            var info = getDeclinedCard();
            purchasePage.sendingData(info);
            //Время отправки данных в базу данных, в секундах:
            //TimeUnit.SECONDS.sleep(10);
            var expected = "DECLINED";
            var creditRequestInfo = getCreditInfo();
            var orderInfo = getOrderInfo();
            //Проверка соответствия статуса в базе данных в таблице запросов кредита:
            assertEquals(expected, creditRequestInfo.getStatus());
            //Проверка соответствия в базе данных id в таблице запросов кредита и в таблице заявок:
            assertEquals(creditRequestInfo.getBank_id(), orderInfo.getCredit_id());
            //Проверка вывода соответствующего уведомления пользователю на странице покупок:
            purchasePage.bankApproved();
        }
    }

    @Nested
    //Тесты на валидацию полей платежной формы:
    public class PaymentFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            var purchasePage = new PurchasePage();
            paymentPage.cardPayment();

        }

        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmptyCardFields() {
            var purchasePage = new PurchasePage();
            val emptyForm = DataGenerator.getEmptyCardInfo();
            purchasePage.sendingData(emptyForm);
            purchasePage.emptyCardNumberField();
            purchasePage.emptyMonthField();
            purchasePage.emptyYearField();
            purchasePage.emptyOwnerField();
            purchasePage.invalidCVCField();
        }

        @Test
        @DisplayName("Поле 'Номер карты', пустое поле")
        public void shouldEmptyCardNumberField() {
            var purchasePage = new PurchasePage();
            var info = getEmptyCardNumber();
            purchasePage.sendingData(info);
            purchasePage.emptyCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Номер карты', не полный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCardNumber();
            purchasePage.sendingData(info);
            purchasePage.invalidCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyMonthValue();
            purchasePage.sendingData(info);
            purchasePage.emptyMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueMonth();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            var purchasePage = new PurchasePage();
            var info = getCardWithLowerMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            var purchasePage = new PurchasePage();
            var info = getCardWithGreaterMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyYearValue();
            purchasePage.sendingData(info);
            purchasePage.emptyYearField();
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueYear();
            purchasePage.sendingData(info);
            purchasePage.expiredYearField();
        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            var purchasePage = new PurchasePage();
            var info = getCardWithYearFromFuture();
            purchasePage.sendingData(info);
            purchasePage.notificationInvalidYear();
        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyOwnerValue();
            purchasePage.sendingData(info);
            purchasePage.emptyOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpaceOrHyphenOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpecialSymbolsOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithNumbersOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyCVC();
            purchasePage.sendingData(info);
            purchasePage.emptyCVCField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCVC();
            purchasePage.sendingData(info);
            purchasePage.invalidCVCField();
        }
    }

    @Nested
    //Тесты на валидацию полей кредитной формы:
    public class CreditFormFieldValidation {

        @BeforeEach
        public void setPayment() {
            var purchasePage = new PurchasePage();
            paymentPage.cardCredit();
        }

        @Test
        @DisplayName("Отправка пустой формы")
        public void shouldEmptyCardFields() {
            var purchasePage = new PurchasePage();
            val emptyForm = DataGenerator.getEmptyCardInfo();
            purchasePage.sendingData(emptyForm);
            purchasePage.emptyCardNumberField();
            purchasePage.emptyMonthField();
            purchasePage.emptyYearField();
            purchasePage.emptyOwnerField();
            purchasePage.invalidCVCField();
        }

        @Test
        @DisplayName("Поле 'Номер карты', пустое поле")
        public void shouldEmptyCardNumberField() {
            var purchasePage = new PurchasePage();
            var info = getEmptyCardNumber();
            purchasePage.sendingData(info);
            purchasePage.emptyCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Номер карты', не полный номер карты")
        public void shouldCardWithIncompleteCardNumber() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCardNumber();
            purchasePage.sendingData(info);
            purchasePage.invalidCardNumberField();
        }

        @Test
        @DisplayName("Поле 'Месяц', пустое поле")
        public void shouldEmptyMonthField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyMonthValue();
            purchasePage.sendingData(info);
            purchasePage.emptyMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', просроченный месяц")
        public void shouldCardWithOverdueMonth() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueMonth();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', нижнее негативное значение '00'")
        public void shouldCardWithLowerMonthValue() {
            var purchasePage = new PurchasePage();
            var info = getCardWithLowerMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Месяц', верхнее негативное значение '13'")
        public void shouldCardWithGreaterMonthValue() {
            var purchasePage = new PurchasePage();
            var info = getCardWithGreaterMonthValue();
            purchasePage.sendingData(info);
            purchasePage.invalidMonthField();
        }

        @Test
        @DisplayName("Поле 'Год', пустое поле")
        public void shouldEmptyYearField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyYearValue();
            purchasePage.sendingData(info);
            purchasePage.emptyYearField();
        }

        @Test
        @DisplayName("Поле 'Год', просроченный год")
        public void shouldCardWithOverdueYear() {
            var purchasePage = new PurchasePage();
            var info = getCardWithOverdueYear();
            purchasePage.sendingData(info);
            purchasePage.expiredYearField();
        }

        @Test
        @DisplayName("Поле 'Год', год из отдаленного будущего")
        public void shouldCardWithYearFromFuture() {
            var purchasePage = new PurchasePage();
            var info = getCardWithYearFromFuture();
            purchasePage.sendingData(info);
            purchasePage.notificationInvalidYear();
        }

        @Test
        @DisplayName("Поле 'Владелец', пустое поле")
        public void shouldEmptyOwnerField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyOwnerValue();
            purchasePage.sendingData(info);
            purchasePage.emptyOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с пробелом или дефисом")
        public void shouldCardWithSpaceOrHyphenOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpaceOrHyphenOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с несколькими спец символами")
        public void shouldCardWithSpecialSymbolsOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithSpecialSymbolsOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'Владелец', с цифрами")
        public void shouldCardWithNumbersOwner() {
            var purchasePage = new PurchasePage();
            var info = getCardWithNumbersOwner();
            purchasePage.sendingData(info);
            purchasePage.invalidOwnerField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', пустое поле")
        public void shouldEmptyCVCField() {
            var purchasePage = new PurchasePage();
            var info = getCardWithEmptyCVC();
            purchasePage.sendingData(info);
            purchasePage.emptyCVCField();
        }

        @Test
        @DisplayName("Поле 'CVC/CVV', не полный номер")
        public void shouldCardWithIncompleteCVC() {
            var purchasePage = new PurchasePage();
            var info = getCardWithIncompleteCVC();
            purchasePage.sendingData(info);
            purchasePage.invalidCVCField();
        }
        }
    }


