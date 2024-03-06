package org.example.analize.premetive.filters;

import lombok.extern.slf4j.Slf4j;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


@Slf4j
class StringFilterFieldTest {
    @BeforeAll
    static void setDebug(){
        DefaultsVariablesName.DEBUG=true;
    }
    static final String realFieldName = "fieldName";
    static final String fieldName = "name";
    static final String tableName = "MyTable";

    @Test
    void StringFilterFieldIfTest() {
        BodyFuncFilterManyParams stringField = make("s:like_" + fieldName);
        log.info("\n"+stringField.interpret().toString());
    }
    BodyFuncFilterManyParams make(String name) throws IllegalArgumentException {
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName);
        return new BodyFuncFilterManyParams(name, endpoint);
    }
}
