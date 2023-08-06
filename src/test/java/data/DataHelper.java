package data;

import lombok.SneakyThrows;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.Connection;
import java.sql.DriverManager;


public class DataHelper {
    private static Connection connection;
    private static QueryRunner runner;

    @SneakyThrows
    public static void initConnection() {
        runner = new QueryRunner();
        connection = DriverManager.getConnection(System.getProperty("db.url"));
    }

    @SneakyThrows
    public static void databaseCleanUp() {
        initConnection();
        val runner = new QueryRunner();
        val deleteFromOrder = "DELETE FROM order_entity;";
        val deleteFromCredit = "DELETE FROM credit_request_entity;";
        val deleteFromPayment = "DELETE FROM payment_entity;";
        runner.update(connection, deleteFromOrder);
        runner.update(connection, deleteFromCredit);
        runner.update(connection, deleteFromPayment);
    }

    @SneakyThrows
    public static CreditInfo getCreditInfo() {
        initConnection();
        var runner = new QueryRunner();
        var creditInfo = "SELECT * FROM credit_request_entity WHERE created = (SELECT MAX(created) FROM credit_request_entity);";
        return runner.query(connection, creditInfo, new BeanHandler<>(CreditInfo.class));
    }

    @SneakyThrows
    public static PaymentInfo getPaymentInfo() {
        initConnection();
        var runner = new QueryRunner();
        var paymentInfo = "SELECT * FROM payment_entity WHERE created = (SELECT MAX(created) FROM payment_entity);";
        return runner.query(connection, paymentInfo, new BeanHandler<>(PaymentInfo.class));
    }

    @SneakyThrows
    public static OrderInfo getOrderInfo() {
        initConnection();
        var runner = new QueryRunner();
        var orderInfo = "SELECT * FROM order_entity WHERE created = (SELECT MAX(created) FROM order_entity);";
            return runner.query(connection, orderInfo, new BeanHandler<>(OrderInfo.class));
    }
}