package org.example.analize.rewrite.premetive.field;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Debug;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;
import org.jooq.impl.DSL;
@Slf4j
public  abstract class BaseField<FieldName> implements Interpretation<FieldName> {
    @Getter
    String field=null;
    String tableName=null;

    BaseField(String field,String tableName, Variables variables){
        log.debug("BaseField field:"+field+" tableName:"+tableName);
        if(field.isEmpty()){
            field="id";
        }
        this.tableName=tableName;
        this.field=makeField(field,variables);
        log.debug("BaseField this.field:"+this.field+" this.tableName:"+this.tableName);
    }
    protected abstract String makeField(String field, Variables variables);
}
