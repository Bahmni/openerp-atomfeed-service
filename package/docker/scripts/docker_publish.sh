#!/bin/bash
set -e
ODOO_CONNECT_IMAGE_TAG=${BAHMNI_VERSION}-${GITHUB_RUN_NUMBER}
echo ${DOCKER_HUB_TOKEN} | docker login -u ${DOCKER_HUB_USERNAME} --password-stdin
echo "[INFO] Pushing build image"
docker push bahmni/odoo-connect:${ODOO_CONNECT_IMAGE_TAG}

echo "[INFO] Pushing latest image"
docker push bahmni/odoo-connect:latest

docker logout
