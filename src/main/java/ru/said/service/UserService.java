package ru.said.service;

import ru.said.SecurityUtils;
import ru.said.bean.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.said.DatabaseUtils.getConnection;

public class UserService {

            public void execute(String query) throws SQLException {
                try (Connection connection = getConnection();
                     Statement statement = connection.createStatement()) {
                    statement.executeUpdate(query);
                }
            }

            public List<User> getAll() throws SQLException {

                List<User> usersList = new ArrayList<>();
                String query = "SELECT * FROM  ddt_users";
                try (Connection connection = getConnection();
                     Statement statement = connection.createStatement();
                     ResultSet resultSet = statement.executeQuery(query)){
                    while (resultSet.next()){
                        User user = new User();
                        user.setUserName(resultSet.getString("user_name"));
                        user.setLogin(resultSet.getString("login"));
                        user.setPassword(resultSet.getString("password"));
                        usersList.add(user);
                    }
                }
                return usersList;
            }

            public List<User> addRow(String userName, String login, String password) throws SQLException{
                String query = "INSERT INTO ddt_users (user_name, login, password) VALUES ('"+ userName +"', '"+ login +"', '"+ password +"')";
                execute(query);
                return getAll();

            }

            public List<User> deleteRow(String userName) throws SQLException{
                String query = "DELETE from ddt_users where user_name ='"+ userName+"'";
                execute(query);
                return getAll();
            }

            public List<User> editRow(String userName, String login, String password) throws SQLException {
                String query = "UPDATE ddt_users SET login ='"+ login +"'," +
                        " password = '"+ password +"' where user_name = '" + userName +"'";
                execute(query);
                return getAll();
            }

            public boolean  authentication (String log, String pass) throws SQLException, NoSuchAlgorithmException {
               try (Connection connection = getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * from ddt_users")){
                    while (resultSet.next()){
                        if (SecurityUtils.hash(pass).equals(resultSet.getString("password"))
                            && log.equals(resultSet.getString("login"))){
                            return true;
                        }
                    }
               }
                return false;
            }

    }
