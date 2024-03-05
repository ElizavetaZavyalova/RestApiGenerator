package org.example.read_json.rest_controller_json.filter.filters_vies;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.example.read_json.rest_controller_json.JsonKeyWords.FilterSuffix.*;

@AllArgsConstructor
@Getter
public abstract class Filter<R> implements Filtering<R> {
    protected   FilterNames names;
    protected String filterName;
    @Getter
    public enum FilterNames{
        AND(_AND),OR(_OR),CALL(_CALL);
        final String name;
        FilterNames(String name){
            this.name=name;
        }
        public int length(){
            return name.length();
        }
    }
    public String getVarName(){
        return filterName;
    }
    protected String getDefault(){
        return "";
    }
}
