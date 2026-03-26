package com.somyuktha.sre.runbook;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import org.junit.jupiter.api.Test;

public class DnsCheckCommandTest {
  @Test
  void resolvesLocalhost() throws Exception {
    InetAddress[] addrs = InetAddress.getAllByName("localhost");
    assertTrue(addrs.length > 0);
  }
}

