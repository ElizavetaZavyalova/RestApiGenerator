package org.example.analize.analize.select;

import lombok.extern.slf4j.Slf4j;
import org.example.analize.analize.BaseVariables;
import org.example.analize.analize.Interpretation;
import org.example.analize.analize.selectInformation.BaseSelectFieldsParser;
import org.example.analize.analize.selectInformation.selectId.BaseSelectIdParser;
import org.example.analize.analize.when.BaseWhenParser;
@Slf4j
public abstract class BaseSelectParser<Condition,Select> implements Interpretation<Select> {
    BaseWhenParser<Condition,Select> when;
    BaseSelectIdParser<Select> select;
    record PORT() {
        static final int SELECT = 0;
        static final int WHEN = 1;
        static final int PORT_CONT=2;
    }
    public abstract String getTableName();
    BaseSelectParser(String request, BaseVariables variables,
                     BaseSelectParser<Condition,Select> selectPrevious){
         log.debug(" BaseSelectParser request is");
        String[] input=request.split("\\?");
         if(input.length==PORT.PORT_CONT){
             this.select=makeSelect(input[PORT.SELECT],variables);
             log.debug(" BaseSelectParser select is"+input[PORT.SELECT]);
             this.when=makeBaseWhenParser(input[PORT.WHEN],variables,selectPrevious);
             log.debug(" BaseSelectParser when is"+input[PORT.WHEN]);
             return;
         }
        this.select=makeSelect(request,variables);
        this.when=makeBaseWhenParser("",variables,selectPrevious);
    }
    abstract  BaseSelectIdParser<Select> makeSelect(String request, BaseVariables variables);
   abstract BaseWhenParser<Condition,Select>makeBaseWhenParser
           (String request, BaseVariables variables,
            BaseSelectParser<Condition,Select> selectPrevious);

}
