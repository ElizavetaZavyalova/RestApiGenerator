package org.example;

import lombok.extern.java.Log;
import org.example.analize.Variables;
import org.example.analize.address.address.BuildAddress;
import org.junit.jupiter.api.Test;

@Log
public class AddressTest {
    protected Variables variables=new Variables();
    void creation(String[] request){
        StringBuilder comit=new StringBuilder();
        String from="from:"+String.join("/",request);
        log.info("-------------------"+from+"--------------------------");
        comit.append("CreateTree");
        BuildAddress address=new BuildAddress(request,variables);
        log.info("CreateTree");
        comit.append("\ninterpretRequest:").append(address.requestInterpret());
        log.info("InterpretRequest");
        comit.append("\nInterpret:").append(address.interpret());
        log.info("Interpret");
        log.info(comit.toString());
        log.info("-----------------------------------------------");
    }

        @Test
        void testNoAddress() {
            String[] request = {"last"};
            creation(request);
        }

        @Test
        void testAddressTable() {
            String[] request = {"Ttable", "last"};
            creation(request);
        }

        @Test
        void testAddressTableWhere() {
            String[] request = {"Ttable?{@id}", "last"};
            creation(request);
        }

        @Test
        void testAddressTableWhereAnd() {
            String[] request = {"Ttable?{@id}&{val}", "last"};
            creation(request);
        }

        @Test
        void testAddressTableWhereOr() {
            String[] request = {"Ttable?{@id}|{val}", "last"};
            creation(request);
        }

        @Test
        void testAddressTableWhereOrEq() {
            String[] request = {"Ttable?id={@id}|{val}", "last"};
            creation(request);
        }

        @Test
        void testAddressTableWhereOrEqNoVar() {
            String[] request = {"Ttable?id=@1|={val}", "last"};
            creation(request);
        }

        @Test
        void testAddressTableMaxMin() {
            String[] requestMax = {"Ttable:M?id=@1|{val}", "last"};
            creation(requestMax);
            String[] requestMin = {"Ttable:m?id=@1|{val}", "last"};
            creation(requestMin);
        }

        @Test
        void testAddressTableId() {
            String[] requestId = {"Ttable:IName?id=@1|{val}", "last"};
            creation(requestId);
        }

        @Test
        void testAddressTablePrev() {
            String[] requestId = {"Ttable:PName?id=@1|{val}", "last"};
            creation(requestId);
        }

        @Test
        void testAddressTablePrevId() {
            String[] requestId = {"Ttable:PName:IName?id=@1|{val}", "last"};
            creation(requestId);
        }
}
