package com.example.vuespringjava.config;

import com.example.vuespringjava.SecurityConfig.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    LoginFailureHandle loginFailureHandle;
    @Autowired
    LoginSuccessHandle loginSuccessHandle;
    @Autowired
    CapthaFilter capthaFilter;
    @Autowired
    JwtAuthenticationEntryPoint JwtAuthenticationEntryPoint;
    @Autowired
    JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    UserDetailServiceImpl UserService;
    @Autowired
    JwtLogoutSuccessHandler JwtLogoutSuccessHandler;

    @Bean
    JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager());
        return jwtAuthenticationFilter;
    }
//确定加密和比对方法
    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico",
    };

    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()

//                    登录配置规则
                .formLogin()
                .failureHandler(loginFailureHandle)
                .successHandler(loginSuccessHandle)
//                退出配置
                .and()
                .logout()
                .logoutSuccessHandler(JwtLogoutSuccessHandler)

//                  禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//               配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()
//            异常处理器
                .and()
                .exceptionHandling()
//                权限处理入口
                .authenticationEntryPoint(JwtAuthenticationEntryPoint)
//                权限不足异常处理器
                .accessDeniedHandler(jwtAccessDeniedHandler)

//         配置自定义的过滤器
                .and()
                .addFilter(jwtAuthenticationFilter())
                .addFilterBefore(capthaFilter, UsernamePasswordAuthenticationFilter.class);
        ;
    }



    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        将UserService注入到
        auth.userDetailsService(UserService);
    }
}
