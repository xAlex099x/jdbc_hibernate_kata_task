package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UserDaoJDBCImpl implements UserDao {

    private final static UserDao INSTANCE;
    Logger LOGGER = Logger.getLogger("DAO LOGGER");

    private static final String CREATE_TABLE_SQL = """
            CREATE TABLE user (
            id BIGINT AUTO_INCREMENT PRIMARY KEY ,
            name VARCHAR(128) NOT NULL ,
            lastname VARCHAR(128) NOT NULL ,
            age TINYINT CHECK ( age > 0 ));
            """;

    private static final String DROP_TABLE_SQL = """
            DROP TABLE user
            """;
    private static final String SAVE_SQL = """
            INSERT INTO user(name, lastname, age)
            VALUES (?, ?, ?)
            """;

    private static final String REMOVE_BY_ID_SQL = """
            DELETE FROM user
            WHERE id = ?
            """;

    private static final String SELECT_ALL_SQL = """
            SELECT id, name, lastname, age
            FROM user
            """;

    private static final String CLEAN_TABLE_SQL = """
            TRUNCATE user
            """;

    static {
        INSTANCE = new UserDaoJDBCImpl();
    }

    public UserDaoJDBCImpl() {

    }

    public static UserDao getInstance() {
        return INSTANCE;
    }

    public void createUsersTable() {
        try (var connection = Util.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CREATE_TABLE_SQL)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Table already exists!");
        }
    }

    public void dropUsersTable() {
        try (var connection = Util.open();
             PreparedStatement preparedStatement = connection.prepareStatement(DROP_TABLE_SQL)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Table missing!");
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (var connection = Util.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SAVE_SQL)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeUserById(long id) {
        try (var connection = Util.open();
             PreparedStatement preparedStatement = connection.prepareStatement(REMOVE_BY_ID_SQL)) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (var connection = Util.open();
             PreparedStatement preparedStatement = connection.prepareStatement(SELECT_ALL_SQL)) {

            ResultSet result = preparedStatement.executeQuery();
            int counter = 0;
            while (result.next()) {
                list.add(new User(
                        result.getString("name"),
                        result.getString("lastname"),
                        result.getByte("age")));
                list.get(counter).setId(result.getLong("id"));
                counter++;
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanUsersTable() {
        try (var connection = Util.open();
             PreparedStatement preparedStatement = connection.prepareStatement(CLEAN_TABLE_SQL)) {

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
