package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.delete.StringDeleteRequest;
import org.example.analize.request.get.StringGetRequest;
import org.example.analize.request.post.StringPostRequest;
import org.example.analize.request.update.patch.StringPatchRequest;
import org.example.analize.request.update.put.StringPutRequest;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.Type;



public record RequestFactory() {
    public static  BaseRequest<CodeBlock, MethodSpec.Builder> createRequestFromType(Endpoint parent, Type type) {
        String request = parent.getRequestInformation().getRequest();
        return switch (type.getRequestType()) {
            case GET -> new StringGetRequest(request, type.getParams(), parent);
            case POST -> new StringPostRequest(request, type.getParams(), parent);
            case PATCH -> new StringPatchRequest(request, type.getParams(), parent);
            case DELETE -> new StringDeleteRequest(request, parent);
            case PUT -> new StringPutRequest(request, type.getParams(), parent);
            default -> throw new IllegalArgumentException("no endpoint type");
        };
    }
}
