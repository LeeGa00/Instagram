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


    /*
        나중에 작성 필요
     */
    public Integer existsEmail(String email) {
        String existsEmailQuery = "select exists(select email from User where email = ?)";
        String existsEmailParams = email;
        return this.jdbcTemplate.queryForObject(existsEmailQuery,
                int.class,
                existsEmailParams);
    }

    public Integer updateRefreshkey(SignUpReqDto siginUpReq) {
        String updateRefreshTokenQuery = "UPDATE User as u\n" +
                "SET u.refreshKey = ?\n" +
                "WHERE u.email =?";
        SignUpReqDto signUpReqDtoParam = siginUpReq;

        return this.jdbcTemplate.update(updateRefreshTokenQuery, signUpReqDtoParam.getRefreshkey()
        ,signUpReqDtoParam.getEmail());
    }

    public void Sinup(SignUpReqDto siginUpReq) {
        String createUserQuery = "insert into User (refreshKey, name, email) VALUES (?, ?, ?)";
        Object[] signUpParams = new Object[]{siginUpReq.getRefreshkey(),siginUpReq.getName(),siginUpReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, signUpParams);
    }

    public Integer findUserByEmail(String email) {
        String findUserQuery = "select userIdx from User where email = ?";
        String existsEmailParams = email;
        return this.jdbcTemplate.queryForObject(findUserQuery,
                int.class,
                existsEmailParams);
    }
}
