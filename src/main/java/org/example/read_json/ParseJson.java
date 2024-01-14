package org.example.read_json;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.MakeCast;
import org.example.read_json.rest_controller_json.RestJson;

import java.io.IOException;
import java.util.List;
import java.util.Map;
@Slf4j
public class ParseJson {
    static LoadJson<List<Object>> loadJson = new ReadJson();
    List<RestJson> restsJson;

    ParseJson(String jsonPath) {
        try {
            restsJson = loadJson.load(jsonPath).stream()
                    .map(object -> new RestJson(MakeCast.makeMap(object, jsonPath)))
                    .toList();
        } catch (IOException|IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO compile exeption
        }
    }


}
