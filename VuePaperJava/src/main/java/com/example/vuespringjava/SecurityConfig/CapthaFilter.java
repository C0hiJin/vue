package com.example.vuespringjava.SecurityConfig;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.vuespringjava.common.exception.CaptchaException;
import com.example.vuespringjava.common.lang.Const;
import com.example.vuespringjava.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@Slf4j
@Component
public class CapthaFilter extends OncePerRequestFilter {
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    LoginFailureHandle failureHandle;


    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String url = httpServletRequest.getRequestURI();
        if ("/login".equals(url) && httpServletRequest.getMethod().equals("POST")) {
//            开始校验验证码
            try {
                Validate(httpServletRequest);
            } catch (CaptchaException captchaException) {
//                交给认证失败处理器
                failureHandle.onAuthenticationFailure(httpServletRequest, httpServletResponse, captchaException);
            }
//            如果不正确,转跳到认证失败处理器
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    //    获取验证码
    private void Validate(HttpServletRequest httpServletRequest) {
        log.info("进入验证码判断");

        String code = httpServletRequest.getParameter("code");
        String token = httpServletRequest.getParameter("token");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(token)) {
            log.info("验证码为空");
            throw new CaptchaException("验证码错误");
        }
        if (!code.equals(redisUtil.hget(Const.CAPTCHA_KEY, token))) {
            log.info("验证码错误");
            log.info(code);
            log.info(token);
            throw new CaptchaException("验证码错误");
        }
//        一次性使用
        redisUtil.hdel(Const.CAPTCHA_KEY, token);
        log.info("验证码正确");
    }
}
