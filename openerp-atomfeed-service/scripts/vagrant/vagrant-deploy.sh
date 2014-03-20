#!/bin/sh -x

PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $PATH_OF_CURRENT_SCRIPT/vagrant_functions.sh

#All config is here
TEMP_ERP_FEED_SERVICE_WAR_FOLDER=/tmp/deploy_erp_feed_service
SCRIPTS_DIR=scripts/vagrant
SHELL_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_BASE=$SHELL_DIR/../..
ERP_FEED_SERVICE_WAR=$PROJECT_BASE/target/openerp-atomfeed-service.war

if [[ ! -a $ERP_FEED_SERVICE_WAR ]]; then
    echo "----------------------------------------"
    echo "$ERP_FEED_SERVICE_WAR does not exist!"
    echo "----------------------------------------"
    exit -1
fi

# Setup environment
run_in_vagrant -f "$SCRIPTS_DIR/setup_environment.sh"

# Copy ERP Atom Feed Service App War file to Vagrant tmp
scp_to_vagrant $ERP_FEED_SERVICE_WAR $TEMP_ERP_FEED_SERVICE_WAR_FOLDER


#Deploy them from Vagrant /tmp to appropriate location
run_in_vagrant -f "$SCRIPTS_DIR/deploy_erp_feed_service_war.sh"

