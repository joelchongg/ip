@ECHO OFF

REM create bin directory if it doesn't exist
if not exist ..\bin mkdir ..\bin

REM delete output from previous run
if exist ACTUAL.TXT del ACTUAL.TXT

REM delete data file from previous run
if exist data del data

REM compile the code into the bin folder
for /R ..\src\main\java %%f in (*.java) do (
    javac -d ..\bin -cp  ..\src\main\java -Xlint:none "%%f"
)

IF ERRORLEVEL 1 (
    echo ********** BUILD FAILURE **********
    exit /b 1
)
REM no error here, errorlevel == 0

REM run the program, feed commands from input.txt file and redirect the output to the ACTUAL.TXT
java -cp ..\bin chatterbox.ui.ChatterBox < input.txt > ACTUAL.TXT

REM compare the output to the expected output
FC /n ACTUAL.TXT EXPECTED.TXT
