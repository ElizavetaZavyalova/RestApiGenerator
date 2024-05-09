package org.example.analize.address;

import lombok.Getter;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.Arrays;

import static org.example.analize.address.BaseAddress.RegExp.*;

@Getter
public abstract class BaseAddress<R> implements Interpretation<R> {
    record RegExp() {
        static final String SPLIT = "/(?![{\\[(])";
        static final int START = 0;
        static final int NO_URL=0;
    }


    PortRequestWithCondition<R> selectCurrent = null;
    protected boolean isSelectCurrentExist(){
        return selectCurrent!=null;
    }

    String endUrl;

    BaseAddress(String url, Endpoint parent) throws IllegalArgumentException {
        String[] urlPorts = Arrays.stream(url.split(SPLIT))
                .filter(urlPort -> !urlPort.isEmpty()).toArray(String[]::new);
        if (urlPorts.length == NO_URL) {
            throw new IllegalArgumentException("no url");
        }
        final int LAST_PORT = urlPorts.length - 1;
        endUrl = urlPorts[LAST_PORT];
        for (int urlPortIndex = START; urlPortIndex < LAST_PORT; urlPortIndex++) {
            selectCurrent = makeSelect(urlPorts[urlPortIndex], selectCurrent, parent);
        }
    }


    abstract PortRequestWithCondition<R> makeSelect(String request, PortRequestWithCondition<R> select, Endpoint parent);
}
