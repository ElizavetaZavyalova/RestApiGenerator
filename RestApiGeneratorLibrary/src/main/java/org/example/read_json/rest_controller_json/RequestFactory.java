package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;

import com.squareup.javapoet.TypeName;
import org.example.analize.request.BaseRequest;
import org.example.analize.request.delete.DeleteRequest;
import org.example.analize.request.get.GetRequest;
import org.example.analize.request.post.PostRequest;
import org.example.analize.request.update.patch.PatchRequest;
import org.example.analize.request.update.put.PutRequest;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.Type;


public record RequestFactory() {
    public static BaseRequest<CodeBlock, MethodSpec.Builder, TypeName> createRequestFromType(Endpoint parent, Type type) {
        String request = parent.getRequestInformation().getRequest();
        return switch (type.getRequestType()) {
            case GET -> new GetRequest(request, type.getParamsBody(), parent, type.isPorts(), type.isSort(), type.isFields());
            case POST -> new PostRequest(request, type.getParamsBody(), type.getReturnParams(), parent);
            case PATCH -> new PatchRequest(request, type.getParamsBody(), type.getReturnParams(), parent);
            case DELETE -> new DeleteRequest(request, parent);
            case PUT -> new PutRequest(request, type.getParamsBody(), type.getReturnParams(), parent);
        };
    }
}
