package org.example.read_json.rest_controller_json;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static org.example.read_json.rest_controller_json.RestJson.KEY_WORDS.*;

@Slf4j
public class RestJson {
    @UtilityClass
    static class KEY_WORDS{
        static String CONFIG="config_bd";
        static String HTTP="http";
    }

    ConfigBd config;
    Endpoints http;

    public RestJson(Map<String,Object> map) {
        try {
            config=new ConfigBd(MakeCast.makeMapAndCheckKey(map,CONFIG));
            http=new Endpoints(MakeCast.makeMapAndCheckKey(map,HTTP));
        }catch (IllegalArgumentException ex){
            log.debug(ex.getMessage());
            //TODO stop compilation
        }
    }

}
