package org.example.analize.interpretation;

import org.example.analize.premetive.info.VarInfo;

import java.util.List;

@FunctionalInterface
public interface InterpretationParams  {
    public void addParams(List<VarInfo> params);
}
