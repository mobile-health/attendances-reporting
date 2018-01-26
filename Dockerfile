FROM jetty:jre8
MAINTAINER Jason Lu <jason.lu@manadr.com>
ADD target/attendances.reporting.war /var/lib/jetty/webapps/ROOT.war
EXPOSE 8080