package org.example.analize.request.post.insert;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.MethodSpec;
import org.example.analize.interpretation.InterpretationBd;
import org.example.analize.premetive.fields.BaseField;
import org.example.analize.premetive.fields.BaseFieldInsertUpdate;
import org.example.analize.premetive.fields.Field;
import org.example.analize.premetive.fields.FieldFieldInsertUpdate;
import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;
import org.example.analize.select.Select;

import org.example.analize.select.port_request.PortRequestWithCondition;
import org.example.analize.select.port_request.PortRequest;
import org.example.analize.select.port_request.WereInterpret;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.jooq.impl.DSL;

import java.util.List;


import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.CONTEXT;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.*;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.DB.FIELD_CLASS;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_BODY;
import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Filter.REQUEST_PARAM_NAME;
import static org.example.read_json.rest_controller_json.JsonKeyWords.Endpoint.Types.FIELDS;


public class Insert extends BaseInsert<CodeBlock, ClassName, List<CodeBlock>, MethodSpec.Builder> {

    public Insert(String request, List<String> fields, List<String> returnFields, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        super(request, fields, returnFields, select, parent);
    }

    CodeBlock makeReturnFields() {
        return returnFields.stream().map(InterpretationBd::interpret)
                .reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build()).orElse(CodeBlock.builder().build());
    }

    CodeBlock makeReturnPort() {
        if (isAlwaysReturn()) {
            var block = CodeBlock.builder().add(".returning($T.field($S).as($S)", DSL_CLASS, tableName + "." + ref, getFieldNameChose());
            if (isReturnSomething()) {
                block.add(", ").add(makeReturnFields());
            }
            block.add(")");
            return block.build();
        }
        if (isReturnSomething()) {
            var block = CodeBlock.builder().add(".returning(").add(makeReturnFields()).add(")");
            return block.build();
        }
        return CodeBlock.builder().build();
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makePortRequest(String tableName, PortRequestWithCondition<CodeBlock> select, Endpoint parent, boolean isPathFound) {
        return new PortRequest(tableName, select, parent, isPathFound);
    }

    @Override
    protected BaseField<CodeBlock> makeReturnField(String name, String table, Endpoint parent) {
        return new Field(name, table, parent);
    }

    @Override
    public MethodSpec.Builder addReturn(MethodSpec.Builder method) {
        if (isExecute()) {
            return method;
        }
        if (isFetchOne()) {
            method.addStatement(CodeBlock.builder().add("$T ", PARAMETRIZED_MAP)
                    .add(RESULT_MAP).add("=new $T<>()", HASH_MAP_CLASS).build());
            returnFields.forEach(field ->
                    method.addStatement(CodeBlock.builder()
                            .add(RESULT_MAP).add(".put(").add("$S, ", field.getName())
                            .add(RESULT_NAME).add(".getValue($S)", field.getName()).add(")").build())
            );
            return method.addStatement("return " + RESULT_MAP);
        }
        return method.addStatement("return " + RESULT_NAME+".intoMaps()");
    }


    @Override
    protected BaseFieldInsertUpdate<CodeBlock, ClassName> makeField(String name, String table, Endpoint parent) {
        return new FieldFieldInsertUpdate(name, table, parent);
    }


    @Override
    public List<CodeBlock> interpret() {
        return switch (insertType) {
            case MANY_TO_ONE -> List.of(makeManyToOneInsert().toBuilder()
                            .add(makeReturnPort()).build(),
                    makeManyToOneUpdate());//Insert(return)->update
            case ONE_TO_MANY -> List.of(makeOneToManyInsertFromSelect().toBuilder()
                    .add(makeReturnPort()).build());//Insert from select
            case MANY_TO_MANY -> List.of(makeManyToManyInsert().toBuilder()
                            .add(makeReturnPort()).build(),
                    makeManyToManyInsertFromSelect());//Insert(return)->Insert from select

            default  /*ONLY_INSERT*/ -> List.of(makeOnlyInsert().toBuilder()
                    .add(makeReturnPort()).build()); //Insert
        };
    }

    /***MANY_TO_ONE***/
    CodeBlock makeManyToOneInsert() {
        return makeManyToManyInsert();
    }

    CodeBlock makeManyToOneUpdate() {
        var block = CodeBlock.builder().add(CONTEXT + ".update($T.table($S)", DSL_CLASS, selectNext.getRealTableName());
        if (!selectNext.getRealTableName().equals(selectNext.getTableName())) {
            block.add(".as($S)", selectNext.getTableName());
        }
        block.add(makeChooseFields());
        block.add(WereInterpret.makeWhere(
                selectNext.getWhere(), selectNext.getSelectNext(),
                selectNext.getTableName(), selectNext.getRef()));
        return block.build();
    }

    protected CodeBlock makeChooseFields() {
        var block = CodeBlock.builder().add(").set($T.of(", MAP_CLASS);
        block.add("$T.field($S)", DSL_CLASS, selectNext.getId())
                .add(", $T.val(", DSL_CLASS).add(getFieldNameChose()).add(")").add("))");
        return block.build();
    }

    /***MANY_TO_MANY***/
    CodeBlock makeManyToManyInsert() {
        return makeOnlyInsert();
    }

    CodeBlock makeManyToManyInsertFromSelect() {
        return CodeBlock.builder()
                .add(CONTEXT + ".insertInto(")
                .add(makeManyToManyInsertFromSelectInsertPort())
                .add(")")
                .add(makeManyToManyInsertFromSelectValuesPort()).build();
    }

    CodeBlock makeManyToManyInsertFromSelectInsertPort() {
        var block = CodeBlock.builder().add("$T.table($S)", DSL_CLASS, selectNext.getRealTableName());
        block.add(", $T.field($S)", DSL_CLASS, selectNext.getRef());
        block.add(", $T.field($S)", DSL_CLASS, selectNext.getId());
        return block.build();
    }

    CodeBlock makeManyToManyInsertFromSelectValuesPort() {
        var block = CodeBlock.builder();
        block.add(".select(($T)" + CONTEXT + ".select(", SELECT_CLASS)
                .add("$T.field($S)", DSL_CLASS, selectNext.getSelectNext().getTableName() + "." + selectNext.getSelectNext().getId());
        block.add(", ").add("$T.val(", DSL_CLASS).add(getFieldNameChose()).add(")");
        block.add(").from($T.table($S)", DSL_CLASS, selectNext.getSelectNext().getRealTableName());
        if (!selectNext.getSelectNext().getTableName().equals(selectNext.getSelectNext().getRealTableName())) {
            block.add(".as($S)", selectNext.getSelectNext().getTableName());
        }
        block.add(WereInterpret.makeWhere(selectNext.getSelectNext().getWhere(), selectNext.getSelectNext().getSelectNext(),
                selectNext.getSelectNext().getTableName(), selectNext.getSelectNext().getRef())).add("))");
        return block.build();
    }

    /***ONE_TO_MANY***/
    CodeBlock makeOneToManyInsertFromSelect() {
        return CodeBlock.builder()
                .add(CONTEXT + ".insertInto(")
                .add(makeOneToManyInsertFromSelectInsertPort())
                .add(")")
                .add(makeOneToManyInsertFromSelectValuesPort()).build();
    }

    CodeBlock makeOneToManyInsertFromSelectInsertPort() {
        var block = CodeBlock.builder().add("$T.table($S)", DSL_CLASS, realTableName);
        block.add(", $T.field($S)", DSL_CLASS, ref);
        if (!fields.isEmpty()) {
            block.add(", ").add(makeFields());
        }
        return block.build();
    }

    CodeBlock makeOneToManyInsertFromSelectValuesPort() {
        var block = CodeBlock.builder();
        block.add(".select(($T)" + CONTEXT + ".select(", SELECT_CLASS)
                .add("$T.field($S)", DSL_CLASS,
                        selectNext.getTableName() + "." + selectNext.getId());
        if (!fields.isEmpty()) {
            block.add(", ").add(values());
        }
        block.add(").from($T.table($S)", DSL_CLASS, selectNext.getRealTableName());
        if (!selectNext.getTableName()
                .equals(selectNext.getRealTableName())) {
            block.add(".as($S)", selectNext.getTableName());
        }
        block.add(WereInterpret.makeWhere(selectNext.getWhere(),
                selectNext.getSelectNext(),
                selectNext.getTableName(),
                selectNext.getRef())).add("))");
        return block.build();
    }

    /***ONLY_INSERT***/
    CodeBlock makeOnlyInsert() {
        return CodeBlock.builder()
                .add(CONTEXT + ".insertInto(")
                .add(makeOnlyInsertInsertPort())
                .add(")")
                .add(makeOnlyInsertValuesPort()).build();
    }

    CodeBlock makeOnlyInsertInsertPort() {
        var block = CodeBlock.builder().add("$T.table($S)", DSL_CLASS, realTableName);
        if (!fields.isEmpty()) {
            block.add(", ").add(makeFields());
        }
        return block.build();
    }

    CodeBlock makeOnlyInsertValuesPort() {
        var block = CodeBlock.builder();
        if (!fields.isEmpty()) {
            block.add(".values(").add(values()).add(")");
            return block.build();
        }
        return block.add(".defaultValues()").build();
    }


    CodeBlock makeValue(BaseFieldInsertUpdate<CodeBlock, ClassName> fieldReal) {
        return CodeBlock.builder().add("$T.val(" + REQUEST_PARAM_BODY + ".containsKey($S)?" + REQUEST_PARAM_BODY + ".get($S):" + fieldReal.getDefaultValue() + ")",
                        DSL_CLASS, fieldReal.getName(), fieldReal.getName())
                .build();
    }

    CodeBlock values() {
        return fields.stream()
                .map(this::makeValue).reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().build());
    }

    CodeBlock makeFields() {
        return fields.stream().map(InterpretationBd::interpret)
                .reduce((v, h) -> CodeBlock.builder().add(v).add(", ").add(h).build())
                .orElse(CodeBlock.builder().build());
    }


    @Override
    public void addParams(List<VarInfo> params, List<FilterInfo> filters) {
        if (isSelectExist()) {
            selectNext.addParams(params, filters);
        }
    }

    @Override
    protected PortRequestWithCondition<CodeBlock> makeSelect(String request, PortRequestWithCondition<CodeBlock> select, Endpoint parent) {
        return new Select(request, select, parent);
    }
}
