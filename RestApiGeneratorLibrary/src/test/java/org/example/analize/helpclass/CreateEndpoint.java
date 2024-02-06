package org.example.analize.helpclass;

import org.example.read_json.rest_controller_json.Endpoint;
import org.mockito.Mockito;

import java.util.List;

public record CreateEndpoint() {
    static public final String realFieldName1 = "RF1";
    static public final String realFieldName3 = "RF3";
    static public final String realFieldName4 = "RF4";
    static public final String fieldName1 = "f1";
    static public final String fieldName2 = "f2";
    static public final String fieldName3 = "f3";
    static public final String fieldName4 = "f4";
    static public final String paramName1 = "p1";
    static public final String paramName2 = "p2";
    static public final String paramName3 = "p3";
    static public final String paramName4 = "p4";
    static public final String realParamName1 = "rp1";
    static public final String realParamName3 = "rp3";
    static public final String realParamName4 = "rp4";
    static public final String table1 = "T1";
    static public final String table2 = "T2";
    static public final String table3 = "T3";
    static public final String realTableName1 = "T1R";
    static public final String realTableName2 = "T2R";
    static public final String id_T1="id_T1";
    static public final String id_T2="id_T2";
    static public final String T2_id="T2_id";

    public static Endpoint makeEndpoint(){
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(List.of(id_T1,T2_id)).when(endpoint).getRealJoinName(table1,table2);
        Mockito.doReturn(List.of(T2_id,id_T1)).when(endpoint).getRealJoinName( table2,table1);
        Mockito.doReturn(List.of(id_T2,":")).when(endpoint).getRealJoinName(table2,table3);
        Mockito.doReturn(List.of(":",id_T2)).when(endpoint).getRealJoinName( table3,table2);
        Mockito.doReturn(List.of(":",":")).when(endpoint).getRealJoinName(table1,table3);
        Mockito.doReturn(List.of(":",":")).when(endpoint).getRealJoinName(table3,table1);

        Mockito.doReturn(realFieldName1).when(endpoint).getRealFieldName(fieldName1);
        Mockito.doReturn(fieldName2).when(endpoint).getRealFieldName(fieldName2);
        Mockito.doReturn(realFieldName3).when(endpoint).getRealFieldName(fieldName3);
        Mockito.doReturn(realFieldName4).when(endpoint).getRealFieldName(fieldName4);

        Mockito.doReturn(realTableName1).when(endpoint).getRealTableName(table1);
        Mockito.doReturn(realTableName2).when(endpoint).getRealTableName(table2);
        Mockito.doReturn(table3).when(endpoint).getRealTableName(table3);

        Mockito.doReturn(realParamName1).when(endpoint).getRealFieldName(paramName1);
        Mockito.doReturn(paramName2).when(endpoint).getRealFieldName(paramName2);
        Mockito.doReturn(realParamName3).when(endpoint).getRealFieldName(paramName3);
        Mockito.doReturn(realParamName4).when(endpoint).getRealFieldName(paramName4);
        return endpoint;
    }
}
