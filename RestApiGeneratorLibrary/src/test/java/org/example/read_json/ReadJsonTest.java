package org.example.read_json;

import com.squareup.javapoet.*;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.util.*;

@Slf4j
class ReadJsonTest {
    class t{
        String getY(){
            return "u";
        }
    }
    Condition EndpointFilterName(Map<String, Object> req,String table,Condition def){
        List<Condition> c1;
        if(req.containsKey("key")){
             c1.add(DSL.field(table+"."+"hi")).eq(req.get("key"));
         }
    }
    Condition makeOrFilter(String[] args, String t,Condition def){
        List<Condition> c1=new ArrayList<>();
         for(var a:args){
             //StringFields f=new StringFields(a,t,parent);
            DSL.field("");
            // c1.add((Condition) f.interpret());

         }
         return c1.stream().reduce(Condition::or).ofNullable(def).get();

    }

    @Test
    @SneakyThrows
    void readJsonTest() {
        ReadJson read = new ReadJson();
       // List<Object> list = read.load(JsonPath.FILE_NAME);
        String f="f";
        log.info("stop");
        @FunctionalInterface
        interface Filter{
            Condition[] filter();
        }
        // DSL.select().from("T").where( DSL.field("f").eq("y").and("1")).getSQL();
        List<Condition> c1=new ArrayList<>();
        String j="k";

            if(f.equals("f")){
              //  Objects.requireNonNull(DSL.table("t").field("st")).like(j);
                log.debug("shoe");
            }


        MethodSpec methodSpec = MethodSpec.methodBuilder("myMethod")
                .addStatement("$T.field($S).equal($S)", ClassName.get("org.jooq.impl", "DSL"), "b", "fieldName")
                .addStatement(CodeBlock.builder()
                                .addStatement("$T.field($S).equal($S)", DSL.class, "b", "fieldName")
                        .build())
                .build();

        TypeSpec classSpec = TypeSpec.classBuilder("MyClass")
                .addMethod(methodSpec)
                .build();

        JavaFile javaFile = JavaFile.builder("com.example", classSpec)
                .build();

        String code = javaFile.toString();
        System.out.println(code);

       // log.info( DSL.select().from("T").where( DSL.field("st").eq(j)).getSQL());
    }
}

