package org.example.analize.condition;

import com.squareup.javapoet.ParameterSpec;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.premetive.fields_cond.FieldCondition;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
class StringOperandTest {
    static final String realFieldName = "fieldName";
    static final String fieldName1 = "name1";
    static final String fieldName2 = "name2";
    static final String tableName = "MyTable";
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    Endpoint makeEndpoint(){
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName1);
        Mockito.doReturn(fieldName2).when(endpoint).getRealFieldName(fieldName2);
        return endpoint;
    }
    FieldCondition makeStringFieldCondition(){
        FieldCondition stringFieldCondition = Mockito.mock(FieldCondition.class);
        return stringFieldCondition;
    }
    @ParameterizedTest(name = "{arguments} test")
    @MethodSource("constructorParams")
    void StringOperandTest(String f1, String f2,String op){
        FieldCondition field1=new FieldCondition(f1,tableName,makeEndpoint());
        FieldCondition field2=new FieldCondition(f2,tableName,makeEndpoint());
        Operand operand=new Operand(field1,field2,op);
        log.info("\n"+operand.interpret().toString());

        List<VarInfo> list=new ArrayList<>();
        List<FilterInfo> filters = new ArrayList<>();
        operand.addParams(list,filters);

        log.info(list.stream().map(v->v.toString()).collect(Collectors.joining("\n")));

        Operand operand2=new Operand(field1,makeStringFieldCondition(),op);
        List<ParameterSpec> list2=new ArrayList<>();
        operand2.addParams(list,filters);
        log.info(list2.stream().map(v->v.toString()).collect(Collectors.joining("\n")));

        Operand operand3=new Operand(makeStringFieldCondition(),field2,op);
        List<ParameterSpec> list3=new ArrayList<>();
        operand3.addParams(list,filters);
        log.info(list3.stream().map(v->v.toString()).collect(Collectors.joining("\n")));

        Operand operand4=new Operand(makeStringFieldCondition(),makeStringFieldCondition(),op);
        List<ParameterSpec> list4=new ArrayList<>();
        operand4.addParams(list,filters);
        log.info(list4.stream().map(v->v.toString()).collect(Collectors.joining("\n")));
    }
    static public Stream<Arguments> constructorParams() {
        return Stream.of(
                Arguments.of("like_" + fieldName1+":s","like_" + fieldName2+":s","|"),
                Arguments.of("like_" + fieldName1+":s","like_" + fieldName2+":s","&"));
    }
}
