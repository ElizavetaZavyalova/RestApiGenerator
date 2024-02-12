package org.example.read_json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.read_json.rest_controller_json.MakeCast;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.example.read_json.rest_controller_json.JsonKeyWords.*;
import static org.example.read_json.rest_controller_json.JsonKeyWords.PSEUDONYMS;

public class ReadJson implements LoadJson<Map<String,Object>> {

    @Override
    public  Map<String,Object> load(String jsonPath) throws IllegalArgumentException {
        ObjectMapper objectMapper=new ObjectMapper();
        try {
            return objectMapper.readValue(new File(jsonPath), new TypeReference<>(){});
        } catch (IOException e) {
            throw new IllegalArgumentException("not found file:"+jsonPath);
        }
    }
    public Map<String,Object> loadEndpoints(Map<String, Object> map) throws IllegalArgumentException{
        String path= MakeCast.makeStringIfContainsKeyMap(map,HTTP,false);
        if(path.isEmpty()) {
            return MakeCast.makeMapAndCheckKey(map, HTTP);
        }
        return load(path);
    }
    public Map<String, String> loadFilters(Map<String, Object> map) throws IllegalArgumentException {
        String path = MakeCast.makeStringIfContainsKeyMap(map, FILTERS, false);
        if (path.isEmpty()) {
            return MakeCast.makeStringMap(map, FILTERS, false);
        }
        return MakeCast.makeStringMap(Map.of(FILTERS, load(path)), FILTERS, false);
    }

    public Map<String, Map<String, List<String>>> loadPseudonyms(Map<String, Object> map) throws IllegalArgumentException{
        String path=MakeCast.makeStringIfContainsKeyMap(map,PSEUDONYMS,false);
        if(path.isEmpty()) {
            return MakeCast.makeMapOfMapOfList(map, PSEUDONYMS, false);
        }
        return MakeCast.makeMapOfMapOfList(Map.of(PSEUDONYMS,load(path)), PSEUDONYMS, false);
    }

}
