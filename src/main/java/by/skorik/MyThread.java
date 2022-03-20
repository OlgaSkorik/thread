package by.skorik;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class MyThread extends Thread {

    public MyThread(String name) {
        super(name);
    }

    public static final String url = "jdbc:mysql://localhost:3306/bank";
    public static final String user = "root";
    public static final String password = "8893960";

    @Override
    public void run() {
        try (Connection connection = DriverManager.getConnection(url, user, password);
             FileWriter writer = new FileWriter("./text_file")) {

            PreparedStatement preparedStatement = connection.prepareStatement(url);
            ResultSet set = preparedStatement.executeQuery("select client_first_name, age, number_account, currency, employee_first_name\n" +
                    "from `client`, customer_accounts, currency_reference, employee\n" +
                    "where customer_accounts.client_id = client.id and\n" +
                    "customer_accounts.employee_id = employee.id and\n" +
                    "customer_accounts.currency_reference_id = currency_reference.id");
            String text = "";
            while (set.next()) {
                text = String.format("client_first_name: %s, age: %d, number_account: %s," +
                                "currency: %s, employee_first_name: %s.\n",
                        set.getString(1), set.getLong(2),
                        set.getString(3), set.getString(4),
                        set.getString(5));
                writer.write(text);
            }
        } catch (SQLException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
