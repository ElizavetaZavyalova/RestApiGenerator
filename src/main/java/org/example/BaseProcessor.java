package org.example;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.tools.Diagnostic;
import java.lang.reflect.Method;
import java.util.Optional;

@Slf4j
public abstract class BaseProcessor extends AbstractProcessor {
    ASTTreeComponents ast=null;
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        processingEnv = jbUnwrap(ProcessingEnvironment.class, processingEnv);
        super.init(processingEnv);
        ast=new ASTTreeComponents(processingEnv);
        log.debug("------ASTTreeComponents was init-----");
    }


    private static <T> T jbUnwrap(Class<? extends T> iface, T wrapper) {
        T unwrapped = null;
        try {
            final Class<?> apiWrappers = wrapper.getClass().getClassLoader().loadClass("org.jetbrains.jps.javac.APIWrappers");
            final Method unwrapMethod = apiWrappers.getDeclaredMethod("unwrap", Class.class, Object.class);
            unwrapped = iface.cast(unwrapMethod.invoke(null, iface, wrapper));
            log.debug("UNWRUPPER");
        }
        catch (Throwable ignored) {
            log.debug("WRUPPER");
        }
        return Optional.ofNullable(unwrapped).orElse(wrapper);
    }

}