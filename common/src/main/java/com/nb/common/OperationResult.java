package com.nb.common;

import lombok.Data;

@Data
public class OperationResult {

  private String uuid;

  public OperationResult(String uuid) {
    this.uuid = uuid;
  }
}
