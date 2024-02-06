package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.CodeBlock;
import lombok.Getter;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.delete.StringDeleteRequest;
import org.example.analize.request.get.StringGetRequest;
import org.example.analize.request.post.StringPostRequest;
import org.example.analize.request.update.patch.StringPatchRequest;
import org.example.analize.request.update.put.StringPutRequest;

import java.util.Optional;

public class InterpretDb {
    @Getter
    BaseRequest<CodeBlock,String> interpretation;
    public InterpretDb(Endpoint parent){
        Type type=parent.getType();
        String requestBd= parent.getRequestBd();
        switch (parent.type.getRequestType()){
            case GET -> interpretation=new StringGetRequest(requestBd,type.getParams(),parent);
            case POST -> interpretation=new StringPostRequest(requestBd,type.getParams(),parent);
            case PATCH -> interpretation=new StringPatchRequest(requestBd,type.getParams(),parent);
            case DELETE -> interpretation=new StringDeleteRequest(requestBd,parent);
            case PUT-> interpretation=new StringPutRequest(requestBd,type.getParams(),parent);
            default -> throw new IllegalArgumentException("NO ENDPOINT TYPE");
        }
    }
}
