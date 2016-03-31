@echo off
mode con:cols=45 lines=20
title %1
java -jar HelloAPI.jar %1 %2
pause