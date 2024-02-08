package org.example.analize.premetive.filters;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


@Slf4j
class StringFilterFieldTest {
    static final String realFieldName = "fieldName";
    static final String fieldName = "name";
    static final String tableName = "MyTable";

    @Test
    void StringFilterFieldIfTest() {
        StringFilterField stringField = make("s:like_" + fieldName);
        log.info("\n"+stringField.interpret().toString());
    }
    StringFilterField make(String name) throws IllegalArgumentException {
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(realFieldName).when(endpoint).getRealFieldName(fieldName);
        return new StringFilterField(name, endpoint);
    }
}
