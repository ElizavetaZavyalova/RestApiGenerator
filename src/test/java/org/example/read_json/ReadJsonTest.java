package org.example.read_json;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.List;
@Slf4j
class ReadJsonTest {

    @Test
    @SneakyThrows
    void readJsonTest(){
        ReadJson read=new ReadJson();
        List<Object> list=read.load(JsonPath.FILE_NAME);
        log.info("stop");
    }
}
