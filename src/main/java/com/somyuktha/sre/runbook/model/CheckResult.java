package com.somyuktha.sre.runbook.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class CheckResult {
  public String checkType;
  public String target;
  public boolean ok;
  public Instant startedAt;
  public Instant finishedAt;
  public Long latencyMs;
  public String message;
  public Map<String, Object> details = new LinkedHashMap<>();

  public static CheckResult start(String checkType, String target) {
    CheckResult r = new CheckResult();
    r.checkType = checkType;
    r.target = target;
    r.startedAt = Instant.now();
    return r;
  }
}

