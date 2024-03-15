package org.example.analize.request.get;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.get.select.BaseGet;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.FIELDS_NAME;

public abstract class BaseGetRequest<R,M,P> extends BaseRequest<R,M,P> {
    BaseGet<R,M> select;
   final boolean isPorts;
   final boolean isSort;
   final boolean isFields;
    protected BaseGetRequest(String url, List<String> fields, Endpoint parent, boolean isPorts, boolean isSort,boolean isFields) throws IllegalArgumentException {
        super(url, parent);
        this.isPorts=isPorts;
        this.isSort=isSort;
        this.isFields=isFields;
        select=makeSelect(address.getEndUrl(),fields,address.getSelectCurrent(),parent);
    }
    @Override
    public String getExampleParams(){
        return CodeBlock.builder().add("{").add("$S:[", FIELDS_NAME).add(select.getExampleFields()).add("]}").build().toString();
    }
    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        select.addParams(params,filters);
    }
    protected abstract BaseGet<R,M> makeSelect(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent);
}
