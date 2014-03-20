#!/bin/sh -x

TEMP_LOCATION=/tmp/deploy_erp_feed_service

if [[ ! -d $TEMP_LOCATION ]]; then
   mkdir $TEMP_LOCATION
fi

rm -rf $TEMP_LOCATION/*