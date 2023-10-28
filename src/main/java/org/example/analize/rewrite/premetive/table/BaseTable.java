package org.example.analize.rewrite.premetive.table;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.rewrite.Interpretation;
import org.example.analize.rewrite.Variables;

@Slf4j
public  abstract class BaseTable<TableName> implements Interpretation<TableName> {
    String table=null;
    BaseTable(String table, Variables variables){
        log.debug("BaseField table:"+table);
        this.table=makeTable(table,variables);
        log.debug("BaseField this.table:"+this.table);
    }
    protected abstract String makeTable(String table, Variables variables);
}
