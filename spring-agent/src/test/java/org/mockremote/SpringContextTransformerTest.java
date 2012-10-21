package org.mockremote;

import com.google.common.io.ByteStreams;
import org.junit.Before;
import org.junit.Test;
import org.mockremote.spring.SpringContextTransformer;
import org.mockremote.spring.SpringPostProcessorAdapter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class SpringContextTransformerTest {

    private SpringContextTransformer transformer;

    private static final String CLASS_DLBF = "org.springframework.beans.factory.support.DefaultListableBeanFactory";
    private static final String CLASS_PPA = "org.mockremote.spring.SpringPostProcessorAdapter";

    @Before
    public void setUp() throws Exception {
        transformer = new SpringContextTransformer();
    }

    @Test
    public void testTransform() throws Exception {
        CustomClassLoader loader = new CustomClassLoader();
        Class<?> aClass = loader.loadClass(CLASS_DLBF);
        Method getPc = aClass.getMethod("getBeanPostProcessors", null);
        List processors = (List) getPc.invoke(aClass.newInstance());
        assertEquals("1 Processor expected", 1, processors.size());
        assertTrue("SpringPostProcessorAdapter expected", processors.get(0).getClass().getName().equals(CLASS_PPA));
    }

    class CustomClassLoader extends ClassLoader {

        protected CustomClassLoader() {
            super(null);
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                Class<?> loadedClass = findLoadedClass(name);
                if (loadedClass != null) {
                    return loadedClass;
                }
                byte[] bytes = loadBytes(name);
                bytes = transformer.transform(getClass().getClassLoader(),
                        name.replaceAll("\\.", "/"), null, null, bytes);
                return defineClass(name, bytes, 0, bytes.length);
            } catch (Exception e) {
                throw new ClassNotFoundException(e.toString());
            }
        }

        private byte[] loadBytes(String name) {
            try {
                InputStream in = ClassLoader.getSystemClassLoader().
                        getResourceAsStream(name.replaceAll("\\.", "/") + ".class");
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                ByteStreams.copy(in, out);
                return out.toByteArray();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
