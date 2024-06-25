FROM amazoncorretto:17

ENV SERVER_PORT=8053
ENV BASE_DIR=/var/run/bahmni-erp-connect
ENV CONTEXT_PATH=/bahmni-erp-connect
ENV WAR_DIRECTORY=/var/run/bahmni-erp-connect/bahmni-erp-connect
ENV SERVER_OPTS="-Xms512m -Xmx1024m -XX:MetaspaceSize=256m -XX:MaxMetaspaceSize=512m"
ENV DEBUG_OPTS="-agentlib:jdwp=transport=dt_socket,address=8002,server=y,suspend=n"

# Used by envsubst command for replacing environment values at runtime
RUN yum install -y gettext nc postgresql

ADD https://raw.githubusercontent.com/eficode/wait-for/v2.2.3/wait-for wait-for.sh

COPY package/docker/bahmni-embedded-tomcat-10-1.0-SNAPSHOT.jar /opt/bahmni-erp-connect/lib/bahmni-erp-connect.jar
COPY openerp-atomfeed-service/target/openerp-atomfeed-service.war /etc/bahmni-erp-connect/openerp-atomfeed-service.war

RUN mkdir -p ${WAR_DIRECTORY}
RUN cd ${WAR_DIRECTORY} && jar xvf /etc/bahmni-erp-connect/openerp-atomfeed-service.war

COPY package/resources/logback.xml ${WAR_DIRECTORY}/WEB-INF/classes/
COPY package/docker/templates/erp-atomfeed.properties.template /opt/bahmni-erp-connect/etc/erp-atomfeed.properties.template
COPY package/docker/scripts/run-liquibase.sh /opt/bahmni-erp-connect/etc/run-liquibase.sh
RUN chmod +x /opt/bahmni-erp-connect/etc/run-liquibase.sh
COPY package/docker/scripts/update_openmrs_host_port.sh update_openmrs_host_port.sh
RUN chmod +x update_openmrs_host_port.sh
RUN chmod +x wait-for.sh

COPY package/docker/scripts/start.sh start.sh
RUN chmod +x start.sh
CMD [ "./start.sh" ]