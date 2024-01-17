package org.example.read_json.rest_controller_json;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.filter.EndpointFilters;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.Pseudonyms;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;

import java.util.Map;
import static org.example.read_json.rest_controller_json.RestJson.KeyWords.*;

@Slf4j
public class RestJson {
    @UtilityClass
    static class KeyWords{
        static String CONFIG="config_bd";
        static String HTTP="http";
        static  String PSEUDONYMS="pseudonyms";
        static String FILTERS="filters";
    }

    ConfigBd config;
    Endpoints http;
    @Getter
    RestJsonPseudonyms pseudonyms;
    @Getter
    RestJsonFilters filters;

    public RestJson(Map<String,Object> map) {
        try {
            config=new ConfigBd(MakeCast.makeMapAndCheckKey(map,CONFIG),this);
            http=new Endpoints(MakeCast.makeMapAndCheckKey(map,HTTP),this);
            pseudonyms=new RestJsonPseudonyms(MakeCast.makeMapOfMapOfList(map,PSEUDONYMS,false),this);
            filters=new RestJsonFilters(MakeCast.makeStringMap(map,FILTERS,false),this);
        }catch (IllegalArgumentException ex){
            log.debug(ex.getMessage());
            //TODO stop compilation
        }
    }

}
