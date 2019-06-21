package ru.said.service;

import ru.said.SecurityUtils;
import ru.said.bean.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.said.DatabaseUtils.getConnection;

public class UserService {

//            public void execute(String query) throws SQLException {
//                try (Connection connection = getConnection();
//
//                     Statement statement = connection.createStatement()) {
//                    statement.executeUpdate(query);
//                }
//            }

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
                try (PreparedStatement prepState = getConnection().prepareStatement(
                        "INSERT INTO ddt_users (user_name, login, password) VALUES (?,?,?)")){
                        prepState.setString(1, userName);
                        prepState.setString(2, login);
                        prepState.setString(3, password);
                        prepState.execute();
                }
                return getAll();
            }

            public List<User> deleteRow(String userName) throws SQLException{
                try (PreparedStatement prepDelete = getConnection().prepareStatement("DELETE from ddt_users where user_name = ?")){
                    prepDelete.setString(1, userName);
                    prepDelete.execute();
                }
                return getAll();
            }

            public List<User> editRow(String userNameNew, String loginNew, String passwordNew) throws SQLException {
                try (PreparedStatement prepEdit = getConnection().prepareStatement("UPDATE ddt_users SET login = ?, password = ? where user_name = ?")) {
                    prepEdit.setString(1, loginNew);
                    prepEdit.setString(2, passwordNew);
                    prepEdit.setString(3, userNameNew);
                    prepEdit.execute();
                }
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
