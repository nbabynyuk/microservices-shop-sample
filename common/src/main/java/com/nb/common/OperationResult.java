package com.nb.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OperationResult {

  private String uuid;

  @JsonCreator
  public OperationResult(@JsonProperty ("uuid") String uuid) {
    this.uuid = uuid;
  }
}
