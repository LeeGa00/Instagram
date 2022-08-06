package com.example.demo.src.auth;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.auth.model.SignUpReq;
import com.example.demo.src.auth.model.SignupRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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
            SignupRes signupRes = authService.kakaoSiginUp(signUpReq);
            return new BaseResponse<>(signupRes);
        } catch(BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    @ResponseBody
    @GetMapping({"/kakao"})
    public BaseResponse<SignupRes> kakaoCallback(@RequestParam String code) throws BaseException {
        try {
            SignUpReq signUpReq = authService.getKakaoAccessToken(code);
            SignupRes signupRes = authService.kakaoSiginUp(signUpReq);
            return new BaseResponse(signupRes);
        } catch (BaseException var4) {
            return new BaseResponse(var4.getStatus());
        }
    }
}