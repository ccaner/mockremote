package org.mockremote.spring;

import org.mockremote.Remotable;
import org.mockremote.util.ProxyUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class SpringPostProcessorAdapter implements BeanPostProcessor {

    private List<String> beanIds;
    Transport transport;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object result = bean;
        if (beanIds.contains(beanName)) {
            Object remotable = ProxyUtil.createRemotableProxy(bean);
            result = transport.exportRemotable(remotable);
        }
        return result;
    }


    interface Transport {

        <T> T exportRemotable(T remotable);

    }

}
