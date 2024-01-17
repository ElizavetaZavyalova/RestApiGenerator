package org.example.read_json.rest_controller_json.filter.filters_vies;

import lombok.AllArgsConstructor;
import lombok.Getter;
@AllArgsConstructor
@Getter
public abstract class Filter<Result> implements Filtering<Result> {
    public  FilterNames names;
    public enum FilterNames{
        AND(":and"),OR(":or"),CALL(":call"),SQL(":sql");
        @Getter
        final String name;
        FilterNames(String name){
            this.name=name;
        }
        public int length(){
            return name.length();
        }
    }
}
