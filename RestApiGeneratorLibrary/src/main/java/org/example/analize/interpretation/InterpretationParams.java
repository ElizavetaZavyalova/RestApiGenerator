package org.example.analize.interpretation;

import org.example.analize.premetive.info.FilterInfo;
import org.example.analize.premetive.info.VarInfo;

import java.util.List;

@FunctionalInterface
public interface InterpretationParams  {
    void addParams(List<VarInfo> params,List<FilterInfo> filters);
}
