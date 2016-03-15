@echo off

if "%1"=="-gh" (
	echo ---Header Generating---
	if exist 'src/com/BomberGame/API/BomberGameBOTAPI.class' del 'src/com/BomberGame/API/BomberGameBOTAPI.class'
	echo Compiling native functions ...
	"javac" -classpath src/ -d src/ src/com/BomberGame/API/BomberGameBOTAPI.java
	echo Generating related header ...
	if exist "%HEADER%.h" del "%HEADER%.h"
	"javah" -classpath src/ -jni com.BomberGame.API.BomberGameBOTAPI
	move com_BomberGame_API_BomberGameBOTAPI.h VS2015/
	echo ---Finished---
)
if "%1"=="-jni" (
	echo ---JNI Generating---
	echo Compiling ...
	if exist 'src/com/BomberGame/API/BomberGameBOTAPI.class' del 'src/com/BomberGame/API/BomberGameBOTAPI.class'
	echo Compiling native functions ...
	"javac" -Xlint:unchecked -classpath JNI/ -d JNI/ src/com/BomberGame/API/BomberGameBOTAPI.java
	echo Generating jar...
	if exist BomberGameBOTAPI.jar del BomberGameBOTAPI.jar
	"jar" cvf BomberGameBOTAPI.jar -C JNI/. .
	echo ---Finished---	
)

goto end


:usage
echo --------------------------------------------------------
echo "Change your Dir to where the bat located!"
echo Usage : run.bat [-gh][-jni]
echo		-gh 	: generate header for native funcs.
echo		-jni : generate jar
echo --------------------------------------------------------

:end