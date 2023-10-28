package org.example.analize.analize.select;


import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.selectInformation.selectId.BaseSelectIdParser;
import org.example.analize.analize.selectInformation.selectId.StringSelectId;
import org.example.analize.analize.when.BaseWhenParser;
import org.example.analize.analize.when.StringWhen;

public class StringSelect extends BaseSelectParser<String, String> {
    StringSelect(String request, BaseVariables variables, BaseSelectParser<String, String> selectPrevious) {
        super(request, variables, selectPrevious);
    }

    @Override
    public String interpret() {
        String condition = when.interpret();
        if (condition != null) {
            return select.interpret() + ".where(" + condition + ")";
        }
        return select.interpret();
    }

    @Override
    public String getTableName() {
        return BaseVariables.make.makeVariableFromString(select.getTable());
    }

    @Override
    BaseSelectIdParser<String> makeSelect(String request, BaseVariables variables) {
        return new StringSelectId(request, variables);
    }

    @Override
    BaseWhenParser<String, String> makeBaseWhenParser(String request, BaseVariables variables,
                                                      BaseSelectParser<String, String> selectPrevious) {
        return new StringWhen(request, variables, selectPrevious, select.getIdIn());
    }
}
