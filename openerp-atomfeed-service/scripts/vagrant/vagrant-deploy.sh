#!/bin/sh -x

#All config is here
TEMP_ERP_FEED_SERVICE_WAR_FOLDER=/tmp/deploy_erp_feed_service
MACHINE_IP=192.168.33.10
SCRIPTS_DIR=scripts/vagrant
SHELL_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_BASE=$SHELL_DIR/../..
KEY_FILE=~/.vagrant.d/insecure_private_key
ERP_FEED_SERVICE_WAR=$PROJECT_BASE/target/openerp-atomfeed-service.war
TIMEOUT="-o ConnectTimeout=5"


if [[ ! -a $ERP_FEED_SERVICE_WAR ]]; then
    echo "----------------------------------------"
    echo "$ERP_FEED_SERVICE_WAR does not exist!"
    echo "----------------------------------------"
    exit -1
fi

# Setup environment
ssh vagrant@$MACHINE_IP -i $KEY_FILE $TIMEOUT < $SCRIPTS_DIR/setup_environment.sh

# Copy ERP Atom Feed Service App War file to Vagrant tmp
scp  -i $KEY_FILE $TIMEOUT $ERP_FEED_SERVICE_WAR vagrant@$MACHINE_IP:$TEMP_ERP_FEED_SERVICE_WAR_FOLDER


#Deploy them from Vagrant /tmp to appropriate location
ssh vagrant@$MACHINE_IP -i $KEY_FILE $TIMEOUT < $SCRIPTS_DIR/deploy_erp_feed_service_war.sh

