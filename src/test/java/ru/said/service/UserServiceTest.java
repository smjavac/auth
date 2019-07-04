package ru.said.service;

import java.sql.SQLException;
import junit.framework.Assert;
import org.junit.Test;
import ru.said.bean.User;

import static org.mockito.Mockito.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
        when(resultSet.next()).thenReturn(false);//задаем поведение для resultSet

        List<User> userList = UserService.getAll(connection);
        List<User> userList1 = new ArrayList<>();

        Assert.assertEquals(userList1, userList);
    }
}
