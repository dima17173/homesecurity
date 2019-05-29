FROM java:8

MAINTAINER ivasenko.pg@gmail.com

ENV TZ=Europe/Kiev
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN apt-get update && apt-get install sshpass
COPY target/home-security-0.0.1-SNAPSHOT.jar .
COPY deploy.sh .
COPY host.sh .
EXPOSE 9990
ENTRYPOINT ["java","-jar","home-security-0.0.1-SNAPSHOT.jar"]



