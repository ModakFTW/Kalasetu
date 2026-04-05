$ErrorActionPreference = "Stop"

# Default to checking system Java
$javaValid = $false

# 1. Try to find global System Java
try {
    Write-Host "Checking for System Java..."
    $versionOutput = & java -version 2>&1
    if ($LASTEXITCODE -eq 0 -or $?) {
        Write-Host "System Java found."
        $javaValid = $true
    }
} catch {
    Write-Host "System Java not found."
}

# 2. Fallback for the original author (for 'atacc') who used the portable Assistant-installed Java
if (-not $javaValid) {
    $toolsDir = "C:\Users\atacc\.gemini\antigravity\brain\566d4e59-70ae-4bca-baaa-004ad4757b84"
    $javaPath = "$toolsDir\jdk-17.0.2"
    
    if (Test-Path $javaPath) {
        Write-Host "Local portable Java found. Configuring..."
        $env:JAVA_HOME = $javaPath
        $env:PATH = "$javaPath\bin;$env:PATH"
        $javaValid = $true
    }
}

if (-not $javaValid) {
    Write-Error "Java 17+ not found! Please ensure Java 17+ is installed globally on your machine and available in your system PATH to run Kalasetu."
    exit 1
}

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

Write-Host "Starting Kalasetu application using Maven Wrapper..."
# We use the local Maven wrapper (mvnw.cmd), which completely eliminates the need for Maven to be pre-installed!
if (Test-Path ".\mvnw.cmd") {
    .\mvnw.cmd clean spring-boot:run
} else {
    Write-Error "Maven wrapper (mvnw.cmd) not found. Please ensure the repository was fully cloned."
}