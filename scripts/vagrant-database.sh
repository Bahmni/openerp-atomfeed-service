# This Source Code Form is subject to the terms of the Mozilla Public License,
# v. 2.0. If a copy of the MPL was not distributed with this file, You can
# obtain one at https://www.bahmni.org/license/mplv2hd.
#
# Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
# graphic logo is a trademark of OpenMRS Inc.

#!/bin/sh -x
PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $PATH_OF_CURRENT_SCRIPT/../openerp-atomfeed-service/scripts/vagrant/vagrant_functions.sh
export USER=bahmni

run_in_vagrant -c "sudo rm -f /packages/build/openerp-atomfeed-service.war"
run_in_vagrant -c "sudo chown ${USER}:${USER} /packages/build"
run_in_vagrant -c "sudo su - ${USER} -c 'cp /bahmni/openerp-atomfeed-service/openerp-atomfeed-service/target/openerp-atomfeed-service.war /packages/build/'"