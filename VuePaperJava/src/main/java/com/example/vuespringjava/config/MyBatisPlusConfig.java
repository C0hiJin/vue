package com.example.vuespringjava.config;

import com.baomidou.mybatisplus.autoconfigure.ConfigurationCustomizer;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.vuespringjava.mapper")
public class MyBatisPlusConfig {
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor(){
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
//               分页插件
                mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
//                防止全表更新插件
                mybatisPlusInterceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
                return mybatisPlusInterceptor;
    }
    @Bean
    public ConfigurationCustomizer  ConfigurationCustomizer(){
        return  configuration -> configuration.setUseDeprecatedExecutor(false);
    }
}
