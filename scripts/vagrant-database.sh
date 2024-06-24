#!/bin/sh -x
PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $PATH_OF_CURRENT_SCRIPT/../openerp-atomfeed-service/scripts/vagrant/vagrant_functions.sh
export USER=bahmni

run_in_vagrant -c "sudo rm -f /packages/build/openerp-atomfeed-service.war"
run_in_vagrant -c "sudo chown ${USER}:${USER} /packages/build"
run_in_vagrant -c "sudo su - ${USER} -c 'cp /bahmni/openerp-atomfeed-service/openerp-atomfeed-service/target/openerp-atomfeed-service.war /packages/build/'"