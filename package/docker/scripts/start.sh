#!/bin/sh
set -e
export ODOO_DB_NAME=${ODOO_DB_NAME:-odoo}
echo "Waiting for ${ODOO_DB_SERVER}:5432 for 3600 seconds"
sh wait-for.sh --timeout=3600 ${ODOO_DB_SERVER}:5432
echo "[INFO] Substituting Environment Variables"
envsubst < /opt/bahmni-erp-connect/etc/erp-atomfeed.properties.template > ${WAR_DIRECTORY}/WEB-INF/classes/erp-atomfeed.properties
./update_openmrs_host_port.sh
echo "[INFO] Starting Application"
java -jar $SERVER_OPTS $DEBUG_OPTS /opt/bahmni-erp-connect/lib/bahmni-erp-connect.jar

