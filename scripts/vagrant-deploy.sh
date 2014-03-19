#!/bin/bash

#All config is here
TEMP_ERP_FEED_SERVICE_WAR_FOLDER=/tmp/deploy_erp_feed_service
MACHINE_IP=192.168.33.10
SCRIPTS_DIR=../scripts
KEY_FILE=~/.vagrant.d/insecure_private_key
ERP_FEED_SERVICE_WAR=./target/openerp-atomfeed-service.war

if [[ ! -a $ERP_FEED_SERVICE_WAR ]]; then
    echo "----------------------------------------"
    echo "$ERP_FEED_SERVICE_WAR does not exist!"
    echo "----------------------------------------"
    exit -1
fi

# Setup environment
ssh vagrant@$MACHINE_IP -i $KEY_FILE < $SCRIPTS_DIR/setup_environment.sh

# Kill tomcat
ssh vagrant@$MACHINE_IP -i $KEY_FILE < $SCRIPTS_DIR/tomcat_stop.sh

# Copy ERP Atom Feed Service App War file to Vagrant tmp
scp  -i $KEY_FILE $ERP_FEED_SERVICE_WAR vagrant@$MACHINE_IP:$TEMP_ERP_FEED_SERVICE_WAR_FOLDER


#Deploy them from Vagrant /tmp to appropriate location
ssh vagrant@$MACHINE_IP -i $KEY_FILE < $SCRIPTS_DIR/deploy_erp_feed_service_war.sh

# Restart tomcat
ssh vagrant@$MACHINE_IP -i $KEY_FILE < $SCRIPTS_DIR/tomcat_start.sh
