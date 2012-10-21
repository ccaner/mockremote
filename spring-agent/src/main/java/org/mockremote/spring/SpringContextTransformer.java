package org.mockremote.spring;

import org.objectweb.asm.*;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import static org.objectweb.asm.ClassWriter.COMPUTE_FRAMES;
import static org.objectweb.asm.ClassWriter.COMPUTE_MAXS;
import static org.objectweb.asm.Opcodes.*;

/**
 * Class transformer that modifies spring context to include given bean post processors by default.
 * Modifies constructors so it invokes "addBeanPostProcessor" with given class.
 */
public class SpringContextTransformer implements ClassFileTransformer {

    private static final String CLASS_ABF = "org/springframework/beans/factory/support/AbstractBeanFactory";
    private static final String CLASS_POST_PROC = "org/mockremote/spring/SpringPostProcessorAdapter";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (!className.equals(CLASS_ABF)) {
            return classfileBuffer;
        }

        ClassWriter writer = new ClassWriter(COMPUTE_FRAMES + COMPUTE_MAXS);
        ClassVisitor cv = new ConstructorModifyingClassVisitor(writer, new MethodVisitorFactory() {
            @Override
            public MethodVisitor createMethodVisitor(MethodVisitor methodVisitor) {
                return new PostProcessorAddingMethodVisitor(CLASS_POST_PROC, methodVisitor);
            }
        });

        ClassReader reader = new ClassReader(classfileBuffer);
        reader.accept(cv, 0);
        return writer.toByteArray();
    }

    static class ConstructorModifyingClassVisitor extends ClassAdapter {

        MethodVisitorFactory methodVisitorFactory;

        public ConstructorModifyingClassVisitor(ClassVisitor classVisitor, MethodVisitorFactory methodVisitorFactory) {
            super(classVisitor);
            this.methodVisitorFactory = methodVisitorFactory;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = cv.visitMethod(access, name, desc, signature, exceptions);
            if (name.equals("<init>")) {
                return methodVisitorFactory.createMethodVisitor(methodVisitor);
            }
            return methodVisitor;
        }
    }

    static class PostProcessorAddingMethodVisitor extends MethodAdapter {

        String internalClassName;

        public PostProcessorAddingMethodVisitor(String className, MethodVisitor methodVisitor) {
            super(methodVisitor);
            this.internalClassName = className;
        }

        @Override
        public void visitInsn(int opCode) {
            if (opCode == RETURN) {
                mv.visitVarInsn(ALOAD, 0);
                mv.visitTypeInsn(NEW, internalClassName);
                mv.visitInsn(DUP);
                mv.visitMethodInsn(INVOKESPECIAL, internalClassName, "<init>", "()V");
                mv.visitMethodInsn(INVOKEVIRTUAL, "org/springframework/beans/factory/support/AbstractBeanFactory",
                        "addBeanPostProcessor", "(Lorg/springframework/beans/factory/config/BeanPostProcessor;)V");
            }
            mv.visitInsn(opCode);
        }
    }

    static interface MethodVisitorFactory {

        MethodVisitor createMethodVisitor(MethodVisitor methodVisitor);

    }

}
