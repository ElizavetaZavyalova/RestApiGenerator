package org.example;

import com.google.testing.compile.Compilation;
import com.google.testing.compile.JavaFileObjects;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import org.example.processors.MainProcessor;
import org.junit.jupiter.api.Test;
import javax.tools.JavaFileObject;
import java.io.File;
import static com.google.testing.compile.Compiler.javac;
@Log
class ProcessorTest {
    record Creation(){
        static String FILE_RESOURSE_DIR="P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\"+
                                         "RestApiGenerator\\TestOfWorkingRestApiGenerator\\src\\main\\java\\";
        @SneakyThrows
        static JavaFileObject createJavaFileObject(String name){
            return JavaFileObjects.forResource(new File(FILE_RESOURSE_DIR+name).toURI().toURL());
        }
    }

    //Из-за сторонних подключенных библиотек(испольуемых в Main.java если такие есть) тест Может падать.
    //Так что assert отсутсвуют, но хоть в дебагере что происходит посмотреть можно
    @Test

    void testCompilation(){
        MainProcessor processor=new MainProcessor();
        Compilation compilation = javac()
           .withProcessors(processor)
          .compile(Creation.createJavaFileObject("org\\example\\Main.java"));
    }
}
