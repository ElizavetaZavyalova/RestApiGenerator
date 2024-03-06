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
        CALL(_CALL),
        AND(_AND),OR(_OR),NOT_OR(_NOT_OR),NOT_AND(_NOT_AND),
        ONE_AND(_ONE_AND),ONE_OR(_ONE_OR),ONE_NOT_OR(_ONE_NOT_OR),ONE_NOT_AND(_ONE_NOT_AND);
        final String name;
        FilterNames(String name){
            this.name=name;
        }



        public int length(){
            return name.length();
        }
        public static boolean isOr(FilterNames name){
            return name.equals(OR)||name.equals(NOT_OR)||name.equals(ONE_OR)||name.equals(ONE_NOT_OR);
        }
        public static boolean isAnd(FilterNames name){
            return name.equals(AND)||name.equals(NOT_AND)||name.equals(ONE_AND)||name.equals(ONE_NOT_AND);
        }
        public static boolean isNot(FilterNames name){
            return name.equals(NOT_AND)||name.equals(ONE_NOT_AND)||name.equals(ONE_NOT_OR)||name.equals(NOT_OR);
        }
    }
    public String getVarName(){
        return filterName;
    }
    protected String getDefault(){
        return "";
    }
}
