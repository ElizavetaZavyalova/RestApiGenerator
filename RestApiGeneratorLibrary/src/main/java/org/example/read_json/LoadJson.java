package org.example.read_json;

import java.io.IOException;


public interface LoadJson<R> {
    public R  load(String jsonPath) throws IOException;
}
