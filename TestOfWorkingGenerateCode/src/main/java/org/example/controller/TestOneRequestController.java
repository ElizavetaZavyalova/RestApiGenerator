package org.example.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.example.repository.TestOneRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class TestOneRequestController {
  private final TestOneRequestRepository testOneRequestRepository;

  @Autowired
  public TestOneRequestController(TestOneRequestRepository testOneRequestRepository) {
    this.testOneRequestRepository = testOneRequestRepository;
  }

  @DeleteMapping("table1")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
      summary = ""
  )
  public void deleteDeleteAll() {
    testOneRequestRepository.deleteDeleteAll();
  }

  @DeleteMapping("table1/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @Operation(
      summary = ""
  )
  public void deleteDeleteFromTable1ById(@PathVariable("id") long id) {
    testOneRequestRepository.deleteDeleteFromTable1ById(id);
  }

  @GetMapping("table1")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = ""
  )
  public List<Map<String, Object>> getGetAll(
      @RequestParam(defaultValue = "{}") MultiValueMap<String, String> filterParam) {
    return testOneRequestRepository.getGetAll(filterParam);
  }

  @GetMapping("table1/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = ""
  )
  public List<Map<String, Object>> getGetTable1(
      @RequestParam(defaultValue = "{}") MultiValueMap<String, String> filterParam,
      @PathVariable("id") long id) {
    return testOneRequestRepository.getGetTable1(filterParam, id);
  }

  @GetMapping("table7")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = ""
  )
  public List<Map<String, Object>> getGetTable7(
          @Parameter(
                  name = "filterParam",
                  example = "{\"fields\":[\"f\"]}",
                  description = "fieldsName return default") @RequestParam(defaultValue = "{}") MultiValueMap<String, String> filterParam,
  @RequestParam(defaultValue = "{}") MultiValueMap<String, String> filterParam1) {
    return testOneRequestRepository.getGetTable7(filterParam);
  }

  @PostMapping("table1/{id}/table7")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = ""
  )
  public void postPostFromTable1ByIdToTable7(@PathVariable("id") long id,
      @RequestBody(required = true) Map<String, Object> entity) {
    testOneRequestRepository.postPostFromTable1ByIdToTable7(id, entity);
  }

  @PatchMapping("table1/{fieldInt1-i}&{fieldStr1-s}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = ""
  )
  public void patchpatch(@PathVariable("fieldStr1-s") String fieldStr1,
      @PathVariable("fieldInt1-i") int fieldInt1,
      @RequestBody(required = true) Map<String, Object> entity) {

    testOneRequestRepository.patchpatch(fieldStr1, fieldInt1, entity);
  }

  @PostMapping("table1")
  @ResponseStatus(HttpStatus.CREATED)
  @Operation(
      summary = ""
  )
  public void postpostR1(@RequestBody(required = true) Map<String, Object> entity) {
    testOneRequestRepository.postpostR1(entity);
  }

  @PutMapping("table1/{id}")
  @ResponseStatus(HttpStatus.OK)
  @Operation(
      summary = ""
  )
  public void putput(@PathVariable("id") long id,
      @RequestBody(required = true) Map<String, Object> entity) {
    testOneRequestRepository.putput(id, entity);
  }
}
