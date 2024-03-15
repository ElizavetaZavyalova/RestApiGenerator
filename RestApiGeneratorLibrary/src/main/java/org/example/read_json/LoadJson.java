package org.example.read_json;

import java.io.IOException;


public interface LoadJson<R> {
    R load(String jsonPath) throws IOException;
}
