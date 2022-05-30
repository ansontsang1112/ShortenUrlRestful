package com.hypernology.shortenurlrestful.repository;

import com.hypernology.shortenurlrestful.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private JdbcOperations jdbcOperations;
    private static final String TABLE = "user_info";

    @Autowired
    UserRepositoryImpl(DataSource dataSource) {
        jdbcOperations = new JdbcTemplate(dataSource);
    }

    public static final class UserExtractor implements ResultSetExtractor<List<User>> {

        @Override
        public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<String, User> hashMap = new HashMap<>();

            while(resultSet.next()) {
                String uid = resultSet.getString("uid");
                User user = hashMap.get(uid);
                if(user == null) {
                    user = new User();
                    user.setUid(uid);
                    user.setUsername(resultSet.getString("username"));
                    user.setDiscordId(resultSet.getString("discord_id"));
                    user.setMemberId(resultSet.getString("member_id"));
                    user.setEmail(resultSet.getString("email"));
                    user.setTimestamp(resultSet.getString("timestamp"));
                    user.setStatus(resultSet.getString("status"));
                    hashMap.put(uid, user);
                }
            }
            return new ArrayList<>(hashMap.values());
        }
    }

    @Override
    public List<User> queryAllUser(boolean showDisable) {
        String statement = "SELECT * FROM " + TABLE + " WHERE status = 'Active'";
        if(showDisable) {
            statement = "SELECT * FROM " + TABLE;
        }

        return jdbcOperations.query(statement, new UserExtractor());
    }

    @Override
    public <T> List<User> queryUserByID(T id) {
        String statement = "SELECT * FROM " + TABLE + " WHERE uid = ?";
        return jdbcOperations.query(statement, new UserExtractor(), id.toString());
    }

    @Override
    public <T> List<User> queryUserByDiscordId(T discordId) {
        String statement = "SELECT * FROM " + TABLE + " WHERE discord_id = ?";
        return jdbcOperations.query(statement, new UserExtractor(), discordId.toString());
    }

    @Override
    public <T> List<User> queryUserByMemberId(T memberId) {
        String statement = "SELECT * FROM " + TABLE + " WHERE member_id = ?";
        return jdbcOperations.query(statement, new UserExtractor(), memberId.toString());
    }

    @Override
    public <F, K> List<User> queryUserByGeneralQuery(F field, K key) {
        String statement = "SELECT * FROM " + TABLE + " WHERE " + field.toString() + " = ?";
        return jdbcOperations.query(statement, new UserExtractor(), key.toString());
    }

    @Override
    public User save(User user) {
        String statement = "INSERT INTO " + TABLE + " VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(statement);
                preparedStatement.setString(1, user.getUid());
                preparedStatement.setString(2, user.getUsername());
                preparedStatement.setString(3, user.getDiscordId());
                preparedStatement.setString(4, user.getMemberId());
                preparedStatement.setString(5, user.getEmail());
                preparedStatement.setString(6, user.getTimestamp());
                preparedStatement.setString(7, user.getStatus());
                return preparedStatement;
            }
        });

        return user;
    }

    @Override
    public <T, V, R> String update(T field, V value, R key) {
        String statement = "UPDATE " + TABLE + " SET " + field.toString() + "=? WHERE uid=?";
        jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(statement);
                preparedStatement.setString(1, value.toString());
                preparedStatement.setString(2, key.toString());
                return preparedStatement;
            }
        });

        return key.toString();
    }

    @Override
    public String disable(String key) {
        String statement = "UPDATE " + TABLE + " SET status = 'Disable' WHERE uid = ?";

        jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(statement);
                preparedStatement.setString(1, key);
                return preparedStatement;
            }
        });

        return key;
    }

    @Override
    public String disable(User user) {
        String statement = "UPDATE " + TABLE + " SET status = 'Disable' WHERE uid = ?";

        jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(statement);
                preparedStatement.setString(1, user.getUid());
                return preparedStatement;
            }
        });

        return user.getUid();
    }

    @Override
    public <T> String delete(T key) {
        String statement = "DELETE FROM " + TABLE + " WHERE uid = ?";

        jdbcOperations.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
                PreparedStatement preparedStatement = con.prepareStatement(statement);
                preparedStatement.setString(1, key.toString());
                return preparedStatement;
            }
        });

        return key.toString();

    }

}
