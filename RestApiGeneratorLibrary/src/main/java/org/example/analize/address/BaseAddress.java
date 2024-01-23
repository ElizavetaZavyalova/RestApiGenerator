package org.example.analize.address;

import lombok.Getter;
import org.example.analize.interpretation.Interpretation;
import org.example.analize.postfix_infix.Converter;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.read_json.rest_controller_json.Endpoint;

import java.util.Arrays;

import static org.example.analize.address.BaseAddress.RegExp.*;

public abstract class BaseAddress<R,C> implements Interpretation<R> {
    record RegExp() {
        static final String SPLIT = "/(?![{\\[(])";
       // static final int NOT_DELETE_EMPTY_STRING = -1;
        static final int START = 0;
    }
    @Getter
    PortRequestWithCondition<R,C> selectCurrent = null;
    @Getter
    String endUrl="";

    BaseAddress(String url, Endpoint parent)throws IllegalArgumentException {
        String[] urlPorts = Arrays.stream(url.split(SPLIT))
                .filter(urlPort->!urlPort.isEmpty()).toArray(String[]::new);
        if(urlPorts.length==0){
            throw new IllegalArgumentException("NO URL");
        }
        final int LAST_PORT = urlPorts.length - 1;
        endUrl = urlPorts[LAST_PORT];
        for (int urlPortIndex = START; urlPortIndex < LAST_PORT; urlPortIndex++) {
            selectCurrent = makeSelect(urlPorts[urlPortIndex], selectCurrent, parent);
        }
    }

    abstract PortRequestWithCondition<R,C> makeSelect(String request, PortRequestWithCondition<R,C> select, Endpoint parent);
}
