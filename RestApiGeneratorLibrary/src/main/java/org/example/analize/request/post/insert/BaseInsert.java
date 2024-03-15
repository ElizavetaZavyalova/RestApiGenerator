package org.example.analize.request.post.insert;


import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.BaseFieldInsertUpdate;

import org.example.analize.select.port_request.BasePortRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;

import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.analize.request.post.insert.BaseInsert.InsertType.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.RESULT_NAME;


public abstract class BaseInsert<R, N, I,M> extends BasePortRequest<R, I> {

    protected List<BaseFieldInsertUpdate<R, N>> fields;
    protected List<BaseField<R>> returnFields;
    boolean isReturnSomething(){
        return !returnFields.isEmpty();
    }
    public String getFieldNameChose() {
        return RESULT_NAME+"_"+ref;
    }
   public boolean isAlwaysReturn(){
            return insertType.equals(MANY_TO_MANY)||insertType.equals(MANY_TO_ONE);
    }
    public boolean isReturn(){
        return isAlwaysReturn()||isReturnSomething();
    }

    protected enum InsertType {
        ONLY_INSERT,
        MANY_TO_MANY,
        ONE_TO_MANY,
        MANY_TO_ONE
    }
    public boolean isFetchOne(){
        return !insertType.equals(ONE_TO_MANY);
    }
   public boolean isFetch(){
        return insertType.equals(ONE_TO_MANY);
    }
    public boolean isExecute(){
        return  !isReturnSomething();
    }

    protected InsertType insertType = InsertType.ONLY_INSERT;

    protected BaseInsert(String request, List<String> fields, List<String> returnFields, PortRequestWithCondition<R> select, Endpoint parent) {
        super.initTableName(request, select, parent);
        super.setJoins(parent, false);
        this.fields = fields.stream().map(fieldName -> makeField(fieldName, tableName, parent)).toList();
        this.setInsertType();
        this.setReturnFields(returnFields, parent);
    }

    void setReturnFields(List<String> returnFields, Endpoint parent) {
        this.returnFields = returnFields.stream().map(fieldName -> makeReturnField(fieldName, tableName, parent)).toList();
    }

    protected abstract BaseField<R> makeReturnField(String name, String table, Endpoint parent);

    void setInsertType() {
        if (isSelectExist()) {
            if (isRefAddressPortIsManyToMay()) {
                this.insertType = MANY_TO_MANY;
                return;
            } else if (isTableRefManyToOne()) {
                this.insertType = MANY_TO_ONE;
                return;
            }
            this.insertType = InsertType.ONE_TO_MANY;
            return;

        }
        this.insertType = InsertType.ONLY_INSERT;
    }
    public abstract M addReturn(M method);

    public String getExampleFields() {
        return fields.stream().map(BaseFieldInsertUpdate::getExample).collect(Collectors.joining(", "));
    }


    protected abstract BaseFieldInsertUpdate<R, N> makeField(String name, String table, Endpoint parent);
}
