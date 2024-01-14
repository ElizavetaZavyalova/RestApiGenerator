package org.example.read_json;

import java.io.IOException;

@FunctionalInterface
public interface LoadJson<Result> {
    public Result  load(String jsonPath) throws IOException;
}
