package org.example.analize.address;

import lombok.Getter;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.select.Select;
import org.example.read_json.rest_controller_json.Endpoint;

import static org.example.analize.address.Address.RegExp.SPLIT;
import static org.example.analize.address.Address.RegExp.START;

public abstract class Address<R> extends Interpretation<R> {
    record RegExp() {
        static final String SPLIT = "/(?![{\\[(])";
        static final int START = 0;
    }

    Select<R> selectCurrent = null;
    @Getter
    String endUrl;

    Address(String url, Endpoint parent) {
        String[] urlPorts = url.split(SPLIT);
        final int LAST_PORT = urlPorts.length - 1;
        endUrl = urlPorts[LAST_PORT];
        for (int urlPortIndex = START; urlPortIndex < LAST_PORT; urlPortIndex++) {
            selectCurrent = makeSelect(urlPorts[urlPortIndex], selectCurrent, parent);
        }
    }

    abstract Select<R> makeSelect(String request, Select<R> select, Endpoint parent);
}
