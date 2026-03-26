package com.somyuktha.sre.runbook;

import com.somyuktha.sre.runbook.model.CheckResult;
import com.somyuktha.sre.runbook.util.JsonIO;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "tcp", mixinStandardHelpOptions = true, description = "TCP connect check for host:port.")
public final class TcpCheckCommand implements Runnable {
  @Option(names = "--host", required = true, description = "Hostname or IP.")
  private String host;

  @Option(names = "--port", required = true, description = "TCP port.")
  private int port;

  @Option(names = "--timeout-ms", defaultValue = "2000", description = "Timeout in milliseconds.")
  private int timeoutMs;

  @Option(names = "--json-out", description = "Optional path to write JSON report.")
  private Path jsonOut;

  @Override
  public void run() {
    String target = host + ":" + port;
    CheckResult r = CheckResult.start("tcp", target);
    try (Socket socket = new Socket()) {
      Instant t0 = Instant.now();
      socket.connect(new InetSocketAddress(host, port), timeoutMs);
      Instant t1 = Instant.now();
      r.latencyMs = Duration.between(t0, t1).toMillis();
      r.ok = true;
      r.message = "Connected";
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
    System.out.println("check=tcp target=" + r.target);
    System.out.println("ok=" + r.ok);
    if (r.latencyMs != null) System.out.println("connectLatencyMs=" + r.latencyMs);
    if (r.message != null) System.out.println("message=" + r.message);
  }
}

