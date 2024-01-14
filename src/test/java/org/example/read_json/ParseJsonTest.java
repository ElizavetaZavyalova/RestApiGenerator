package org.example.read_json;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
public class ParseJsonTest {
    @Test
    void testParseJson(){
        ParseJson parseJson=new ParseJson(JsonPath.FILE_NAME);
        log.info("OK");
    }
}
