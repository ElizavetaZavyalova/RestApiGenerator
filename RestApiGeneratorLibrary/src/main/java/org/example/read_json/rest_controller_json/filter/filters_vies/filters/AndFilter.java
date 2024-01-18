package org.example.read_json.rest_controller_json.filter.filters_vies.filters;

import org.example.analize.premetive.BaseField;
import org.example.read_json.rest_controller_json.Endpoint;
import org.example.read_json.rest_controller_json.filter.filters_vies.ListFilter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AndFilter extends ListFilter<String> {
    public AndFilter(List<String> val) {
        super(FilterNames.AND, val);
    }

    public void makeCall(Endpoint parent, String tableName){
        List<BaseField> fields=new ArrayList<>();
        for(String field:this.val){
           // fields.add()
        }
    }

    @Override
    public String makeFilter(Object...args) {
        return null;
    }
}
