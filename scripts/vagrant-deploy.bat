@echo off 

REM !!!!IMPORTANT!!!!
REM Before using this script to deploy do the following
REM Add putty to your path
REM Use puttygen to generate win_insecure_private_key.ppk from your %USERPROFILE%\.vagrant.d\insecure_private_key that comes along with vagrant.
REM !!!End of IMPORTANT!!!

REM All config is here

set MACHINE_IP=192.168.33.10
set TEMP_ERP_FEED_SERVICE_WAR_FOLDER=/tmp/deploy_erp_feed_service
set SCRIPTS_DIR=..\scripts
set KEY_FILE=%USERPROFILE%\.vagrant.d\win_insecure_private_key.ppk
set ERP_FEED_SERVICE_WAR=.\target\openerp-atomfeed-service.war

if exist %KEY_FILE% (
    REM setup
    putty -ssh vagrant@%MACHINE_IP% -i %KEY_FILE% -m %SCRIPTS_DIR%/setup_environment.sh
    REM Kill tomcat
    putty -ssh vagrant@%MACHINE_IP% -i %KEY_FILE% -m %SCRIPTS_DIR%/tomcat_stop.sh
    REM Copy war to Vagrant tmp
    pscp  -i %KEY_FILE% %ERP_FEED_SERVICE_WAR% vagrant@%MACHINE_IP%:%TEMP_ERP_FEED_SERVICE_WAR_FOLDER%
    REM Copy ERP Atom Feed war to Tomcat from tmp
    putty -ssh vagrant@%MACHINE_IP% -i %KEY_FILE% -m %SCRIPTS_DIR%/deploy_erp_feed_service_war.sh
    REM Start tomcat
    putty -ssh vagrant@%MACHINE_IP% -i %KEY_FILE% -m %SCRIPTS_DIR%/tomcat_start.sh
) else (
    echo Use puttygen to generate win_insecure_private_key.ppk from your %USERPROFILE%\.vagrant.d\insecure_private_key that comes along with vagrant.
)

