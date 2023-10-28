package org.example.analize.analize.selectInformation.selectId;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.selectInformation.BaseSelectFieldsParser;
import org.jooq.impl.DSL;

@Slf4j
public class StringSelectId extends BaseSelectIdParser<String> {
    public StringSelectId(String request, BaseVariables variables) {
        super(request, variables);
    }

    @Override
    public String interpret() {
        log.debug("interpret");
        StringBuilder stringBuilder = new StringBuilder().append("dslContext.select(");
        if (maxMin == MAX_MIN.getMAX()) {
            stringBuilder.append("DSL.max(DSL.field(").append(id).append(")))");
        } else if (maxMin == MAX_MIN.getMIN()) {
            stringBuilder.append("DSL.min(DSL.field(").append(id).append(")))");
        } else stringBuilder.append("DSL.field(").append(id).append("))");
        stringBuilder.append(".from(").append(table).append(")");
        return debug.debugString("StringSelectId interpret", stringBuilder.toString());

    }

    @Override
    protected String addField(String field, BaseVariables variables) {
        if (BaseVariables.make.isVariable(field)) {
            BaseVariables.make.TYPE type = BaseVariables.make.TYPE.STRING;
            variables.addVariable(BaseVariables.make.makeVariable(field), type);
        }
        return BaseVariables.make.makeWriteVariable(field);
    }

    @Override
    protected String addTable(String table, BaseVariables variables) {
        if (BaseVariables.make.isVariable(table)) {
            BaseVariables.make.TYPE type = BaseVariables.make.TYPE.STRING;
            variables.addVariable(BaseVariables.make.makeVariable(table), type);
        }
        return BaseVariables.make.makeWriteVariable(table);
    }
}
