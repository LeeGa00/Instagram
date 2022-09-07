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

    public void deletePost(int userIdx, int postIdx){
        String deleteQuery = "UPDATE Post AS p SET p.status = 'INACTIVE' WHERE p.userIdx = ? AND p.postIdx = ?";
        Object[] deleteParams = new Object[]{userIdx, postIdx};
    }

    public void getPostLike(int userIdx, int postIdx){

    }

    public void createPostComment(int userIdx, int postIdx, String Comment){

    }

    public int getUserIdxByPostIdx(int postIdx){
        String getUploaderQuery = "SELECT userIdx From Posts WHERE postIdx = ?";
        return this.jdbcTemplate.queryForObject(getUploaderQuery, int.class, postIdx);
    }

    public int checkUserIdxFollowed(int userIdx, int uploader){
        return 1;
    }
}
