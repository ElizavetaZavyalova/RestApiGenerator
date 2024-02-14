package org.example.analize.helpclass;

import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.example.read_json.rest_controller_json.endpoint.RequestInformation;
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
    static public final String funcName="endpoint";

    static public final String tableNoRef1 = "Tno1";
    static public final String tableNoRef2 = "Tno2";
    static public final String tableNoRef3 = "Tno3";
    static public final String tableNoRef4 = "Tno3";
    static public final String mmTable1 = "T1";
    static public final String mmTable2 = "T2";
    static public final String mmTable3 = "T3";
    static public final String mmTable4 = "T4";
    static public final String pTable1 = "P1";
    static public final String pTable2 = "P2";
    static public final String pTable3 = "P3";

    static public final String entity1="entity1";
    static public final List<String> entityList1=List.of("e11","e12","e13");
    static public final String entity2="entity2";
    static public final List<String> entityList2=List.of("e21","e22","e23");

    public static Endpoint makeEndpointManyToManyEndpoint() {
        Endpoint point= Mockito.mock(Endpoint.class);
        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(mmTable1,mmTable2);

        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(mmTable2,mmTable3);
        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(mmTable3,mmTable4);

        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(mmTable2,mmTable1);
        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(mmTable3,mmTable2);
        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(mmTable4,mmTable3);

        Mockito.doReturn(List.of(mmTable3)).when(point).getRealJoinName(mmTable4,mmTable2);
        Mockito.doReturn(List.of(mmTable3)).when(point).getRealJoinName(mmTable2,mmTable4);
        Mockito.doReturn(List.of(mmTable2)).when(point).getRealJoinName(mmTable1,mmTable4);
        Mockito.doReturn(List.of(mmTable2)).when(point).getRealJoinName(mmTable4,mmTable1);
        Mockito.when(point.getRealTableName(Mockito.any(String.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Mockito.when(point.findPath(Mockito.any(String.class),Mockito.any(String.class),Mockito.any(Boolean.class))).thenAnswer(invocation -> List.of(invocation.getArguments()[0],invocation.getArguments()[0]));
        return point;
    }
    public static Endpoint makePath() {
        Endpoint point= Mockito.mock(Endpoint.class);
        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(pTable1,pTable2);

        Mockito.doReturn(List.of(":",":")).when(point).getRealJoinName(pTable2,pTable3);
        Mockito.when(point.getRealTableName(Mockito.any(String.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Mockito.doReturn(List.of(pTable1,pTable2,pTable3)).when(point).findPath(pTable1,pTable3,true);
        Mockito.doReturn(List.of(pTable1,pTable2,pTable3)).when(point).findPath(pTable1,pTable3,false);
        return point;
    }

    public static Endpoint creteEndpoint(RequestInformation info){
        Endpoint point= Mockito.mock(Endpoint.class);
        Mockito.when(point.getRealJoinName(Mockito.any(String.class),Mockito.any(String.class))).thenAnswer(invocation ->List.of(":",":"));
        Mockito.when(point.getRealTableName(Mockito.any(String.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Mockito.when(point.getRealFieldName(Mockito.any(String.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        Mockito.when(point.getFuncName()).thenReturn("funcName");
        Mockito.when(point.getRequestInformation()).thenReturn(info);
        return point;
    }
    public static Endpoint creteEndpointReturnEntityName(){
        Endpoint point= Mockito.mock(Endpoint.class);
        Mockito.when(point.getEntity(Mockito.any(String.class))).thenAnswer(invocation -> List.of(invocation.getArguments()[0]));
        return point;
    }

    public static Endpoint creteEndpointReturnEntity(){
        Endpoint point= Mockito.mock(Endpoint.class);
        Mockito.when(point.getEntity(entity1)).thenAnswer(invocation -> entityList1);
        Mockito.when(point.getEntity(entity2)).thenAnswer(invocation -> entityList2);
        return point;
    }


    public static Endpoint makeEndpoint(){
        Endpoint endpoint = Mockito.mock(Endpoint.class);
        Mockito.doReturn(List.of(id_T1,T2_id)).when(endpoint).getRealJoinName(table1,table2);
        Mockito.doReturn(List.of(T2_id,id_T1)).when(endpoint).getRealJoinName( table2,table1);
        Mockito.doReturn(List.of(id_T2,":")).when(endpoint).getRealJoinName(table2,table3);
        Mockito.doReturn(List.of(":",id_T2)).when(endpoint).getRealJoinName( table3,table2);
        Mockito.doReturn(List.of(":",":")).when(endpoint).getRealJoinName(table1,table3);
        Mockito.doReturn(List.of(":",":")).when(endpoint).getRealJoinName(table3,table1);

        Mockito.doReturn(List.of(":",":")).when(endpoint).getRealJoinName(tableNoRef1,tableNoRef2);
        Mockito.doReturn(List.of(":",":")).when(endpoint).getRealJoinName(tableNoRef2,tableNoRef3);
        Mockito.doReturn(List.of(":",":")).when(endpoint).getRealJoinName(tableNoRef3,tableNoRef4);
        Mockito.doReturn(tableNoRef1).when(endpoint).getRealTableName(tableNoRef1);
        Mockito.doReturn(tableNoRef2).when(endpoint).getRealTableName(tableNoRef2);
        Mockito.doReturn(tableNoRef3).when(endpoint).getRealTableName(tableNoRef3);
        Mockito.doReturn(tableNoRef4).when(endpoint).getRealTableName(tableNoRef4);



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

        Mockito.doReturn(funcName).when(endpoint).getFuncName();
        return endpoint;
    }
}
