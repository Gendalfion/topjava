package ru.javawebinar.topjava.repository.jdbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final Logger log = LoggerFactory.getLogger(JdbcUserRepositoryImpl.class);

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final TransactionTemplate transactionTemplate;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate
            , TransactionTemplate transactionTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;

        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public User save(User user) {
        return transactionTemplate.execute(status -> {
            log.debug("{}.save({})", getClass().getSimpleName(), user);
            BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

            if (user.isNew()) {
                Number newKey = insertUser.executeAndReturnKey(parameterSource);
                user.setId(newKey.intValue());

                Set<Role> roleSet = user.getRoles();
                if (roleSet != null && roleSet.size() >= 1) {
                    final List<Role> roles = new ArrayList<>(user.getRoles());
                    jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                        @Override
                        public void setValues(PreparedStatement ps, int i) throws SQLException {
                            ps.setInt(1, user.getId());
                            ps.setString(2, roles.get(i).toString());
                        }

                        @Override
                        public int getBatchSize() {
                            return roles.size();
                        }
                    });
                }
            } else {
                namedParameterJdbcTemplate.update(
                        "UPDATE users SET name=:name, email=:email, password=:password, " +
                                "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource);
            }
            return user;
        });
    }

    @Override
    public boolean delete(int id) {
        return transactionTemplate.execute(status -> {
            log.debug("{}.delete({})", getClass().getSimpleName(), id);
            return jdbcTemplate.update("DELETE FROM users WHERE id=?", id);
        }) != 0;
    }

    @Override
    public User get(int id) {
        return transactionTemplate.execute(status -> {
            log.debug("{}.get({})", getClass().getSimpleName(), id);
//            List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
//            return DataAccessUtils.singleResult(users);
            return DataAccessUtils.singleResult(queryForUsers(id, null));
        });
    }

    private Collection<User> queryForUsers(Integer id, String email) {
        String query = "SELECT u.id, u.name, u.password, u.registered, u.password, u.enabled, u.email, u.calories_per_day, r.role " +
                "FROM users u LEFT JOIN user_roles r " +
                "ON r.user_id = u.id" +
                (id == null ? "" : " WHERE u.id=?") +
                (email == null ? "" : " WHERE u.email=?") +
                (id == null && email == null ? " ORDER BY u.name, u.email" : "");
        Object queryParam = (id == null) ? email : id;
        SqlRowSet sqlRowSet = queryParam != null ? jdbcTemplate.queryForRowSet(query, queryParam) : jdbcTemplate.queryForRowSet(query);
        Map<Integer, User> userMap = new LinkedHashMap<>();
        while (sqlRowSet.next()) {
            userMap.compute(sqlRowSet.getInt("id"),
                    (key, user) -> {
                        String roleStr = sqlRowSet.getString("role");
                        Role role = null;
                        if (roleStr != null) {
                            role = Role.valueOf(roleStr);
                        }
                        if (user == null) {
                            Set<Role> roles = new HashSet<>();
                            if (role != null) {
                                roles.add(role);
                            }
                            user = new User(key,
                                    sqlRowSet.getString("name"),
                                    sqlRowSet.getString("email"),
                                    sqlRowSet.getString("password"),
                                    sqlRowSet.getInt("calories_per_day"),
                                    sqlRowSet.getBoolean("enabled"),
                                    roles);
                        } else {
                            if (role != null) {
                                user.getRoles().add(role);
                            }
                        }
                        return user;
                    });
        }
        return userMap.values();
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return transactionTemplate.execute(status -> {
            log.debug("{}.getByEmail({})", getClass().getSimpleName(), email);
//            List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
//            return DataAccessUtils.singleResult(users);
            return DataAccessUtils.singleResult(queryForUsers(null, email));
        });
    }

    @Override
    public List<User> getAll() {
        return transactionTemplate.execute(status -> {
            log.debug("{}.getAll()", getClass().getSimpleName());
//            return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
            return new ArrayList<>(queryForUsers(null, null));
        });
    }
}
