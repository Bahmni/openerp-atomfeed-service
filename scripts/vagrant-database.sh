#!/bin/sh -x
PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $PATH_OF_CURRENT_SCRIPT/../openerp-atomfeed-service/scripts/vagrant/vagrant_functions.sh

run_in_vagrant -c "sudo rm -f /packages/build/openerp-atomfeed-service.war"
run_in_vagrant -c "sudo chown jss:jss /packages/build"
run_in_vagrant -c "sudo su - jss -c 'cp /Project/openerp-atomfeed-service/openerp-atomfeed-service/target/openerp-atomfeed-service.war /packages/build/'"
run_in_vagrant -c "sudo su - jss -c 'cd /bahmni_temp/bahmni-openerp && ./run-liquibase.sh'"

