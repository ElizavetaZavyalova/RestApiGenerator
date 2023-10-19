package org.example;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import javax.tools.JavaFileObject;
import java.io.File;
import java.net.MalformedURLException;
import static com.google.testing.compile.Compiler.javac;
@Log
class ProcessorTest {
    record Creation(){
        static String FILE_RESOURSE_DIR="P:\\Projects\\JetBrains\\IntelliJIDEA\\restApiGenerator\\"+
                                         "RestApiGenerator\\TestOfWorkingRestApiGenerator\\src\\main\\java\\";
        static JavaFileObject createJavaFileObject(String name) throws MalformedURLException {
            return JavaFileObjects.forResource(new File(FILE_RESOURSE_DIR+name).toURI().toURL());
        }
    }
    @Test
    void testCompilation() throws MalformedURLException {
        MainProcessor processor=new MainProcessor();
        Compilation compilation = javac()
           .withProcessors(processor) // Здесь укажите ваш Processor, если необходимо
          .compile(Creation.createJavaFileObject("org\\example\\Main.java"));


       // log.info(compilation.toString());
    }
}
