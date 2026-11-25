# This Source Code Form is subject to the terms of the Mozilla Public License,
# v. 2.0. If a copy of the MPL was not distributed with this file, You can
# obtain one at https://www.bahmni.org/license/mplv2hd.
#
# Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
# graphic logo is a trademark of OpenMRS Inc.

#!/bin/sh -x -e

PATH_OF_CURRENT_SCRIPT="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
source $PATH_OF_CURRENT_SCRIPT/vagrant/vagrant_functions.sh
USER=bahmni

run_in_vagrant -c "sudo rm -rf /opt/bahmni-erp-connect/bahmni-erp-connect"
run_in_vagrant -c "sudo ln -s /bahmni/openerp-atomfeed-service/openerp-atomfeed-service/target/openerp-atomfeed-service /opt/bahmni-erp-connect/bahmni-erp-connect"
run_in_vagrant -c "sudo chown -h ${USER}:${USER} /opt/bahmni-erp-connect/bahmni-erp-connect"