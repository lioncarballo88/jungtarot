# JungTarot Quick Launch Script
# This script starts the emulator and installs the app

param(
    [switch]$help,
    [switch]$emulatorOnly,
    [switch]$installOnly,
    [switch]$logs
)

if ($help) {
    Write-Host @"
JungTarot Launch Script

Usage: .\run-app.ps1 [options]

Options:
  -help          Show this help message
  -emulatorOnly  Only start the emulator (don't install app)
  -installOnly   Only install the app (assumes emulator is running)
  -logs          Show app logs after launching

Examples:
  .\run-app.ps1                  # Start emulator and install app
  .\run-app.ps1 -emulatorOnly    # Just start emulator
  .\run-app.ps1 -installOnly     # Just install app
  .\run-app.ps1 -logs            # Install and show logs
"@
    exit 0
}

$sdkPath = "$env:LOCALAPPDATA\Android\Sdk"
$emulatorExe = "$sdkPath\emulator\emulator.exe"
$adbExe = "$sdkPath\platform-tools\adb.exe"

Write-Host "🎯 JungTarot App Launcher" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan

# Check if SDK exists
if (-not (Test-Path $sdkPath)) {
    Write-Host "❌ Android SDK not found at $sdkPath" -ForegroundColor Red
    exit 1
}

if (-not $installOnly) {
    Write-Host "`n🚀 Starting Emulator..." -ForegroundColor Yellow
    
    # Set Android SDK root
    $env:ANDROID_SDK_ROOT = $sdkPath
    
    # Start emulator
    & $emulatorExe -avd Pixel5_Android14 -no-audio -no-boot-anim -gpu swiftshader_indirect | Out-Null &
    
    Write-Host "⏳ Waiting for emulator to boot (this can take 1-2 minutes)..." -ForegroundColor Yellow
    Start-Sleep -Seconds 5
    
    $bootWait = 0
    while ($bootWait -lt 120) {
        $devices = & $adbExe devices 2>$null | Select-String "emulator"
        if ($devices) {
            Write-Host "✅ Emulator is ready!" -ForegroundColor Green
            break
        }
        Write-Host "   Waiting... ($bootWait/120s)" -ForegroundColor Gray
        Start-Sleep -Seconds 5
        $bootWait += 5
    }
    
    if ($bootWait -ge 120) {
        Write-Host "⚠️  Emulator may still be loading. Continuing anyway..." -ForegroundColor Yellow
    }
}

if (-not $emulatorOnly) {
    Write-Host "`n📦 Installing JungTarot App..." -ForegroundColor Yellow
    
    # Navigate to project directory
    $projectPath = (Split-Path $script:MyInvocation.MyCommand.Path -Parent)
    if (-not (Test-Path "$projectPath\gradlew.bat")) {
        $projectPath = "c:\Users\yo\Desktop\dev\new-tarot\jungtarot"
    }
    
    cd $projectPath
    
    # Install app
    Write-Host "   Running: gradlew installDebug" -ForegroundColor Gray
    .\gradlew installDebug 2>&1 | Select-String -Pattern "installed|failed|error" | ForEach-Object { Write-Host "   $_" }
    
    Write-Host "`n🎮 Launching JungTarot..." -ForegroundColor Yellow
    & $adbExe shell am start -n com.example.tarot/.MainActivity | Out-Null
    
    Start-Sleep -Seconds 3
    Write-Host "✅ App launched!" -ForegroundColor Green
    
    if ($logs) {
        Write-Host "`n📋 Showing app logs (Ctrl+C to stop)..." -ForegroundColor Yellow
        & $adbExe logcat | Select-String -Pattern "tarot|JungTarot|MainActivity"
    }
}

Write-Host "`n✨ Done! The JungTarot app should now be running on your emulator." -ForegroundColor Cyan
