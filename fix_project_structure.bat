@echo off
REM Script para corrigir a configuração do projeto Java no VSCode

REM Cria o arquivo .classpath na pasta atual

echo ^<?xml version="1.0" encoding="UTF-8"?^> > .classpath
echo ^<classpath^> >> .classpath
echo ^<classpathentry kind="src" path="src/main/java"/^> >> .classpath
echo ^<classpathentry kind="src" path="src/test/java"/^> >> .classpath
echo ^<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER"/^> >> .classpath
echo ^<classpathentry kind="con" path="org.eclipse.m2e.MAVEN2_CLASSPATH_CONTAINER"/^> >> .classpath
echo ^<classpathentry kind="output" path="target/classes"/^> >> .classpath
echo ^</classpath^> >> .classpath

echo Arquivo .classpath criado na pasta atual.
echo Por favor, recarregue o projeto no VSCode para aplicar as mudanças.
pause
