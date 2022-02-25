#!/bin/bash
set -xe

cp core-1.0-SNAPSHOT.jar package/docker/bahmni-core.jar

cd package
#Building Docker images
ODOO_CONNECT_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
docker build -t bahmni/odoo-connect:${ODOO_CONNECT_IMAGE_TAG} -f docker/Dockerfile  . --no-cache
