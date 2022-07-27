package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.auth.model.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;


    public AuthController(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping(value = "/kakao/signup")
    public BaseResponse<SignupRes> kakaoSignup(@RequestBody SignUpReq signUpReq) throws BaseException {
        try{
            SignupRes signupRes = authService.kakoSiginUp(signUpReq);
            return new BaseResponse<>(signupRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @GetMapping("/kakao/callback")
    public String kakaoLogin(String code) {
        // code는 카카오 서버로부터 받은 인가 코드
        log.info("kakaoLogin");
        authService.kakaoLogin(code);
        return "redirect:/";
    }

}