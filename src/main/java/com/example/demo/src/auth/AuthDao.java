package com.example.demo.src.auth;

import com.example.demo.src.auth.model.SignUpReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class AuthDao {
    private JdbcTemplate jdbcTemplate;
    @Autowired
    public void setDataSource(DataSource dataSource) {this.jdbcTemplate = new JdbcTemplate(dataSource);}
    public Integer existsEmail(String email) {
        String existsEmailQuery = "SELECT exists(select email FROM User WHERE email = ?)";
        String existsEmailParams = email;
        return this.jdbcTemplate.queryForObject(existsEmailQuery,
                int.class,
                existsEmailParams);
    }

    public Integer updateRefreshToken(SignUpReqDto siginUpReq) {
        String updateRefreshTokenQuery = "UPDATE User AS u\n" +
                "SET u.refreshToken = ?\n" +
                "WHERE u.email =?";
        SignUpReqDto signUpReqDtoParam = siginUpReq;

        return this.jdbcTemplate.update(updateRefreshTokenQuery, signUpReqDtoParam.getRefreshToken()
        ,signUpReqDtoParam.getEmail());
    }

    public void SinUp(SignUpReqDto signUpReq) {
        String createUserQuery = "INSERT INTO User (nickName, email, refreshToken) VALUES (?, ?, ?)";
        Object[] signUpParams = new Object[]{signUpReq.getNickName(),signUpReq.getEmail(), signUpReq.getRefreshToken()};
        this.jdbcTemplate.update(createUserQuery, signUpParams);
    }

    public Integer findUserByEmail(String email) {
        String findUserQuery = "SELECT userIdx FROM User WHERE email = ?";
        String existsEmailParams = email;
        return this.jdbcTemplate.queryForObject(findUserQuery,
                int.class,
                existsEmailParams);
    }
}
