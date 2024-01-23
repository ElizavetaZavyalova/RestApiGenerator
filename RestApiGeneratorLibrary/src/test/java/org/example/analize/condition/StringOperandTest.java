package org.example.analize.condition;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.fieldsCond.StringFieldCondition;
import org.example.read_json.rest_controller_json.Endpoint;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.stream.Stream;

@Slf4j
class StringOperandTest {
    static final String realFieldName = "fieldName";
    static final String fieldName1 = "name1";
    static final String fieldName2 = "name2";
    static final String tableName = "MyTable";
    Endpoint makeEndpoint(){
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName1);
        Mockito.doReturn(fieldName2).when(endpoint).getRealFieldName(fieldName2);
        return endpoint;
    }
    StringFieldCondition makeStringFieldCondition(){
        StringFieldCondition stringFieldCondition = Mockito.mock(StringFieldCondition.class);
        Mockito.doReturn("").when(stringFieldCondition).requestInterpret();
        return stringFieldCondition;
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams")
    void StringOperandTest(String f1, String f2,String op){
        StringFieldCondition field1=new StringFieldCondition(f1,tableName,makeEndpoint());
        StringFieldCondition field2=new StringFieldCondition(f2,tableName,makeEndpoint());
        StringOperand operand=new StringOperand(field1,field2,op);
        log.info("\n"+operand.interpret());
        log.info(operand.requestInterpret());
        StringOperand operand2=new StringOperand(field1,makeStringFieldCondition(),op);
        log.info(operand2.requestInterpret());
        StringOperand operand3=new StringOperand(makeStringFieldCondition(),field2,op);
        log.info(operand3.requestInterpret());
        StringOperand operand4=new StringOperand(makeStringFieldCondition(),makeStringFieldCondition(),op);
        log.info(operand4.requestInterpret());
    }
    static public Stream<Arguments> constructorParams() {
        return Stream.of(
                Arguments.of("s:like_" + fieldName1,"s:like_" + fieldName2,"|"),
                Arguments.of("s:like_" + fieldName1,"s:like_" + fieldName2,"&"));
    }
}
