
$ErrorActionPreference = "Stop"

# Paths to portable tools installed by the assistant
$toolsDir = "C:\Users\atacc\.gemini\antigravity\brain\566d4e59-70ae-4bca-baaa-004ad4757b84"
$javaPath = "$toolsDir\jdk-17.0.2"
$mavenPath = "$toolsDir\apache-maven-3.9.6"

# Check if tools exist
if (!(Test-Path $javaPath)) {
    Write-Error "JDK 17 not found at $javaPath. Please ensure the assistant installed it correctly."
}
if (!(Test-Path $mavenPath)) {
    Write-Error "Maven not found at $mavenPath. Please ensure the assistant installed it correctly."
}

# Set environment variables for this session
$env:JAVA_HOME = $javaPath
$env:PATH = "$mavenPath\bin;$env:JAVA_HOME\bin;$env:PATH"

Write-Host "Checking for existing server on port 8080..."
$processHoldingPort = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($processHoldingPort) {
    $pidToKill = $processHoldingPort.OwningProcess | Select-Object -Unique
    foreach ($p in $pidToKill) {
        if ($p -ne 0 -and $p -ne 4) {
            Write-Host "Port 8080 is blocked by process $p. Terminating it to allow restart..."
            Stop-Process -Id $p -Force -ErrorAction SilentlyContinue
        }
    }
    Start-Sleep -Seconds 2
}

Write-Host "Starting Kalasetu with Java 17 and Maven..."
mvn clean spring-boot:run