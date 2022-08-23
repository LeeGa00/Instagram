package com.example.demo.src.post;


import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

}
