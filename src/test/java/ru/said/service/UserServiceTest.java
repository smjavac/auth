package ru.said.service;

import java.sql.*;

import junit.framework.Assert;
import org.junit.Test;
import ru.said.bean.User;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;


public class UserServiceTest {

    @Test
    public void test1GetAll() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        String sql = "SELECT * FROM  ddt_users";
        when(statement.executeQuery(sql)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<User> userListFromUserService = UserService.getAll(connection);

        // Assert.assertEquals(userList, userListFromUserService);
        Assert.assertTrue(userListFromUserService.isEmpty());
        verify(resultSet).next();
        verify(resultSet).close();
        verify(statement).close();
    }

    @Test
    public void test2GetAll() throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);
       
        when(connection.createStatement()).thenReturn(statement);
        String sql = "SELECT * FROM  ddt_users";
        when(statement.executeQuery(sql)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);
        when(resultSet.getString("user_name")).thenReturn("said");
        when(resultSet.getString("login")).thenReturn("user");
        when(resultSet.getString("password")).thenReturn("12345678");

        List<User> userListFromUserService = UserService.getAll(connection);
        User user = userListFromUserService.get(0);
        Assert.assertEquals(1, userListFromUserService.size());
        Assert.assertEquals("said", user.getUserName());
        Assert.assertEquals("user", user.getLogin());
        Assert.assertEquals("12345678", user.getPassword());

        verify(resultSet, atLeastOnce()).next();
        verify(resultSet).close();
        verify(statement).close();
    }


}
