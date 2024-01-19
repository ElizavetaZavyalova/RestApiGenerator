package org.example.read_json;

import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.MakeCast;
import org.example.read_json.rest_controller_json.RestJson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class ParseJson {
    static LoadJson<Map<String,Object>> loadJson = new ReadJson();
    List<RestJson> restsJson=new ArrayList<>();

    public ParseJson(String jsonPath) {
        try {
            loadJson.load(jsonPath).forEach(
                    (key,object) -> restsJson.add(new RestJson(MakeCast.makeMap(object, jsonPath),key)));
        } catch (IOException|IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO compile exeption
        }
    }


}
