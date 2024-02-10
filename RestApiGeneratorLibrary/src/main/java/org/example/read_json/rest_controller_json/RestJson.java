package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.example.read_json.rest_controller_json.filter.RestJsonFilters;
import org.example.read_json.rest_controller_json.pseudonyms.RestJsonPseudonyms;

import java.util.Map;
import java.util.Optional;

import static org.example.read_json.rest_controller_json.JsonKeyWords.*;

@Slf4j
public class RestJson {
    @Getter
    String locationName;
    String dialect;
    Endpoints http;
    @Getter
    RestJsonPseudonyms pseudonyms;
    @Getter
    RestJsonFilters filters;
    String beanName="";


    public RestJson(Map<String,Object> map,String locationName,String beanName) {
        try {
            this.beanName= Optional.ofNullable(beanName).orElse(locationName);
            this.locationName=locationName;
            dialect =MakeCast.makeStringIfContainsKeyMap(map, DIALECT, true);
            http=new Endpoints(MakeCast.makeMapAndCheckKey(map,HTTP),this);
            pseudonyms=new RestJsonPseudonyms(MakeCast.makeMapOfMapOfList(map,PSEUDONYMS,false),this);
            filters=new RestJsonFilters(MakeCast.makeStringMap(map,FILTERS,false),this);
        }catch (IllegalArgumentException ex){
            log.debug(ex.getMessage());
            //TODO stop compilation
        }
    }
    public JavaFile getJavaRepository(String className,String packageName){
        return JavaFile.builder(packageName,http.createRepository(className,beanName)).build();

    }
    public JavaFile getJavaController(String className,String packageName,String repositoryName,String repositoryPath){
       return JavaFile.builder(packageName,http.createController(className,repositoryName,repositoryPath)).build();
    }
    public MethodSpec getConnectionBean(){
         return null;
    }



}
