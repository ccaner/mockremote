package org.mockremote.util;

import org.apache.commons.lang3.ClassUtils;
import org.mockremote.Remotable;
import org.mockremote.invocation.RemotableInvocationHandler;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public abstract class ProxyUtil {

    public static Object createRemotableProxy(Object localObject) {
        List<Class<?>> interfaces = new ArrayList<>();
        interfaces.add(Remotable.class);
        interfaces.addAll(ClassUtils.getAllInterfaces(localObject.getClass()));

        InvocationHandler handler = new RemotableInvocationHandler<>(localObject);

        return Proxy.newProxyInstance(ProxyUtil.class.getClassLoader(),
                interfaces.toArray(new Class<?>[interfaces.size()]),
                handler);
    }
}
