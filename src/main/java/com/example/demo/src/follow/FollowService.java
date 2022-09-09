package com.example.demo.src.follow;


import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

// Service Create, Update, Delete 의 로직 처리
@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final JwtService jwtService;

    public FollowService(FollowDao followDao, JwtService jwtService) {
        this.followDao = followDao;
        this.jwtService = jwtService;
    }

    public String FollowUser(int userIdx, int follower){
        //비공계 유저인지 확인 필요
        return "result";
    }
}
