package com.example.vuespringjava.common.exception;


import com.example.vuespringjava.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@Slf4j
@RestController
public class GlobalExceptionHandler {
//    实体类校验时的异常

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e){
        BindingResult result = e.getBindingResult();
        Optional<ObjectError> first = result.getAllErrors().stream().findFirst();

        log.error("实体校验异常:-----------"+e.getMessage());
        return Result.fail(first.get().getDefaultMessage());
    }

//    针对不同的异常情况
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(IllegalArgumentException e){
        log.error("Assert异常:-----------"+e.getMessage());
        return Result.fail(e.getMessage());
    }

//   捕获异常并将异常信息包装进Result中
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常:-----------"+e.getMessage());
        return Result.fail(e.getMessage());
    }
}
