package data;

import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;

    public class DataHelper {
        private static final String datasource = System.getProperty("db.url");

        public static void databaseCleanUp() {
            val runner = new QueryRunner();
            val deleteFromOrder = "DELETE FROM order_entity;";
            val deleteFromCredit = "DELETE FROM credit_request_entity;";
            val deleteFromPayment = "DELETE FROM payment_entity;";

            try (val connection = DriverManager.getConnection(datasource, "app", "pass")) {
                runner.update(connection, deleteFromOrder);
                runner.update(connection, deleteFromCredit);
                runner.update(connection, deleteFromPayment);
            } catch (Exception e) {
                throw new RuntimeException("SQL exception in databaseCleanUp", e);
            }
        }

        public static CreditInfo getCreditInfo() {
            var runner = new QueryRunner();
            var creditInfo = "SELECT * FROM credit_request_entity WHERE created = (SELECT MAX(created) FROM credit_request_entity);";

            try (var connection = DriverManager.getConnection(
                    datasource, "app", "pass")) {
                return runner.query(connection, creditInfo, new BeanHandler<>(CreditInfo.class));
            } catch (Exception e) {
                throw new RuntimeException("SQL exception in databaseCleanUp", e);
            }
        }

        public static PaymentInfo getPaymentInfo() {
            var runner = new QueryRunner();
            var paymentInfo = "SELECT * FROM payment_entity WHERE created = (SELECT MAX(created) FROM payment_entity);";

            try (var connection = DriverManager.getConnection(
                    datasource, "app", "pass")) {
                return runner.query(connection, paymentInfo, new BeanHandler<>(PaymentInfo.class));
            } catch (Exception e) {
                throw new RuntimeException("SQL exception in databaseCleanUp", e);
            }
        }

        public static OrderInfo getOrderInfo() {
            var runner = new QueryRunner();
            var orderInfo = "SELECT * FROM order_entity WHERE created = (SELECT MAX(created) FROM order_entity);";

            try (var connection = DriverManager.getConnection(
                    datasource, "app", "pass")) {
                return runner.query(connection, orderInfo, new BeanHandler<>(OrderInfo.class));
            } catch (Exception e) {
                throw new RuntimeException("SQL exception in databaseCleanUp", e);
            }
        }
    }