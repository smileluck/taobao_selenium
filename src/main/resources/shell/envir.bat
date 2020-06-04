@echo off
REM 检查JDK环境
pushd %~dp0
cd..
set bjava=0
set "bbd=%cd%"
java -version>nul 2>nul
if /i not %errorlevel% == 0 (
set bjava=1
goto ENDJAVA
) else GOTO CHECKJAVA

:CHECKJAVA
for /f "tokens=3" %%g in ('java -version 2^>^&1 ^| findstr /i "version"') do (
    set JAVAVER=%%g
)
set JAVAVER=%JAVAVER:"=%
for /f "delims=. tokens=1-3" %%v in ("%JAVAVER%") do (
    set CURRENTV=%%w
)

if %CURRENTV% LSS 8 (set bjava=1)

:ENDJAVA
if %bjava% equ 1 (
    setx JAVA_HOME /M "%bbd%\jdk1.8.0_201"
    setx Path /M "%%JAVA_HOME%%\bin;%PATH%"
)

pause
echo **********************************************
echo             jdk环境已配置好,请按任意键继续!
pause
