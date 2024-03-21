package ca.uwaterloo.cs346.uwconnect.data.repository.DatabaseRepository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//import java.sql.Connection;
//import java.sql.DriverManager;

public class JobInfoConnection {
    public String JDBC_URL;
    public String USERNAME;
    public String PASSWORD;

    public Connection connection = null;

    private void open_connection() throws SQLException
    {
        connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public JobInfoConnection() throws SQLException
    {
        JDBC_URL = "jdbc:sqlserver://cs346server.database.windows.net:1433;database=JobInfo;encrypt=true;trustServerCertificate=false;hostNameInCertificate=.database.windows.net;loginTimeout=30;";
        USERNAME = "ourlogin@cs346server.database.windows.net";
        PASSWORD = "ukgtKHGVCHTCjhgvkv%^65r^66657897";

        open_connection();
    }

    public void close() throws SQLException
    {
        if (connection != null) {
            connection.close();
        }
    }

    public Integer find_company_pk(String name) throws SQLException
    {
        String query = "SELECT * FROM Company WHERE c_name = ?";

        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, name);

        ResultSet resultSet = statement.executeQuery();

        // Names are unique, so we will only get one result (or zero results)
        if (resultSet.next())
        {
            return resultSet.getInt("c_id");
        }

        return null;
    }
}