package org.mockremote.invocation;

import org.mockremote.Remotable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Enhances proxy with a RemotableHelper object. Makes it effectively implement Remotable interface.
 */
public class RemotableInvocationHandler<T> implements InvocationHandler {

    private static final Logger logger = LoggerFactory.getLogger(RemotableInvocationHandler.class);

    private final InvocationSwitch helper;

    public RemotableInvocationHandler(T localObject) {
        helper = new InvocationSwitch<T>(localObject);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(Remotable.class)) {
//            logControl(method, args);
            return method.invoke(helper, args);
        }
        if (helper.remoteModeOn) {
            try {
                Object result = method.invoke(helper.remoteObject, args);
//                logRemote(method, args, result);
                return result;
            } catch (InvocationTargetException e) {
                if (InvokeLocalMethod.class.isInstance(e.getCause())) {
                    Object result = method.invoke(helper.localObject, args);
//                    logRedirect(method, args, result);
                    return result;
                }
                throw e.getCause();
            }
        }
        Object result = method.invoke(helper.localObject, args);
//        logLocal(method, args, result);
        return result;
    }

    private static class InvocationSwitch<T> implements Remotable<T> {

        volatile T remoteObject;
        final T localObject;
        volatile boolean remoteModeOn = false;

        private InvocationSwitch(T localObject) {
            this.localObject = localObject;
        }

        @Override
        public void attachRemote(T remoteObject) {
            InvocationSwitch.this.remoteObject = remoteObject;
        }

        @Override
        public void switchRemoteModeOn() {
            remoteModeOn = true;
        }

        @Override
        public void switchRemoteModeOff() {
            remoteModeOn = false;
        }
    }

/*
    private void logLocal(Method method, Object[] args, Object result) {
        if (!method.getDeclaringClass().equals(Object.class)) {
            logger.debug("LOCAL: method: [{},{}] response: [{}]",
                    new Object[]{method.getName(), Arrays.toString(args),
                            ReflectionToStringBuilder.toString(result)});
        }
    }

    private void logRedirect(Method method, Object[] args, Object result) {
        logger.debug("REMOTE call directed to local: method: [{},{}] response: [{}]",
                new Object[]{method.getName(), Arrays.toString(args),
                        ReflectionToStringBuilder.toString(result)});
    }

    private void logRemote(Method method, Object[] args, Object result) {
        logger.debug("REMOTE: method: [{},{}] response: [{}]",
                new Object[]{method.getName(), Arrays.toString(args),
                        ReflectionToStringBuilder.toString(result)});
    }

    private void logControl(Method method, Object[] args) {
        logger.trace("CONTROL: method: [{},{}] local class: [{}]",
                new Object[]{method.getName(), Arrays.toString(args),
                        helper.localObject.getClass().getSimpleName()});
    }
*/
}
