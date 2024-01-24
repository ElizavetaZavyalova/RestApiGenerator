package org.example.read_json;

import com.squareup.javapoet.CodeBlock;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
@Slf4j
class ParseJsonTest {
    @Test
    void testParseJson(){
       // ParseJson parseJson=new ParseJson("dd");
        log.info("OK");
        CodeBlock block= CodeBlock.builder().add("frrtt$S","ghjfgjj").build();
        log.info(CodeBlock.builder().add(block).add("hhhhhhhhhhhhhhhhhhhhhh").build().toString());
    }
}
