package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.delete.StringDeleteRequest;
import org.example.analize.request.get.StringGetRequest;
import org.example.analize.request.post.StringPostRequest;
import org.example.analize.request.update.patch.StringPatchRequest;
import org.example.analize.request.update.put.StringPutRequest;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.Type;

import java.util.List;


public record RequestFactory() {
    public static  BaseRequest<CodeBlock, MethodSpec.Builder> createRequestFromType(Endpoint parent, Type type) {
        String request = parent.getRequestInformation().getRequest();
        return switch (type.getRequestType()) {
            case GET -> new StringGetRequest(request, type.getParamsBody(), parent);
            case POST -> new StringPostRequest(request, type.getParamsBody(), parent);
            case PATCH -> new StringPatchRequest(request, type.getParamsBody(), parent);
            case DELETE -> new StringDeleteRequest(request, parent);
            case PUT -> new StringPutRequest(request, type.getParamsBody(), parent);
            default -> throw new IllegalArgumentException("no endpoint type");
        };
    }
}
