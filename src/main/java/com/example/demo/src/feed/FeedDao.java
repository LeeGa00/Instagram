package com.example.demo.src.feed;

import com.example.demo.src.user.model.GetUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class FeedDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void getUserFeed(int userIdx, int offset){
        String getUserFeed = "";
    }

    public GetUserRes getUsersByEmail(String email){
        String getUsersByEmailQuery = "SELECT userIdx, name, nickName, profileImg, email FROM User WHERE email = ?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.queryForObject(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("profileImg"),
                        rs.getString("email")),
                getUsersByEmailParams);
    }

    public GetUserRes getUsersByIdx(int userIdx){
        String getUsersByIdxQuery = "SELECT userIdx, name, nickName, profileImg, email FROM User WHERE userIdx = ?";
        int getUsersByIdxParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUsersByIdxQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("name"),
                        rs.getString("nickName"),
                        rs.getString("profileImg"),
                        rs.getString("email")),
                getUsersByIdxParams);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from User where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }
}
