package org.example.read_json;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReadJson implements LoadJson<List<Object>> {
    @Override
    public List<Object> load(String jsonPath) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(new File(jsonPath), new TypeReference<>(){});
    }

}
