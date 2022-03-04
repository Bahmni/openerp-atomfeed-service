openerp-atomfeed-service
========================

[![Build and Publish](https://github.com/Bahmni/openerp-atomfeed-service/actions/workflows/build_publish.yml/badge.svg)](https://github.com/Bahmni/openerp-atomfeed-service/actions/workflows/build_publish.yml)


Atom feed client and server interface service for OpenERP

*Compile using:*
* `./script/vagrant-deploy.sh` (to build war file and deploy to Vagrant tomcat)
* `./script/vagrant-database.sh` (to run liquibase migrations on the vagrant machine on the dev box)

*Atom feed client (odoo-connect) docker image*

Docker images for [odoo-connect](https://hub.docker.com/r/bahmni/odoo-connect/tags) are built using [Github Actions](/.github/workflows). 

Resources to build the [bahmni/odoo-connect](https://hub.docker.com/r/bahmni/odoo-connect/tags) images can be found in the [package](/package) directory.

*To Build and Package, run:*

```
./mvnw clean package
```


