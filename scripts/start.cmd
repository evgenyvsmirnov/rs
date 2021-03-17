@ECHO OFF
SETLOCAL
set SRC="examples/space.rs"

if [%1] neq [] (
     set SRC=%1
)

java -Dfile.encoding=UTF-8 -p lib/rsvm-0.9-SNAPSHOT.jar;lib/antlr4-runtime-4.9.1.jar -m rs.rsvm/rs.RsVmLauncher %SRC%

if %ERRORLEVEL% neq 0 (
    if %ERROR_CODE% neq 0 (
        set ERROR_CODE=ERRORLEVEL
    )
)
cmd /C exit /B %ERROR_CODE%