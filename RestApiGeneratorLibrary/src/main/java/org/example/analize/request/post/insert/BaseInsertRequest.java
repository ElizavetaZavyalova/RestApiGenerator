package org.example.analize.request.post.insert;

import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.BaseFieldReal;
import org.example.analize.select.port_request.PortRequest;
import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.where.BaseWhere;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;

import java.util.List;

public abstract class BaseInsertRequest<R,N> extends PortRequest<R> {
    protected List<BaseFieldReal<R,N>> fields;

    protected BaseInsertRequest(String request, List<String> fields, PortRequestWithCondition<R> select, Endpoint parent) {
        super.initTableName(request, select, parent);
        super.setJoins(parent,false);
        this.fields = fields.stream().map(fieldName -> makeField(fieldName, tableName, parent)).toList();
    }


    protected PortRequestWithCondition<R> getAddress() {
        if (isAddressExist()) {
            return selectNext.getSelectNext();
        }
        return null;
    }

    protected boolean isAddressExist() {
        if (isSelectExist()) {
            return selectNext.getSelectNext()!=null;
        }
        return false;
    }

    protected PortRequestWithCondition<R> getSelectPort() {
        if (isSelectExist()) {
            return selectNext;
        }
        return null;
    }
    protected BaseWhere<R> getWherePort() {
       return getSelectPort().getWhere();
    }


    protected abstract BaseFieldReal<R,N> makeField(String name, String table, Endpoint parent);
}
