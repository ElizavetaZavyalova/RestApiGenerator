package org.example.read_json.rest_controller_json;

import com.squareup.javapoet.MethodSpec;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.example.processors.code_gen.file_code_gen.DefaultsVariablesName;
import org.example.read_json.ReadJson;
import org.example.read_json.rest_controller_json.endpoint.Endpoint;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.example.read_json.rest_controller_json.RestJsonTest.Constants.*;

@Slf4j
class RestJsonTest {
   static Map<String,Object> json;
   record Constants(){
      static final String controllerName="ControllerName";
      static final String repositoryName="RepositoryName";
      static final String repositoryPath="path.repository";
      static final String controllerPath="path.controller";
      static final String lockation="lockation";
      static final String beanName="bean";
   }
   @BeforeAll
   @SneakyThrows
   static void loadJson(){
      ReadJson readJson=new ReadJson();
      json=readJson.load("P:\\Projects\\JetBrains\\IntelliJIDEA\\vkr\\RestApiGenerator\\RestApiGeneratorLibrary\\src\\test\\resources\\rest\\rest.json");
   }
   void test(Map<String, Object> object,String name){
      log.info(name);
      log.info(object.toString());
      RestJson rest=new RestJson(object,lockation,beanName);
      log.info("\n"+rest.getJavaRepository(repositoryName,repositoryPath).toString());
      log.info("\n"+rest.getJavaController(controllerName,controllerPath,repositoryName,repositoryPath).toString());

   }
   void testDebug(Map<String, Object> object,String name){
      log.info(name);
      log.info(object.toString());
      DefaultsVariablesName.DEBUG=true;
      RestJson rest=new RestJson(object,lockation,beanName);
      log.info("\n"+rest.getConnectionBean().toString());
      DefaultsVariablesName.DEBUG=false;
   }


   @ParameterizedTest(name = "{arguments} test")
   @MethodSource("restTest")
   void restTestDebug(String name) {
      testDebug((Map<String, Object>) json.get(name),name);
   }
   @ParameterizedTest(name = "{arguments} test")
   @MethodSource("restTest")
   void restTest(String name) {
      test((Map<String, Object>) json.get(name),name);
   }
   static public Stream<Arguments> restTest() {
      return json.keySet().stream().filter(k->k.startsWith("Rest"))
              .map(Arguments::of).toList().stream();
   }
}