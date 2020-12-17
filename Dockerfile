FROM tomcat:latest
ADD target/music-platform-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/

RUN chmod +x "entrypoint.sh"
CMD /bin/bash "entrypoint.sh"

EXPOSE 8081
CMD ["catalina.sh", "run"]