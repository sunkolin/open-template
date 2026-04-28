@echo off
setlocal enabledelayedexpansion

set APP_NAME=mailman
set APP_PORT=8080
set APP_VERSION=1.0.0
set APP_ENV=dev

set JAR_PATTERN=target\*.jar
set PID_FILE=%APP_NAME%.pid

echo ========================================
echo Starting: %APP_NAME% %APP_VERSION%
echo ========================================
echo.

echo [1/4] Building with Maven...
call mvn clean package -DskipTests -Dmaven.resources.overwrite=true
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Build failed with code %ERRORLEVEL%
    pause
    exit /b 1
)
echo.
echo [OK] Build completed
echo.

echo [2/4] Stopping old processes...
if exist "%PID_FILE%" (
    set /p OLD_PID=<"%PID_FILE%"
    tasklist /FI "PID eq !OLD_PID!" 2>NUL | findstr /C:"!OLD_PID!" >NUL
    if not errorlevel 1 (
        echo Stopping process !OLD_PID!...
        taskkill /PID !OLD_PID! /F >NUL 2>&1
        timeout /t 2 /nobreak >NUL
    )
    del /F /Q "%PID_FILE%" 2>NUL
    echo [OK] Process stopped
) else (
    echo [OK] No running process
)
echo.

echo [3/4] Finding JAR file...
for %%f in (%JAR_PATTERN%) do (
    set JAR_FILE=%%f
    goto :jar_found
)
echo [ERROR] JAR not found
pause
exit /b 1

:jar_found
echo [OK] Found: %JAR_FILE%
echo.

echo [4/4] Starting application...
echo Environment: %APP_ENV%
echo Port: %APP_PORT%
echo ========================================
echo Press Ctrl+C to stop
echo ========================================
echo.

java -Dspring.profiles.active=%APP_ENV% -jar "%JAR_FILE%"
set EXIT_CODE=%errorlevel%

echo.
if %EXIT_CODE% equ 0 (
    echo [OK] Stopped normally
) else (
    echo [ERROR] Exited with code: %EXIT_CODE%
)

pause
exit /b %EXIT_CODE%
