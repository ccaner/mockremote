package org.mockremote.spring;

import java.lang.instrument.Instrumentation;

/**
 * Java agent that makes given beans in target application context remotely mockable.
 */
public class MockRemoteJavaAgent {

    public static void premain(String agentArgs, Instrumentation inst) {
        inst.addTransformer(new SpringContextTransformer());
    }

}
