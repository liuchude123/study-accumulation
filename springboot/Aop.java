package com.rich.config;

import com.rich.entity.domain.DBEnum;
import com.rich.service.Test1Service;
import com.rich.service.Test2Service;
import com.rich.service.impl.Test1ServiceImpl;
import com.rich.service.impl.Test2ServiceImpl;
import com.rich.util.DBContextHolder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@org.aspectj.lang.annotation.Aspect
@Component
public class Aspect {

    /**
     * 使用空方法定义切点表达式
     */
    @Pointcut("execution(* com.rich.service.**.*(..))")
    public void service() {
    }

    /**
     * 使用定义切点表达式的方法进行切点表达式的引入
     */
    @Before("service()")
    public void setDataSourceKey(JoinPoint point) {
        //根据连接点所属的类实例，动态切换数据源
        if (point.getTarget() instanceof Test1Service
                || point.getTarget() instanceof Test1ServiceImpl) {
            DBContextHolder.setDB(DBEnum.db1);
        } else if (point.getTarget() instanceof Test2Service
                || point.getTarget() instanceof Test2ServiceImpl) {
            DBContextHolder.setDB(DBEnum.db2);
        } else {//连接点所属的类实例是（当然，这一步也可以不写，因为defaultTargertDataSource就是该类所用的db）
            DBContextHolder.setDB(DBEnum.db1);
        }
    }

    /**
     * 使用空方法定义切点表达式
     */
    @Pointcut("execution(* com.rich.controller.**.*(..))")
    public void controller() {
    }

    // 环绕通知
    @Before("controller()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
        // 接收到请求，记录请求内容
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // 记录下请求内容
        System.out.println("[Controller][Before]URL : " + request.getRequestURL().toString());
        System.out.println("[Controller][Before]HTTP_METHOD : " + request.getMethod());
        System.out.println("[Controller][Before]IP : " + request.getRemoteAddr());
        System.out.println("[Controller][Before]CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        System.out.println("[Controller][Before]ARGS : " + Arrays.toString(joinPoint.getArgs()));
    }

    // 执行成功后通知
    @AfterReturning(returning = "ret", pointcut = "controller()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        System.out.println("[Controller][AfterReturning]RESPONSE : " + ret);
    }

    // 环绕通知
    @Around("controller()")
    public Object doAround(ProceedingJoinPoint joinPoint) {
        Object result = null;
        try {
            System.out.println("[Controller][Around]环绕通知开始 日志记录");
            long start = System.currentTimeMillis();

            //有返回参数 则需返回值
            result = joinPoint.proceed();
            System.out.println("[Controller][Around]RESPONSE : " + result);

            long end = System.currentTimeMillis();
            System.out.println("[Controller][Around]总共执行时长" + (end - start) + " 毫秒");
            System.out.println("[Controller][Around]环绕通知结束 日志记录");
        } catch (Throwable t) {
            System.out.println("[Controller][Around]出现错误");
        }
        return result;
    }

    // 配置切点 及要传的参数
    @Pointcut("execution(* com.rich.controller.WelcomeController.hello(..)) && args(name)")
    public void hello(String name) {
    }

    // 配置连接点 方法开始执行时通知
    @Before("hello(name)")
    public void before(String name) {
        System.out.println("[WelcomeController.hello.Before]开始执行前置通知  日志记录:" + name);
    }

    // 方法执行完后通知
    @After("hello(name)")
    public void after(String name) {
        System.out.println("[WelcomeController.hello.After]开始执行后置通知 日志记录:" + name);
    }

    // 执行成功后通知
    @AfterReturning(returning = "ret", pointcut = "hello(name)")
    public void afterReturning(String name, Object ret) {
        System.out.println("[WelcomeController.hello.AfterReturning]方法成功执行后通知 日志记录:" + name + "[RESPONSE]" + ret);
    }

    // 抛出异常后通知
    @AfterThrowing("hello(name)")
    public void afterThrowing(String name) {
        System.out.println("[WelcomeController.hello.AfterThrowing]方法抛出异常后执行通知 日志记录" + name);
    }

}





package com.huawei.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

/**
 * Created by liuchude lWX433784 on 2017/3/16.
 */
@Aspect
@Component
public class AOP {
    private static final Logger LOG = LoggerFactory.getLogger(AOP.class);

    /**
     * 使用空方法定义切点表达式
     */
    @Pointcut("execution(* com.huawei.controller.**.*(..))")
    public void controller() {
    }

    /**
     * 环绕通知
     *
     * @param joinPoint 切入点
     * @return
     */
    @Around("controller()")
    public Object aroundController(ProceedingJoinPoint joinPoint) {
        Object result = null;
        String url;
        String user;
        String address;
        String httpMethod;
        String classMethod;
        String args;
        long startTime = 0L;
        long endTime;
        try {
            startTime = System.currentTimeMillis();
            // 接收到请求，记录请求内容
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            HttpServletRequest request = attributes.getRequest();
            // 请求内容
            url = request.getRequestURL().toString();
            user = request.getRemoteUser();
            address = request.getRemoteAddr();
            httpMethod = request.getMethod();
            classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();
            args = Arrays.toString(joinPoint.getArgs());
            LOG.info("start! class method: " + classMethod + ", method: " + httpMethod + ", user: " + user + ", address: " + address + ", url: " + url);
            LOG.info("request: " + args);
            // 有返回参数 则需返回值
            result = joinPoint.proceed();
            LOG.info("response: " + result);
        } catch (Throwable t) {
            LOG.error("error", t);
        } finally {
            endTime = System.currentTimeMillis();
            LOG.info("end! spend time: " + (endTime - startTime) + " ms!");
        }
        return result;
    }
}
