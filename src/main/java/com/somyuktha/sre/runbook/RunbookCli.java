package com.somyuktha.sre.runbook;

import picocli.CommandLine.Command;

@Command(
    name = "runbook",
    mixinStandardHelpOptions = true,
    version = "runbook-automation-cli 0.1.0",
    description = "Automate basic on-call runbook checks (HTTP/TCP/DNS) and generate reports.",
    subcommands = {
        HttpCheckCommand.class,
        TcpCheckCommand.class,
        DnsCheckCommand.class
    })
public final class RunbookCli implements Runnable {
  @Override
  public void run() {
    // If no subcommand, show usage.
    throw new picocli.CommandLine.ParameterException(
        new picocli.CommandLine(this), "Missing subcommand. Use -h for help.");
  }
}

