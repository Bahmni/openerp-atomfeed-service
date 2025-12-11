# openerp-atomfeed-service

[![Build and Publish](https://github.com/Bahmni/openerp-atomfeed-service/actions/workflows/build_publish.yml/badge.svg)](https://github.com/Bahmni/openerp-atomfeed-service/actions/workflows/build_publish.yml)

The `openerp-atomfeed-service` is an interface service for OpenERP, enabling synchronization through Atom Feed events. This service is crucial for maintaining data consistency across various Bahmni components such as OpenMRS, OpenERP, and OpenELIS.

## Overview

Bahmni uses ATOM Feed synchronization to allow services to pull for changes occurring in Bahmni. The Bahmni EMR (OpenMRS) publishes various event feeds, like Patient, Encounter, Lab, Drug feeds, and other systems subscribe to these event feeds to be notified of changes. Based on the "notification", the downstream consuming systems make a REST API call to OpenMRS to pull relevant data. Typically, a sync service is written for each consumer, that listens to these events, constructs the relevant data packets, and then pushes to downstream systems (like ELIS, ERP, etc).

### Example Workflow

1. **Drug Creation in Bahmni**:
   - Administrators create drugs using OpenMRS or the "CSV Upload" feature on Bahmni's Admin module.
   - Once the drug is created, an event is published (in the Drug Feed).
   - The `openerp-atomfeed-service` contains batch jobs that are scheduled to consume these atom feed events.
   - The job then creates an drugs in Odoo.

### Underlying Atom Feed Libraries

Bahmni uses an Atom Feed-based framework created by the ThoughtWorks ICT4H team for data synchronization between OpenMRS and OpenERP. More information can be found here:

- [AtomFeed Library](http://github.com/ict4h/atomfeed)
- [SimpleFeed Library](https://github.com/ICT4H/simplefeed)

### Database Tables for Atom Feed Synchronization

The tables used behind the scenes in Atom Feed are:

- **event_records (PUBLISHER)**: This table holds the list of events which are to be published by Atom Feed for others to consume.
- **markers (CONSUMER)**: This table holds marker entries to indicate the records which have already been processed.
- **event_records_offset_marker (CONSUMER)**: This table holds cached records for faster event process by the consumer.
- **failed_events (CONSUMER)**: This table holds the list of events which failed and could not be consumed. They are retried later by a different event handler.

## Compiling the Project

To compile the project and deploy it to a Vagrant tomcat:

```shell
./script/vagrant-deploy.sh
```

To run Liquibase migrations on the Vagrant machine:

```shell
./script/vagrant-database.sh
```

## Building and Packaging the Docker Image

To build the project locally, ensure your machine has `Java 17` installed. Docker images for [odoo-connect](https://hub.docker.com/r/bahmni/odoo-connect/tags) are built using [GitHub Actions](/.github/workflows). Resources to build the [bahmni/odoo-connect](https://hub.docker.com/r/bahmni/odoo-connect/tags) images can be found in the [package](/package/docker/Dockerfile) directory.

To build and package the project locally, run:

```shell
./mvnw clean package
docker build -f package/docker/Dockerfile --no-cache -t bahmni/odoo-connect:<tag> .
```

For further information, you can watch the [YouTube video](https://www.youtube.com/watch?v=xyz) giving an overview of Atom Feed sync mechanism in Bahmni.

## Adding Custom Extensions

The service supports adding custom parameters to sale orders through an extension mechanism. This allows you to inject additional business logic without modifying the core codebase.

### How to Use Extensions

1. **Create your extension JAR**:
   - Implement the `SaleOrderParameterProvider` interface from the `odoo-connect-extensions` dependency
   - Create a Spring XML configuration file named `*-extensions.xml` that defines your extension beans
   - Package everything as a JAR file

2. **Extend the base Docker image**:
   - Create a Dockerfile that extends the base odoo-connect image
   - Copy your extension JAR into the container

Sample Dockerfile:
```dockerfile
FROM bahmni/odoo-connect:latest

COPY target/systemx-odooconnect-extension-*.jar ${WAR_DIRECTORY}/WEB-INF/lib/
```

Build your custom image:
```shell
docker build -t my-org/odoo-connect-custom:1.0 .
```

The service will automatically discover and load all extension beans during startup.

By keeping this README comprehensive, detailed, and user-friendly, we aim to make the onboarding process for new developers smoother and enhance the understanding of our synchronization processes.
