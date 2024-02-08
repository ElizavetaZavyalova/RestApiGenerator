package org.example.read_json.rest_controller_json;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
@Slf4j
class MakeCastTest {

    @Test
    void makeMapTest(){
        String makeMap="makeMap";
        Object object=Map.of("key","val");
        MakeCast.makeMap(object,makeMap);
        log.info(MakeCast.makeMap(object,makeMap).toString());

        Object objectInt=12;
        var ex = assertThrows(IllegalArgumentException.class, () ->   MakeCast.makeMap(objectInt,makeMap));
        log.info(ex.getMessage());
    }

    @Test
    void makeMapOrMapFromString() {
        String keyWord="entity";
        Object object=Map.of("key","val");
        log.info(MakeCast.makeMapOrMapFromString(
                object,keyWord).entrySet().stream().map(e->e.getKey()+":"+e.getValue()).collect(Collectors.joining(";")));

        Object objectString="string";
        log.info(MakeCast.makeMapOrMapFromString(
                objectString,keyWord).entrySet().stream().map(e->e.getKey()+":"+e.getValue()).collect(Collectors.joining(";")));

        Object objectInt=23;
        var ex = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeMapOrMapFromString(objectInt,keyWord));
        log.info(ex.getMessage());
    }

    @Test
    void makeStringMap() {
        String keyWord="keyWord";
        Object object=Map.of("key","val");
        Map<String,Object> map=Map.of(keyWord,object);
        log.info(MakeCast.makeStringMap(
                map,keyWord,true).entrySet().stream().map(e->e.getKey()+":"+e.getValue()).collect(Collectors.joining(";")));

        String keyWordNo="keyWordNo";
        Map<String,Object> mapNo=Map.of(keyWordNo,object);
        log.info(MakeCast.makeStringMap(
                mapNo,keyWord,false).entrySet().stream().map(e->e.getKey()+":"+e.getValue()).collect(Collectors.joining(";")));

        var ex = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeStringMap(mapNo,keyWord,true));
        log.info(ex.getMessage());

        Map<String,Object> mapInt=Map.of(keyWord,2);
        var exInt = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeStringMap(mapInt,keyWord,false));
        log.info(exInt.getMessage());
    }

    @Test
    void makeMapOfStringMap() {
        String keyWord="keyWord";
        String field="field";
        Map<String, Map<String,String>> object=Map.of("post",Map.of("entity","age"));
        Map<String,Object> map=Map.of(keyWord,object);
        log.info(MakeCast.makeMapOfStringMap(map,keyWord,field,true).toString());

        String keyWordNo="keyWordNo";
        Map<String,Object> mapNo=Map.of(keyWordNo,object);
        log.info(MakeCast.makeMapOfStringMap(mapNo,keyWord,field,false).toString());

        var ex = assertThrows(IllegalArgumentException.class, () -> MakeCast.makeMapOfStringMap(mapNo,keyWord,field,true));
        log.info(ex.getMessage());

        Map<String,Object> mapCast=Map.of(keyWord,13);
        var exCast = assertThrows(IllegalArgumentException.class, () -> MakeCast.makeMapOfStringMap(mapCast,keyWord,field,true));
        log.info(exCast.getMessage());

        Map<String, String> objectString=Map.of("post","String");
        Map<String,Object> mapString=Map.of(keyWord,objectString);
        log.info(MakeCast.makeMapOfStringMap(mapString,keyWord,field,true).toString());
    }

    @Test
    void makeMapAndCheckKey() {
        String keyWord="keyWord";
        Object object=Map.of("key","val");
        Map<String,Object> map=Map.of(keyWord,object);
        log.info(MakeCast.makeMapAndCheckKey(map,keyWord).toString());

        String keyWordNo="keyWordNo";
        Map<String,Object> mapNo=Map.of(keyWordNo,object);

        var ex = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeMapAndCheckKey(mapNo,keyWord).toString());
        log.info(ex.getMessage());
    }

    @Test
    void makeString() {
        String makeString="makeString";
        Object object=new String("String");
        log.info("true:" +MakeCast.makeString(object,makeString));

        Object objectInt=12;
        var ex = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeString(objectInt,makeString));
        log.info(ex.getMessage());
    }

    @Test
    void makeStringIfContainsKeyMap() {
        String keyWord="keyWord";
        Object object="String";
        Map<String,Object> map=Map.of(keyWord,object);
        log.info(MakeCast.makeStringIfContainsKeyMap(map,keyWord,true));

        String keyWordNo="keyWordNo";
        Map<String,Object> mapNo=Map.of(keyWordNo,object);
        log.info(MakeCast.makeStringIfContainsKeyMap(mapNo,keyWord,false));

        var ex = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeStringIfContainsKeyMap(mapNo,keyWord,true));
        log.info(ex.getMessage());

        Map<String,Object> mapInt=Map.of(keyWord,2);
        var exInt = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeStringIfContainsKeyMap(mapInt,keyWord,false));
        log.info(exInt.getMessage());
    }

    @Test
    void makeBoolean() {
        String keyWord="keyWord";
        Object object=true;
        Map<String,Object> map=Map.of(keyWord,object);
        log.info(MakeCast.makeBoolean(map,keyWord,true).toString());

        String keyWordNo="keyWordNo";
        Map<String,Object> mapNo=Map.of(keyWordNo,object);
        log.info(MakeCast.makeBoolean(mapNo,keyWord,false).toString());

        var ex = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeBoolean(mapNo,keyWord,true));
        log.info(ex.getMessage());

        Map<String,Object> mapInt=Map.of(keyWord,2);
        var exInt = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeBoolean(mapInt,keyWord,false));
        log.info(exInt.getMessage());
    }

    @Test
    void makeMapOfMapOfList() {
        String keyWord="keyWord";
        Object object=Map.of("key",Map.of("inKey",List.of("l1","l2")));
        Map<String,Object> map=Map.of(keyWord,object);
        log.info(MakeCast.makeMapOfMapOfList(
                map,keyWord,true).entrySet().stream().map(e->e.getKey()+":"+e.getValue()).collect(Collectors.joining(";")));

        String keyWordNo="keyWordNo";
        Map<String,Object> mapNo=Map.of(keyWordNo,object);
        log.info(MakeCast.makeMapOfMapOfList(
                mapNo,keyWord,false).entrySet().stream().map(e->e.getKey()+":"+e.getValue()).collect(Collectors.joining(";")));

        var ex = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeMapOfMapOfList(mapNo,keyWord,true));
        log.info(ex.getMessage());

        Map<String,Object> mapInt=Map.of(keyWord,2);
        var exInt = assertThrows(IllegalArgumentException.class, () ->  MakeCast.makeMapOfMapOfList(mapInt,keyWord,false));
        log.info(exInt.getMessage());
    }
}