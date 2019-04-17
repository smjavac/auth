package ru.said.service;

import ru.said.Util2;
import ru.said.bean.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserService extends Util2 {
    private Connection connection = getConnection();
    private List<User> usersList;

            public void Connections (String sql) throws SQLException {
                try (Connection connection = getConnection();
                     Statement statement = connection.createStatement()) {
                    statement.executeUpdate(sql);
                } finally { if (connection != null) connection.close(); }
            }

            public List<User> getall() throws SQLException {

                usersList = new ArrayList<>();
                String sql = "SELECT * FROM  ddt_users";
                try (Connection connection = getConnection();
                     Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(sql)){
                    while (resultSet.next()){
                        User user = new User();
                        user.setUser_name(resultSet.getString("user_name"));
                        user.setLogin(resultSet.getString("login"));
                        user.setPassword(resultSet.getString("password"));
                        usersList.add(user);
                    }
                } finally { if (connection !=null) connection.close(); }
                return usersList;
            }

            public List<User> addRow(String userName, String login, String password) throws SQLException{
                usersList = new ArrayList<>();
                String sql = "INSERT INTO ddt_users (user_name, login, password) VALUES ('"+ userName +"', '"+ login +"', '"+ password +"')";
                Connections(sql);
                return usersList = getall();

            }

            public List<User> deleteRow(String userName) throws SQLException{
                usersList = new ArrayList<>();
                String sql = "DELETE from ddt_users where user_name ='"+ userName+"'";
                Connections(sql);
                return usersList = getall();
            }

            public List<User> editRow(String userName, String login, String password) throws SQLException {
                usersList = new ArrayList<>();
                String sql = "UPDATE ddt_users SET user_name = '"+ userName +"', login ='"+ login +"', password = '"+ password +"'";
                Connections(sql);
                return usersList = getall();

            }
}
