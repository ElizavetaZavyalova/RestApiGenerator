package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.CodeBlock;
import lombok.Getter;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.delete.StringDeleteRequest;
import org.example.analize.request.get.StringGetRequest;
import org.example.analize.request.post.StringPostRequest;
import org.example.analize.request.update.patch.StringPatchRequest;
import org.example.analize.request.update.put.StringPutRequest;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.Type;


public class InterpretDb {
    @Getter
    BaseRequest<CodeBlock> interpretation;

    public InterpretDb(Endpoint parent, Type type) {
        String request = parent.getRequestInformation().getRequest();
        switch (type.getRequestType()) {
            case GET -> interpretation = new StringGetRequest(request, type.getParams(), parent);
            case POST -> interpretation = new StringPostRequest(request, type.getParams(), parent);
            case PATCH -> interpretation = new StringPatchRequest(request, type.getParams(), parent);
            case DELETE -> interpretation = new StringDeleteRequest(request, parent);
            case PUT -> interpretation = new StringPutRequest(request, type.getParams(), parent);
            default -> throw new IllegalArgumentException("NO ENDPOINT TYPE");
        }
    }
}
