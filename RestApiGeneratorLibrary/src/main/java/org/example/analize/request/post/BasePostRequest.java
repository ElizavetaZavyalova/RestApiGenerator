package org.example.analize.request.post;

import com.squareup.javapoet.CodeBlock;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.post.insert.BaseInsert;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BasePostRequest<R,M,N> extends BaseRequest<R,M> {

    protected BaseInsert<R,N> insert;

    protected BasePostRequest(String url, List<String> params, Endpoint parent) throws IllegalArgumentException {
        super();
        super.init(url,parent);
        insert = makeBaseInsertRequest(address.getEndUrl(), params, address.getSelectCurrent(), parent);
    }
    @Override
    public String getExampleEntity(){
        return CodeBlock.builder().add("{").add(insert.getExampleFields()).add("}").build().toString();
    }

    @Override
    public void addParams(List<VarInfo> params,List<FilterInfo> filters) {
        this.insert.addParams(params,filters);
    }

    abstract BaseInsert<R,N> makeBaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent);

}
