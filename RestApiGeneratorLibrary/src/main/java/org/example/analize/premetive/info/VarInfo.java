package org.example.analize.premetive.info;

import com.squareup.javapoet.AnnotationSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeName;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.example.analize.premetive.BaseFieldParser;

import static org.example.processors.code_gen.file_code_gen.DefaultsVariablesName.Annotations.Controller.PATH_VARIABLE_ANNOTATION_CLASS;


@Getter
@Setter
@ToString
public class VarInfo {
    BaseFieldParser.Type type;
    String name;
    boolean filter;

    public VarInfo(BaseFieldParser.Type type, String name) {
        filter = false;
        this.type = type;
        this.name = name;
    }

    public VarInfo(String name) {
        filter = true;
        this.name = name;
    }

    public ParameterSpec getAnnotationParameterSpec() {
        ParameterSpec.Builder parameterSpec = getParameterSpec().toBuilder();
        parameterSpec.addAnnotation(AnnotationSpec.builder(PATH_VARIABLE_ANNOTATION_CLASS).build());
        return parameterSpec.build();

    }

    public ParameterSpec getParameterSpec() {
        ParameterSpec.Builder parameterSpec;
        switch (type) {
            case STRING -> parameterSpec = ParameterSpec.builder(String.class, name);
            case BOOLEAN -> parameterSpec = ParameterSpec.builder(TypeName.BOOLEAN, name);
            default -> parameterSpec = ParameterSpec.builder(TypeName.INT, name);
        }
        return parameterSpec.build();
    }

    public String getParameter() {
        return name;
    }
}
