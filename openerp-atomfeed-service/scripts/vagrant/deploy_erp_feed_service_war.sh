#!/bin/sh -x

TEMP_LOCATION=/tmp/deploy_erp_feed_service
WAR_LOCATION=/home/jss/apache-tomcat-8.0.12/webapps

sudo rm -rf $WAR_LOCATION/openerp-atomfeed-service
sudo su - jss -c "cp -f $TEMP_LOCATION/* $WAR_LOCATION"
