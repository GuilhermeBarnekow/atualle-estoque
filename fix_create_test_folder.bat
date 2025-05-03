@echo off
REM Script para criar a pasta src/test/java vazia no projeto AtualleEstoque

if not exist src\test\java (
    mkdir src\test\java
    echo Pasta src\test\java criada com sucesso.
) else (
    echo Pasta src\test\java já existe.
)

pause
