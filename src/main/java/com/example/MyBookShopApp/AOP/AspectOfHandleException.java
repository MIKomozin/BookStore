package com.example.MyBookShopApp.AOP;

import com.example.MyBookShopApp.errs.BookStoreApiWrongParameterException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.logging.Logger;

@Aspect
@Component
public class AspectOfHandleException {

    @Pointcut(value = "execution(* handleUser*(..)) || execution(* getSearch*(..))")
    public void pointCutLoggerExceptionDone(){

    }
    //вынесем логгирование о возникновении исключения в отделный advice
    @AfterThrowing(value = "pointCutLoggerExceptionDone()", throwing = "exception")
    public void getLoggerOfExceptionCatcherAdvice(JoinPoint joinPoint, Exception exception) {
        Logger.getLogger(joinPoint.getTarget().getClass().getSimpleName()).warning(exception.getLocalizedMessage() + " - AspectOfHandleException class");
    }

    //REST API
    @Pointcut(value = "execution(* *ParameterException(..))")
    public void pointCutLoggerExceptionRestApiDone() {
    }

    @After("args(exception) && pointCutLoggerExceptionRestApiDone()")
    public void getLoggerOfExceptionRestApiCatcherAdvice(JoinPoint joinPoint, Exception exception){
        Logger logger = Logger.getLogger(joinPoint.getTarget().getClass().getSimpleName());
        if (exception instanceof BookStoreApiWrongParameterException) {
            logger.warning(exception.getLocalizedMessage() + " - AspectOfHandleException class");
        } else if (exception instanceof MissingServletRequestParameterException) {
            logger.warning("Missing required parameters. Check entered perameters! - AspectOfHandleException class");
        }
    }
}
