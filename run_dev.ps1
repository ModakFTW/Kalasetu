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

Write-Host "Starting Kalasetu with Java 17 and Maven..."
mvn spring-boot:run
