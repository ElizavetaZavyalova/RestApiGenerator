package org.example.repository;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Select;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.util.MultiValueMap;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class TestOneRequestRepository {
  private static final Logger log = Logger.getLogger(TestOneRequestRepository.class.getName());;

  private final Boolean showSql;

  private final DSLContext context;

  @Autowired
  public TestOneRequestRepository(DSLContext dsl,
      @Value("${restApi.showSql:false}") Boolean showSql) {
    this.context = dsl;
    this.showSql = showSql;
  }

  public void deleteDeleteAll() {
    var result = context.deleteFrom(DSL.table("table1"));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[35m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    result.execute();
  }

  public void deleteDeleteFromTable1ById(long id) {
    var result = context.deleteFrom(DSL.table("table1")).where(DSL.field("table1.id").eq(id));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[35m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    result.execute();
  }

  public List<Map<String, Object>> getGetAll(MultiValueMap<String, String> filterParam) {
    var result = context.select().from(DSL.table("table1"));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[34m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    List<Map<String, Object>> resultList = new ArrayList<>();
    result.fetch().forEach(r ->  {
      Map<String, Object> resultMap = new  HashMap<>();
      Arrays.stream(r.fields()).forEach(field -> resultMap.put(field.getName(), r.getValue(field)));
      resultList.add(resultMap);
    }
    );
    return resultList;
  }

  public List<Map<String, Object>> getGetTable1(MultiValueMap<String, String> filterParam,
      long id) {
    Map<String,String>fieldsName=Map.of("fieldStr1", "fieldStr1", "fieldInt1", "fieldInt1", "fieldBool1", "fieldBool1");
    List<Field<Object>>listDefault=List.of(DSL.field("table1.fieldStr1").as("fieldStr1"), DSL.field("table1.fieldInt1").as("fieldInt1"), DSL.field("table1.fieldBool1").as("fieldBool1"));
    if(filterParam.containsKey("fields")) {
      List<Field<Object>>list=filterParam.get("fields").parallelStream().filter(f->fieldsName.containsKey(f)).map(f->DSL.field("table1."+fieldsName.get(f)).as(f)).toList();
      if(!list.isEmpty()) {
        listDefault=list;
      }
    }
    var result = context.select(listDefault).from(DSL.table("table1")).where(DSL.field("table1.id").eq(id));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[34m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    List<Map<String, Object>> resultList = new ArrayList<>();
    result.fetch().forEach(r ->  {
      Map<String, Object> resultMap = new  HashMap<>();
      Arrays.stream(r.fields()).forEach(field -> resultMap.put(field.getName(), r.getValue(field)));
      resultList.add(resultMap);
    }
    );
    return resultList;
  }

  public List<Map<String, Object>> getGetTable7(MultiValueMap<String, String> filterParam) {
    var result = context.select().from(DSL.table("table7"));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[34m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    List<Map<String, Object>> resultList = new ArrayList<>();
    result.fetch().forEach(r ->  {
      Map<String, Object> resultMap = new  HashMap<>();
      Arrays.stream(r.fields()).forEach(field -> resultMap.put(field.getName(), r.getValue(field)));
      resultList.add(resultMap);
    }
    );
    return resultList;
  }

  public void postPostFromTable1ByIdToTable7(long id, Map<String, Object> entity) {
    var result = context.insertInto(DSL.table("table7"), DSL.field("table1_id"), DSL.field("fieldInt7"), DSL.field("fieldStr7")).select((Select)context.select(DSL.field("table1.id"), DSL.val(entity.containsKey("fieldInt7")?entity.get("fieldInt7"):6), DSL.val(entity.containsKey("fieldStr7")?entity.get("fieldStr7"):"str")).from(DSL.table("table1").where(DSL.field("table1.id").eq(id))));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[32m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    result.execute();
  }

  public void patchpatch(String fieldStr1, int fieldInt1, Map<String, Object> entity) {
    var result = context.update(DSL.table("table1")).set(Map.of(DSL.field("fieldStr1"), !entity.containsKey("fieldStr1")?DSL.field("fieldStr1"):DSL.val(entity.get("fieldStr1")), DSL.field("fieldInt1"), !entity.containsKey("fieldInt1")?DSL.field("fieldInt1"):DSL.val(entity.get("fieldInt1")), DSL.field("fieldBool1"), !entity.containsKey("fieldBool1")?DSL.field("fieldBool1"):DSL.val(entity.get("fieldBool1")))).where(DSL.and(DSL.field("table1.fieldStr1").eq(fieldStr1), DSL.field("table1.fieldInt1").eq(fieldInt1)));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[36m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    result.execute();
  }

  public void postpostR1(Map<String, Object> entity) {
    var result = context.insertInto(DSL.table("table1"), DSL.field("fieldStr1"), DSL.field("fieldInt1"), DSL.field("fieldBool1")).values(DSL.val(entity.containsKey("fieldStr1")?entity.get("fieldStr1"):"str"), DSL.val(entity.containsKey("fieldInt1")?entity.get("fieldInt1"):0), DSL.val(entity.containsKey("fieldBool1")?entity.get("fieldBool1"):true));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[32m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    result.execute();
  }

  public void putput(long id, Map<String, Object> entity) {
    var result = context.update(DSL.table("table1")).set(Map.of(DSL.field("fieldStr1"), DSL.val(entity.containsKey("fieldStr1")?entity.get("fieldStr1"):"str"), DSL.field("fieldInt1"), DSL.val(entity.containsKey("fieldInt1")?entity.get("fieldInt1"):0), DSL.field("fieldBool1"), DSL.val(entity.containsKey("fieldBool1")?entity.get("fieldBool1"):true))).where(DSL.field("table1.id").eq(id));
    if (showSql) {
      log.log(Level.INFO, "\n"
              + "\u001b[33m"+result.getSQL()+"\n"
              + "\u001b[0m");
    }
    result.execute();
  }
}
