package org.example.analize.premetive.fields;

import lombok.extern.slf4j.Slf4j;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
@Slf4j
class StringFieldTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    static final String realFieldName = "fieldName";
    static final String fieldName = "name";
    static final String tableName = "MyTable";
    @Test
    void testInterpret(){
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName);
        StringField stringField=new StringField(fieldName,tableName,endpoint);
        log.info(stringField.interpret().toString());
    }
}
