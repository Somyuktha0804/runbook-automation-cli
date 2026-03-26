package com.somyuktha.sre.runbook;

import com.somyuktha.sre.runbook.model.CheckResult;
import com.somyuktha.sre.runbook.util.JsonIO;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "http", mixinStandardHelpOptions = true, description = "HTTP GET check for an endpoint.")
public final class HttpCheckCommand implements Runnable {
  @Option(names = "--url", required = true, description = "URL to check, e.g. https://example.com")
  private String url;

  @Option(names = "--timeout-ms", defaultValue = "3000", description = "Timeout in milliseconds.")
  private int timeoutMs;

  @Option(names = "--json-out", description = "Optional path to write JSON report.")
  private Path jsonOut;

  @Override
  public void run() {
    CheckResult r = CheckResult.start("http", url);
    try {
      HttpClient client =
          HttpClient.newBuilder().connectTimeout(Duration.ofMillis(timeoutMs)).build();
      HttpRequest req =
          HttpRequest.newBuilder()
              .uri(URI.create(url))
              .timeout(Duration.ofMillis(timeoutMs))
              .GET()
              .build();

      Instant t0 = Instant.now();
      HttpResponse<Void> resp = client.send(req, HttpResponse.BodyHandlers.discarding());
      Instant t1 = Instant.now();

      int code = resp.statusCode();
      r.details.put("statusCode", code);
      r.latencyMs = Duration.between(t0, t1).toMillis();
      r.ok = code >= 200 && code < 400;
      r.message = r.ok ? "OK" : "Non-success status code";
    } catch (Exception e) {
      r.ok = false;
      r.message = e.getClass().getSimpleName() + ": " + e.getMessage();
    } finally {
      r.finishedAt = Instant.now();
      print(r);
      if (jsonOut != null) {
        try {
          JsonIO.write(jsonOut, r);
          System.out.println("JSON report: " + jsonOut.toAbsolutePath());
        } catch (Exception e) {
          System.err.println("Failed to write JSON: " + e.getMessage());
        }
      }
      System.exit(r.ok ? 0 : 2);
    }
  }

  private static void print(CheckResult r) {
    System.out.println("check=http target=" + r.target);
    System.out.println("ok=" + r.ok);
    if (r.latencyMs != null) System.out.println("latencyMs=" + r.latencyMs);
    if (r.details.containsKey("statusCode")) System.out.println("statusCode=" + r.details.get("statusCode"));
    if (r.message != null) System.out.println("message=" + r.message);
  }
}

