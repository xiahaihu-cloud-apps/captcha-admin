FROM java:8
VOLUME /tmp /data

ENV DATABASE_IP db
ENV DATABASE_PORT 3306
ENV REDIS_PORT 6379
ENV REDIS_HOST redis
ENV JAVA_OPTS ""

RUN mkdir /app
ADD captcha-admin-0.0.1-SNAPSHOT.jar /app/app.jar
ADD runboot.sh /app/
RUN bash -c 'touch /app/app.jar'
WORKDIR /app
RUN chmod a+x runboot.sh
EXPOSE 8080
CMD /app/runboot.sh