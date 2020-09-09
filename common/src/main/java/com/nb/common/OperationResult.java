package com.nb.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OperationResult {

  private String uuid;

  public OperationResult(@JsonProperty ("uuid") String uuid) {
    this.uuid = uuid;
  }
}
