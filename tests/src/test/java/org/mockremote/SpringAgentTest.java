package org.mockremote;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;

public class SpringAgentTest {

    @Test
    public void test() throws Exception {
    //    ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:context.xml");
//        System.out.println("classPathXmlApplicationContext.getBeanFactory().getBeanPostProcessorCount() = " + classPathXmlApplicationContext.getBeanFactory().getBeanPostProcessorCount());
    }

    public static void main(String[] args) {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("classpath:context.xml");
        System.out.println("classPathXmlApplicationContext.getBeanFactory().getBeanPostProcessorCount() = " + classPathXmlApplicationContext.getBeanFactory().getBeanPostProcessorCount());
    }
}
