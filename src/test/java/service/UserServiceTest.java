package service;

import org.junit.Test;
import junit.framework.Assert;
import ru.said.bean.User;
import ru.said.service.UserService;

import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserServiceTest {
    @Test
    public void testGetAll() throws SQLException {
        Connection connection = mock(Connection.class); //создаем mock для Connection
        Statement statement = mock(Statement.class); //создаем mock для Statement
        ResultSet resultSet = mock(ResultSet.class); //создаем mock для ResultSet

        when(connection.createStatement()).thenReturn(statement); //связываем connection и statement
        String sql = "SELECT * FROM  ddt_users";
        when(statement.executeQuery(sql)).thenReturn(resultSet); //связываем  statement и resultSet
        when(resultSet.next()).thenReturn(false);//задаем поведение для resultSet
        when(resultSet.next()).thenReturn(true, false);
        List<User> userList = UserService.getAll(connection);
        List<User> userList3 = UserService.getAll(connection);
        List<User> userList1 = new ArrayList<User>();
        List<User> userList2 = new ArrayList<User>();
        userList2.add(new User());

        Assert.assertEquals(userList2.size(), userList.size());
        Assert.assertEquals(userList1, userList3);
      //  verify(resultSet).next();//проверяем, что вызывался метод next() у resulSet
        verify(resultSet).close();//проверяем, что resultSet закрыли после использования
        verify(statement).close();//проверяем, что statement закрыли после использования
    }


}
