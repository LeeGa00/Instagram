package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.ModifyUserPostReq;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.POST_NOT_EXIST;

// Service Create, Update, Delete 의 로직 처리
@Service
public class PostsService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostsDao postsDao;
    private final JwtService jwtService;

    public PostsService(PostsDao postsDao, JwtService jwtService) {
        this.postsDao = postsDao;
        this.jwtService = jwtService;
    }

    public void uploadPost(int userIdx, String contents){
        postsDao.createPost(userIdx, contents);
    }

    public void modifyPost(int userIdx, ModifyUserPostReq modifyUserPostReq){
        postsDao.modifyPost(userIdx, modifyUserPostReq.getPostIdx(), modifyUserPostReq.getContent());
    }

    public void deletePost(int userIdx, int postIdx){
        postsDao.deletePost(userIdx, postIdx);
    }

    public void commentPost(int userIdx, int postIdx, String comment) throws BaseException{
        int postOwner = existPostGetUserIdx(postIdx);
        if (postOwner == 0){
            throw new BaseException(POST_NOT_EXIST);
        }
    }

    public int existPostGetUserIdx(int postIdx) throws BaseException{
        int userIdx = 0;
        try{ userIdx = postsDao.getUserIdxByPostIdx(postIdx); }
        catch (Exception exception){ throw new BaseException(DATABASE_ERROR); }
        return userIdx;
    }
}
