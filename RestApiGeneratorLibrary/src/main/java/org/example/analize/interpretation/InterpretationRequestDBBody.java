package org.example.analize.interpretation;

public interface InterpretationRequestDBBody<M,P> {
    M makeMethodBody(M method);
    P returnParam();
}
