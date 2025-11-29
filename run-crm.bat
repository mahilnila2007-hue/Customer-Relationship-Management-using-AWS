@echo off
echo ===================================
echo    CRM Application Launcher
echo ===================================
echo.

cd /d "%~dp0"

REM Java location
set "JAVA_EXE=C:\Program Files\Eclipse Adoptium\jdk-25.0.0.36-hotspot\bin\java.exe"

if not exist "%JAVA_EXE%" (
    echo ERROR: Java not found at expected location!
    echo Please check Java installation.
    pause
    exit /b 1
)

echo Starting CRM Application...
echo.
echo Default Login: kaviya / kaviya123
echo.
"%JAVA_EXE%" -cp "target\classes;lib\sqlite-jdbc-3.42.0.0.jar" com.example.crm.ui.LoginFrame

if errorlevel 1 (
    echo.
    echo Application failed to start!
    pause
)
exit /b 0
