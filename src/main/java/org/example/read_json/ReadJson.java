package org.example.read_json;

import java.io.File;
import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadJson implements LoadJson<List<Object>> {
    @Override
    public List<Object> load(String jsonPath) throws IOException {
        ObjectMapper objectMapper=new ObjectMapper();
        return objectMapper.readValue(new File(jsonPath), new TypeReference<>(){});
    }

}
