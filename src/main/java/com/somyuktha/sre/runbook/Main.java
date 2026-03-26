package com.somyuktha.sre.runbook;

import picocli.CommandLine;

public final class Main {
  public static void main(String[] args) {
    int exitCode = new CommandLine(new RunbookCli()).execute(args);
    System.exit(exitCode);
  }
}

