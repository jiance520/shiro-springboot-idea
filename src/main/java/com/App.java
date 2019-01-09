package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//springboot自带tomcat插件，所以启动springboot，只需要在启动类上加注解@SpringBootApplication。
@SpringBootApplication
@MapperScan("com.dao")//重点，实列化Bean，只有扫描了dao，才能实例化mapper调用mapper.xml里面的方法！也可以在application.xml配置扫描。
public class App
{
    public static void main( String[] args )
    {
        SpringApplication.run(App.class, args);
    }
}
