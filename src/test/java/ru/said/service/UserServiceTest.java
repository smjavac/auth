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
    public void testGetall () throws SQLException {
        Connection connection = mock(Connection.class); //создаем mock для Connection
        Statement statement = mock(Statement.class); //создаем mock для Statement
        ResultSet resultSet = mock(ResultSet.class); //создаем mock для ResultSet

        when(connection.createStatement()).thenReturn(statement); //связываем connection и statement
        String sql = "SELECT * FROM  ddt_users";
        when(statement.executeQuery(sql)).thenReturn(resultSet); //связываем  statement и resultSet
        when(resultSet.next()).thenReturn(true, false);

        List<User> userListFromUserService = UserService.getAll(connection);
        List<User> userList1 = new ArrayList<User>();
        userList1.add(new User());

     //   Assert.assertEquals(userList1, userListFromUserService);
        Assert.assertEquals(userList1.size(), userListFromUserService.size());

        verify(resultSet, atLeastOnce()).next();//проверяем, что вызывался метод next() у resulSet
        verify(resultSet).close();//проверяем, что resultSet закрыли после использования
        verify(statement).close();//проверяем, что statement закрыли после использования
    }


    @Test
    public void test2Getall () throws SQLException {
        Connection connection = mock(Connection.class);
        Statement statement = mock(Statement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.createStatement()).thenReturn(statement);
        String sql = "SELECT * FROM  ddt_users";
        when(statement.executeQuery(sql)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        List<User> userListFromUserService = UserService.getAll(connection);
        List<User> userList = new ArrayList<User>();

        Assert.assertEquals(userList, userListFromUserService);

        verify(resultSet).next();
        verify(resultSet).close();
        verify(statement).close();
    }


}
