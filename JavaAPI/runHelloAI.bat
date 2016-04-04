@echo off
mode con:cols=45 lines=20
if %1 == nul java -jar HelloAPI.jar TEST01 01
if %1 == nul goto end
title %1
java -jar HelloAPI.jar %1 %2
end:
pause