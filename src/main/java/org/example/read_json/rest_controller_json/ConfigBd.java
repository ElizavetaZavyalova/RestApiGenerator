package org.example.read_json.rest_controller_json;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

import static org.example.read_json.rest_controller_json.ConfigBd.KeyWords.*;

@Getter
@Setter
@Slf4j
public class ConfigBd {
    private String dialect;
    private String bdName;
    private String user;
    private String password;
    private String url;
    private Boolean perms = false;
    ConfigBd(Map<String, Object> configJson) {
        try {
            dialect = MakeCast.makeString(configJson, DIALECT, true);
            bdName = MakeCast.makeString(configJson, BD_NAME, false);
            user = MakeCast.makeString(configJson, USER, false);
            password = MakeCast.makeString(configJson, PASSWORD, false);
            url = MakeCast.makeString(configJson, URL, true);
            perms = MakeCast.makeBoolean(configJson, PERMS, false);
        } catch (IllegalArgumentException ex) {
            log.debug(ex.getMessage());
            //TODO stop compilation;
        }
    }



    record KeyWords() {
        static String DIALECT = "dialect";
        static String BD_NAME = "bd_name";
        static String USER = "user";
        static String PASSWORD = "password";
        static String URL = "url";
        static String PERMS = "perms";
    }

}
