package org.example.read_json;

import java.io.IOException;


public interface LoadJson<Result> {
    public Result  load(String jsonPath) throws IOException;
}
