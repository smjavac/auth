package ru.said.service;

import ru.said.bean.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static ru.said.Сompound.getConnection;

public class UserService {
    private Connection connection = getConnection();
    private List<User> usersList;

            public void Connections (String query) throws SQLException {
                try (Connection connection = getConnection();
                     Statement statement = connection.createStatement()) {
                    statement.executeUpdate(query);
                } // finally { if (connection != null) connection.close(); }
            }

            public List<User> getAll() throws SQLException {

                usersList = new ArrayList<>();
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
                } finally { if (connection !=null) connection.close(); }
                return usersList;
            }

            public List<User> addRow(String userName, String login, String password) throws SQLException{
                usersList = new ArrayList<>();
                String query = "INSERT INTO ddt_users (user_name, login, password) VALUES ('"+ userName +"', '"+ login +"', '"+ password +"')";
                Connections(query);
                return usersList = getAll();

            }

            public List<User> deleteRow(String userName) throws SQLException{
                usersList = new ArrayList<>();
                String query = "DELETE from ddt_users where user_name ='"+ userName+"'";
                Connections(query);
                return usersList = getAll();
            }

            public List<User> editRow(String userName, String login, String password) throws SQLException {
                usersList = new ArrayList<>();
                String query = "UPDATE ddt_users SET login ='"+ login +"'," +
                        " password = '"+ password +"' where user_name = '" + userName +"'";
                Connections(query);
                return usersList = getAll();
            }

            public boolean  authentication (String log, String pass) throws SQLException, NoSuchAlgorithmException {
               try (Connection connection = getConnection();
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT * from ddt_users")){
                    while (resultSet.next()){
                        if (hashPass(pass).equals(resultSet.getString("password"))
                            && log.equals(resultSet.getString("login"))){
                            return true;
                        }
                        System.out.println(hashPass(pass));
                        System.out.println(resultSet.getString("password"));
                    }

               }finally { if (connection != null) connection.close(); }
                return false;
            }

            public String hashPass(String hashpass) throws NoSuchAlgorithmException {
                MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
                byte[] bytes = sha256.digest(hashpass.getBytes());
                StringBuilder strBuilder = new StringBuilder();
                for (byte b : bytes )
                strBuilder.append(b);
                return strBuilder.toString();
            }
    }
