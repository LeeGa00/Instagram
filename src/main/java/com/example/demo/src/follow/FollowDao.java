package com.example.demo.src.follow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class FollowDao {
    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public String accountIsPublic(int userIdx){
        String checkQuery = "SELECT ";  //먼저 비공계 유저 설정 상태값 DB에 설정하기
        //return this.jdbcTemplate.queryForObject();
        return checkQuery; //삭제코드
    }

    public void newFollow(int userIdx, int followerIdx) {
        String newFollowQuery = "SET Follow Values (?, ?)"; // DB 롹인해보기 값 3개..
        Object followParams = new Object[]{userIdx, followerIdx};
        this.jdbcTemplate.update(newFollowQuery, followParams);
    }
}
