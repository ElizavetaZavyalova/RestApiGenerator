package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import javax.lang.model.element.Modifier;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Slf4j
public class Endpoints {
    Map<String, Endpoint> endpoint = new TreeMap<>();
    @Getter
    RestJson parent;
    String repositoryName;

    Endpoints(Map<String, Object> map, RestJson parent, String repositoryName) throws IllegalArgumentException {
        this.repositoryName = repositoryName;
        this.parent = parent;
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            endpoint.put(entry.getKey(), new Endpoint(MakeCast.makeMap(entry.getValue(), entry.getKey()), this, entry.getKey()));
        }

    }

    TypeSpec createRepository() {
        List<MethodSpec> methods = endpoint.entrySet().
                parallelStream().map(v -> v.getValue().getDBMethods()).flatMap(List::stream).toList();
        TypeSpec.Builder repository = TypeSpec.classBuilder(repositoryName)
                .addModifiers(Modifier.PUBLIC);
        methods.forEach(repository::addMethod);
        return repository.build();
    }


}
