@echo off
title Project Guild - Android Emulator
echo ========================================================
echo   Launching Project Guild Android Emulator (Option 2)
echo ========================================================
echo.

set "EMULATOR_EXE=%LOCALAPPDATA%\Android\Sdk\emulator\emulator.exe"
set "ADB_EXE=%LOCALAPPDATA%\Android\Sdk\platform-tools\adb.exe"
set "APK_PATH=%~dp0app\build\outputs\apk\debug\app-debug.apk"

if not exist "%EMULATOR_EXE%" (
    echo [ERROR] Emulator not found at: "%EMULATOR_EXE%"
    pause
    exit /b 1
)

echo [1/3] Starting Google Pixel Virtual Device...
start "" "%EMULATOR_EXE%" -avd medium_phone

echo [2/3] Waiting for virtual device to finish booting...
"%ADB_EXE%" wait-for-device
:wait_boot
for /f "tokens=*" %%a in ('"%ADB_EXE%" shell getprop sys.boot_completed 2^>nul') do set BOOT_STATE=%%a
if "%BOOT_STATE%" neq "1" (
    timeout /t 2 /nobreak >nul
    goto wait_boot
)

echo [3/3] Installing latest Project Guild APK...
"%ADB_EXE%" install -r "%APK_PATH%"
"%ADB_EXE%" shell am start -n com.example.projectguild/.MainActivity

echo.
echo ========================================================
echo   Project Guild is now running in the emulator window!
echo ========================================================
timeout /t 4
