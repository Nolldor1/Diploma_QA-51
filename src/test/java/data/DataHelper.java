package data;

import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;

    public class DataHelper {
        private static final String datasource = System.getProperty("datasource");

        @SneakyThrows
        public static void databaseCleanUp() {
            var runner = new QueryRunner();
            var deleteFromOrder = "DELETE FROM order_entity;";
            var deleteFromCredit = "DELETE FROM credit_request_entity;";
            var deleteFromPayment = "DELETE FROM payment_entity;";

            try (var connection = DriverManager.getConnection(
                    datasource, "app", "pass")) {
                runner.update(connection, deleteFromOrder);
                runner.update(connection, deleteFromCredit);
                runner.update(connection, deleteFromPayment);
            }
        }

        @SneakyThrows
        public static CreditInfo getCreditInfo() {
            var runner = new QueryRunner();
            var creditInfo = "SELECT * FROM credit_request_entity WHERE created = (SELECT MAX(created) FROM credit_request_entity);";

            try (var connection = DriverManager.getConnection(
                    datasource, "app", "pass")) {
                return runner.query(connection, creditInfo, new BeanHandler<>(CreditInfo.class));
            }
        }

        @SneakyThrows
        public static PaymentInfo getPaymentInfo() {
            var runner = new QueryRunner();
            var paymentInfo = "SELECT * FROM payment_entity WHERE created = (SELECT MAX(created) FROM payment_entity);";

            try (var connection = DriverManager.getConnection(
                    datasource, "app", "pass")) {
                return runner.query(connection, paymentInfo, new BeanHandler<>(PaymentInfo.class));
            }
        }

        @SneakyThrows
        public static OrderInfo getOrderInfo() {
            var runner = new QueryRunner();
            var orderInfo = "SELECT * FROM order_entity WHERE created = (SELECT MAX(created) FROM order_entity);";

            try (var connection = DriverManager.getConnection(
                    datasource, "app", "pass")) {
                return runner.query(connection, orderInfo, new BeanHandler<>(OrderInfo.class));
            }
        }
    }

