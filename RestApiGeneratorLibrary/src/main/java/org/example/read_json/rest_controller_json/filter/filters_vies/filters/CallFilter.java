package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import com.squareup.javapoet.CodeBlock;
import org.example.read_json.rest_controller_json.filter.filters_vies.StringFilter;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_NAME;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.createClass;
import static org.example.read_json.rest_controller_json.filter.filters_vies.Filter.FilterNames.CALL;
import static org.example.read_json.rest_controller_json.filter.filters_vies.filters.CallFilter.Regexp.*;

public class CallFilter extends StringFilter<CodeBlock> {
    record Regexp(){
        static final String SPLIT_PATH="#";
        static final int PATH_PORT=0;

        static final int CLASS_CALL=1;
        static final int METHOD_PORT=2;
        static final int SPLIT_COUNT=3;
    }
    String path="";
    String callPort="";
    String classPort="";
    public CallFilter(String val,String filter) throws IllegalArgumentException {
        super(CALL, val,filter);
        String[] split=val.split(SPLIT_PATH);
        if(split.length!=SPLIT_COUNT){
            throw new IllegalArgumentException("NO PATH MUST BE LIKE path#Class#Method:"+val);
        }
        path=split[PATH_PORT];
        callPort=split[METHOD_PORT];
        classPort=split[CLASS_CALL];
    }

    @Override
    public CodeBlock makeFilter(Object...args) {
        return CodeBlock.builder().add("$T."+callPort+"("+REQUEST_PARAM_NAME+", "+"$S)",createClass(path,classPort),(String)args[1]).build();
    }
}
