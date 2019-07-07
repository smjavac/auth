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
   //     when(resultSet.next()).thenReturn(false);//задаем поведение для resultSet
        when(resultSet.next()).thenReturn(true, false);
        List<User> userList = UserService.getAll(connection);
        List<User> userList2 = new ArrayList<User>();
        userList2.add(new User());

        Assert.assertEquals(userList2.size(), userList.size());

       // verify(resultSet).next();
      //  verify(resultSet).close();
      //  verify(statement).close();
    }


}
