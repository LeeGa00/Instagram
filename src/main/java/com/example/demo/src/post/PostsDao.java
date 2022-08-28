package com.example.demo.src.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

@Repository
public class PostsDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void createPost(int userIdx, String contents){
        String postQuery = "INSERT INTO Posts (userIdx, content) VALUES (?,?)";
        Object[] postParams = new Object[]{userIdx, contents};
        this.jdbcTemplate.update(postQuery, postParams);

        /**
         * String lastInserIdQuery = "select last_insert_id()";
         * return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
         */
    }

    public void modifyPost(int userIdx, int postIdx, String contents){
        String modifyQuery = "UPDATE Post AS p SET p.contents = ? WHERE p.userIdx = ? AND p.postIdx = ?";
        Object[] modifyParams = new Object[]{contents, userIdx, postIdx};
        this.jdbcTemplate.update(modifyQuery, modifyParams);
    }
}
