package org.example.read_json;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.impl.DSL;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
class ReadJsonTest {
    class t{
        String getY(){
            return "u";
        }
    }
    Condition makeOrFilter(String[] args, t T){
        List<Condition> c1=new ArrayList<>();
         for(var a:args){
             if(T.getY()!=null){
                // c1.add(DSL.field(DSL.table("n").field("d")).eq(T.getY()));
             }
         }
         return c1.stream().reduce(Condition::or).ofNullable(DSL.falseCondition()).get();

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

            if(f.equals("f")){
                boolean add = c1.add((DSL.table("y").field("f")).eq("d")));
                log.debug("shoe");
            }



        //log.info( DSL.select().from("T").where( DSL.field("f").eq("y").and(makeOrFilter())).getSQL());
    }
}

