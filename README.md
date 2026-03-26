# runbook-automation-cli

Small Java CLI to automate common on-call runbook checks and generate a handover-friendly report.

## Features

- HTTP check: GET a URL with timeout; capture status + latency
- TCP check: connect to host:port; capture connect latency
- DNS lookup: resolve A/AAAA; capture results
- Output: human-readable summary and optional JSON report file

## Requirements

- Java 17+
- Maven

## Build

```bash
mvn -q test
mvn -q package
```

This produces a fat jar at `target/runbook-automation-cli-0.1.0.jar`.

## Usage

### HTTP

```bash
java -jar target/runbook-automation-cli-0.1.0.jar http --url "https://example.com" --timeout-ms 3000
```

### TCP

```bash
java -jar target/runbook-automation-cli-0.1.0.jar tcp --host "example.com" --port 443 --timeout-ms 2000
```

### DNS

```bash
java -jar target/runbook-automation-cli-0.1.0.jar dns --host "example.com"
```

### JSON report

```bash
java -jar target/runbook-automation-cli-0.1.0.jar http --url "https://example.com" --json-out report.json
```

## Why this is useful (SRE/Production Support)

This tool helps standardize basic checks during incidents and shift handover:
connectivity, endpoint health, and name resolution — with timestamps and latency.

