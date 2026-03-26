package com.somyuktha.sre.runbook;

import com.somyuktha.sre.runbook.model.CheckResult;
import com.somyuktha.sre.runbook.util.JsonIO;
import java.net.InetAddress;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "dns", mixinStandardHelpOptions = true, description = "DNS resolution check (A/AAAA).")
public final class DnsCheckCommand implements Runnable {
  @Option(names = "--host", required = true, description = "Hostname to resolve.")
  private String host;

  @Option(names = "--json-out", description = "Optional path to write JSON report.")
  private Path jsonOut;

  @Override
  public void run() {
    CheckResult r = CheckResult.start("dns", host);
    try {
      InetAddress[] addrs = InetAddress.getAllByName(host);
      List<String> ips = new ArrayList<>();
      for (InetAddress a : addrs) {
        ips.add(a.getHostAddress());
      }
      r.details.put("addresses", ips);
      r.ok = !ips.isEmpty();
      r.message = r.ok ? "Resolved" : "No addresses returned";
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
    System.out.println("check=dns target=" + r.target);
    System.out.println("ok=" + r.ok);
    Object addrs = r.details.get("addresses");
    if (addrs != null) System.out.println("addresses=" + addrs);
    if (r.message != null) System.out.println("message=" + r.message);
  }
}

