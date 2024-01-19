package org.example.read_json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ReadJson implements LoadJson<Map<String,Object>> {

    @Override
    public Map<String,Object> load(String jsonPath) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(new File(jsonPath), new TypeReference<>(){});
    }

}
