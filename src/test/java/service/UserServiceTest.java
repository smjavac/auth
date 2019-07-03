package service;

import org.junit.Test;
import junit.framework.Assert;
import ru.said.bean.User;
import ru.said.service.UserService;

import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.ArrayList;

public class UserServiceTest {

    @Test
    public void testGetAll() throws SQLException {
        Connection connection = mock(Connection.class); //создаем mock для Connection
        Statement statement = mock(Statement.class); //создаем mock для Statement
        ResultSet resultSet = mock(ResultSet.class); //создаем mock для ResultSet

        when(connection.createStatement()).thenReturn(statement);
        String sql = "SELECT * FROM  ddt_users";
        when(statement.executeQuery(sql)).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);
        ArrayList<User> list = UserService.getAll(connection,)
    }
}
