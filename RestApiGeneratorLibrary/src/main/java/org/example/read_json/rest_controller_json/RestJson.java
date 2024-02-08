package org.example.read_json.rest_controller_json;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;

import java.util.Map;

import static org.example.read_json.rest_controller_json.JsonKeyWords.*;

@Slf4j
public class RestJson {
    String location;
    String dialect;
    Endpoints http;
    @Getter
    RestJsonPseudonyms pseudonyms;
    @Getter
    RestJsonFilters filters;

    public RestJson(Map<String,Object> map,String location) {
        try {
            this.location=location;
            dialect =MakeCast.makeStringIfContainsKeyMap(map, DIALECT, true);
            http=new Endpoints(MakeCast.makeMapAndCheckKey(map,HTTP),this,location);
            pseudonyms=new RestJsonPseudonyms(MakeCast.makeMapOfMapOfList(map,PSEUDONYMS,false),this);
            filters=new RestJsonFilters(MakeCast.makeStringMap(map,FILTERS,false),this);
        }catch (IllegalArgumentException ex){
            log.debug(ex.getMessage());
            //TODO stop compilation
        }
    }

}
