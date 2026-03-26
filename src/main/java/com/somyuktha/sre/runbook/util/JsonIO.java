package com.somyuktha.sre.runbook.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class JsonIO {
  private static final ObjectMapper MAPPER =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
          .enable(SerializationFeature.INDENT_OUTPUT);

  private JsonIO() {}

  public static void write(Path out, Object value) throws IOException {
    byte[] bytes = MAPPER.writeValueAsBytes(value);
    Files.createDirectories(out.toAbsolutePath().getParent());
    Files.write(out, bytes);
  }
}

