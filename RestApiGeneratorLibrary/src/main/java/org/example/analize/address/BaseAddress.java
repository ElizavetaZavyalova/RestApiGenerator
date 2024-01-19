package org.example.analize.address;

import lombok.Getter;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.select.Select;
import org.example.read_json.rest_controller_json.Endpoint;

import static org.example.analize.address.BaseAddress.RegExp.SPLIT;
import static org.example.analize.address.BaseAddress.RegExp.START;

public abstract class BaseAddress<R,C> implements Interpretation<R> {
    record RegExp() {
        static final String SPLIT = "/(?![{\\[(])";
        static final int START = 0;
    }

    Select<R,C> selectCurrent = null;
    @Getter
    String endUrl;

    BaseAddress(String url, Endpoint parent) {
        String[] urlPorts = url.split(SPLIT);
        final int LAST_PORT = urlPorts.length - 1;
        endUrl = urlPorts[LAST_PORT];
        for (int urlPortIndex = START; urlPortIndex < LAST_PORT; urlPortIndex++) {
            selectCurrent = makeSelect(urlPorts[urlPortIndex], selectCurrent, parent);
        }
    }

    abstract Select<R,C> makeSelect(String request, Select<R,C> select, Endpoint parent);
}
