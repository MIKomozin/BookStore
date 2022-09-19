package com.example.MyBookShopApp.AOP;

import com.example.MyBookShopApp.errs.BookStoreApiWrongParameterException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.logging.Logger;

@Aspect
@Component
public class AspectOfHandleException {

    @Pointcut(value = "@annotation(com.example.MyBookShopApp.AOP.annotations.HandleException)")
    public void pointCutExceptionByHandleExceptionAnnotation() {
    }

    //вынесем логгирование о возникновении исключения в отделный advice
    @AfterThrowing(pointcut = "pointCutExceptionByHandleExceptionAnnotation()", throwing = "exception")
    public void getLoggerOfExceptionCatcherAdvice(JoinPoint joinPoint, Exception exception) {
        Logger.getLogger(joinPoint.getTarget().getClass().getSimpleName()).warning(exception.getLocalizedMessage());
    }

    //REST API
    @Pointcut(value = "@annotation(com.example.MyBookShopApp.AOP.annotations.HandleExceptionRestApi)")
    public void pointCutExceptionByHandleExceptionRestApiAnnotation() {
    }

    @After("args(exception) && pointCutExceptionByHandleExceptionRestApiAnnotation()")
    public void getLoggerOfExceptionRestApiCatcherAdvice(JoinPoint joinPoint, Exception exception){
        Logger logger = Logger.getLogger(joinPoint.getTarget().getClass().getSimpleName());
        if (exception instanceof BookStoreApiWrongParameterException) {
            logger.warning(exception.getLocalizedMessage());
        } else if (exception instanceof MissingServletRequestParameterException) {
            logger.warning("Missing required parameters. Check entered perameters!");
        }
    }

    //без аннотаций (просто как пример)
    @Pointcut(value = "execution(* *..handleMissingServletRequestParameterException(Exception))")
    public void pointCutExceptionByHandleExceptionRestApi() {
    }

    @After("pointCutExceptionByHandleExceptionRestApi()")
    public void getLoggerOfExceptionAfterMethod(JoinPoint joinPoint){
        Logger.getLogger(joinPoint.getTarget().getClass().getSimpleName()).warning("Missing required parameters. Check entered perameters!");
    }

    //если поставить аннтоацию HandleExceptionRestApi над методом public ResponseEntity<ApiResponse<Book>> booksByTitle
    //в классе BooksRestApiController (проверял. Все работает)
//    @AfterThrowing(pointcut = "pointCutExceptionByHandleExceptionRestApiAnnotation()", throwing = "exception")
//    public void getLoggerOfExceptionRestApiCatcherAdvice(JoinPoint joinPoint, Exception exception) {
//        if (exception instanceof BookStoreApiWrongParameterException) {
//            Logger.getLogger(joinPoint.getTarget().getClass().getSimpleName()).warning(exception.getLocalizedMessage());
//        }
//    }

}
