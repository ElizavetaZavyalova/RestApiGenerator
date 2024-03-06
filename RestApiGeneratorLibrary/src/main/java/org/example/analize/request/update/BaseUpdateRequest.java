package org.example.analize.request.update;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.update.update.BaseUpdate;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.FIELDS_NAME;

public abstract class BaseUpdateRequest<R,M,N> extends BaseRequest<R,M> {
    protected BaseUpdate<R,N> update;
    protected BaseUpdateRequest(String url, List<String> fields, Endpoint parent) throws IllegalArgumentException {
        super(url, parent);
        update=makeUpdate(address.getEndUrl(),fields,address.getSelectCurrent(),parent);
    }
    @Override
    public String getExampleEntity(){
        return CodeBlock.builder().add("{").add(update.getExampleFields()).add("}").build().toString();
    }



    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        update.addParams(params,filters);
    }
    protected abstract BaseUpdate<R,N> makeUpdate(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent);
}
