package org.example.analize.interpretation;
@FunctionalInterface
public interface InterpretationRequestDBBody<M> {
    M makeMethodBody(M method);
}
