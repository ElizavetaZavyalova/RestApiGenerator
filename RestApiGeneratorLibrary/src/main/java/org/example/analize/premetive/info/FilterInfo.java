package org.example.analize.premetive.info;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Optional;


@RequiredArgsConstructor
@Getter
public class FilterInfo {
    final String filterName;
    final String nameInRequest;
    final String example;
    final String varName;
    Boolean useFilter = null;

    public boolean isNameInRequestAndFilterNameIdent() {
        return Optional.ofNullable(useFilter).orElse(makeUseFilter());
    }

    private boolean makeUseFilter() {
        useFilter = filterName.equals(nameInRequest);
        return useFilter;
    }

    public String replaceRequest(String request){
        if(isNameInRequestAndFilterNameIdent()){
            String name="["+filterName+"]";
            if(nameInRequest.isEmpty()){
                return request.replace("&"+name,"")
                        .replace(name+"&","")
                        .replace("|"+name,"")
                        .replace(name+"|","")
                        .replace("/"+name,"");
            }
            return request.replace(name,nameInRequest);
        }
        return request;
    }

}
