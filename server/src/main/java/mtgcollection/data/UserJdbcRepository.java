package mtgcollection.data;

import mtgcollection.model.User;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UserJdbcRepository implements UserRepository{

    private static JdbcClient jdbcClient;

    public UserJdbcRepository(JdbcClient jdbcClient){
        this.jdbcClient = jdbcClient;
    }

    public User findUserByEmail(String email){
        final String sql = """
                select user_id as userId, email,password from `user`
                where email = :email;
                """;
        return jdbcClient.sql(sql)
                .param("email",email)
                .query(User.class)
                .optional().orElse(null);
    }

    public User add(User user){
        final String sql = """
                insert into `user` (email, password) values
                (:email,:password);
                """;
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcClient.sql(sql)
                .param("email", user.getEmail())
                .param("password", user.getPassword())
                .update(keyHolder,"user_id");

        user.setUserId(keyHolder.getKey().intValue());
        return user;
    }

}
