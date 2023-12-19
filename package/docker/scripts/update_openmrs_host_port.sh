#!/bin/bash
set +e

run_sql() {
  PGPASSWORD="${ODOO_DB_PASSWORD}" psql --host="${ODOO_DB_SERVER}" -U "${ODOO_DB_USERNAME}" -d odoo -t -c "$1"
}

if [ $(run_sql "select count(*) from information_schema.tables where table_name='markers' and table_schema='public';") -gt 0 ]
then
    echo "Updating OpenMRS Host Port in markers and failed_events table"
    run_sql "UPDATE markers SET feed_uri_for_last_read_entry = regexp_replace(feed_uri_for_last_read_entry, 'http://.*/openmrs', 'http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs'),feed_uri = regexp_replace(feed_uri, 'http://.*/openmrs', 'http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs') where feed_uri ~ 'openmrs';"
    run_sql "UPDATE failed_events SET feed_uri = regexp_replace(feed_uri, 'http://.*/openmrs', 'http://${OPENMRS_HOST}:${OPENMRS_PORT}/openmrs') where feed_uri ~'openmrs';"

    echo "Updating OpenELIS Host Port in markers and failed_events table"
    run_sql "UPDATE markers SET feed_uri_for_last_read_entry = regexp_replace(feed_uri_for_last_read_entry, 'http://.*/openelis', 'http://${OPENELIS_HOST}:${OPENELIS_PORT}/openelis'),feed_uri = regexp_replace(feed_uri, 'http://.*/openelis', 'http://${OPENELIS_HOST}:${OPENELIS_PORT}/openelis') where feed_uri ~ 'openelis';"
    run_sql "UPDATE failed_events SET feed_uri = regexp_replace(feed_uri, 'http://.*/openelis', 'http://${OPENELIS_HOST}:${OPENELIS_PORT}/openelis') where feed_uri ~'openelis';"
fi
