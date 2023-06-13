package Tests;

import com.codeborne.selenide.logevents.SelenideLogger;
import data.PurchasePage;
import io.qameta.allure.selenide.AllureSelenide;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import java.util.concurrent.TimeUnit;

import static com.codeborne.selenide.Selenide.open;
import static data.DataGenerator.getApprovedCard;
import static data.DataHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
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
    @SneakyThrows
    @DisplayName("Покупка валидной картой")
    public void shouldPaymentValidCard() {
        var purchasePage = new PurchasePage();
        purchasePage.cardPayment();
        var info = getApprovedCard();
        purchasePage.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(10);
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
    @SneakyThrows
    @DisplayName("Получение кредита на покупку по валидной карте")
    public void shouldCreditValidCard() {
        var purchasePage = new PurchasePage();
        purchasePage.cardCredit();
        var info = getApprovedCard();
        purchasePage.sendingData(info);
        //Время отправки данных в базу данных, в секундах:
        TimeUnit.SECONDS.sleep(10);
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
}


