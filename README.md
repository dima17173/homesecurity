1. Установить Java с помощью команд в терминале:
"sudo add-apt-repository ppa:webupd8team/java",
"sudo apt-get update -y",
"sudo apt-get install oracle-java8-installer"

2. Установить MySQL с помощью команд в терминале:
"sudo apt-get update",
"sudo apt-get install mysql-server"

3. Установить Maven с помощью команд в терминале:
"cd ~/Downloads/"
"wget http://apache.mirror.digitalpacific.com.au/maven/maven-3/3.3.9/binaries/apache-maven-3.3.9-bin.tar.gz",
"cd /opt/ && sudo tar -xzvf ~/Downloads/apache-maven-3.3.9-bin.tar.gz",
"sudo update-alternatives --install /usr/bin/mvn maven /opt/apache-maven-3.3.9/bin/mvn 1001"

4. В терминале прописать команду "mvn clean package"

5. Запустить в терминале команду "java -jar target/home-security-0.0.1-SHAPSHOT.jar"

Запуск через докер:

Установить докер на машину.

1. sudo docker pull impltechteam/home-security:1.2 - скачиваем докер образ бэкэнд.
2. sudo docker pull impltechteam/home-security-admin:1.2 - скачиваем докер образ админ.
3. docker run -p 9990:9990 --name home-security impltechteam/home-security:1.2 - запускаем приложение.
4. docker run --link home-security:backendserver -p 80:80 impltechteam/home-security-admin:1.2  -  запуск админки в другом терминале

Для автоматической замены образа бэкэнда после изменений.
В файле host.sh - добавить пароль своей машины.


Using actuator:
-------------------------------------------------------------------------------------------------
To use it run application and send GET request to any endpoint shown below. 
You can use command line or Postman - it's up to you.

Exposed actuator endpoints:
-------------------------------------------------------------------------------------------------
1. "/actuator" - shows avalaible endpoints.

   ***Request example:***
   curl http://localhost:8880/actuator
   
   ***Response example:***
   ```json
   {
    "_links": {
        "self": {
            "href": "http://localhost:8880/actuator",
            "templated": false
        },
        "health": {
            "href": "http://localhost:8880/actuator/health",
            "templated": false
        },
        "metrics": {
            "href": "http://localhost:8880/actuator/metrics",
            "templated": false
        },
        "metrics-requiredMetricName": {
            "href": "http://localhost:8880/actuator/metrics/{requiredMetricName}",
            "templated": true
        }
      }
   }
2. "/actuator/health" - returns application status and additional info. Status can be "UP" whether application works fine, and "DOWN" when it's not.

   ***Request example:***
   curl http://localhost:8880/actuator/health
   
   ***Response example:***
  ```json
   {
    "status": "UP",
    "details": {
        "diskSpace": {
            "status": "UP",
            "details": {
                "total": 79034327040,
                "free": 64867303424,
                "threshold": 10485760
            }
        },
        "db": {
            "status": "UP",
            "details": {
                "mysqlMain": {
                    "status": "UP",
                    "details": {
                        "database": "MySQL",
                        "hello": 1
                    }
                },
                "mysqlApp": {
                    "status": "UP",
                    "details": {
                        "database": "MySQL",
                        "hello": 1
                      }
                  }
              }
          }
      }
  }
```
3. "/actuator/metrics" - returns list with all metrics.

   ***Request example:***
   curl http://localhost:8880/actuator/metrics
   
   ***Response example:***
```json
 {
    "names": [
        "jvm.memory.max",
        "process.files.max",
        "jvm.gc.memory.promoted",
        "tomcat.cache.hit",
        "system.load.average.1m",
        "tomcat.cache.access",
        "jvm.memory.used",
        "jvm.gc.max.data.size",
        "jvm.gc.pause",
        "jvm.memory.committed",
        "system.cpu.count",
        "logback.events",
        "tomcat.global.sent",
        "jvm.buffer.memory.used",
        "tomcat.sessions.created",
        "jvm.threads.daemon",
        "system.cpu.usage",
        "jvm.gc.memory.allocated",
        "tomcat.global.request.max",
        "hikaricp.connections.idle",
        "hikaricp.connections.pending",
        "tomcat.global.request",
        "tomcat.sessions.expired",
        "hikaricp.connections",
        "jvm.threads.live",
        "jvm.threads.peak",
        "tomcat.global.received",
        "hikaricp.connections.active",
        "hikaricp.connections.creation",
        "process.uptime",
        "tomcat.sessions.rejected",
        "process.cpu.usage",
        "tomcat.threads.config.max",
        "jvm.classes.loaded",
        "jdbc.connections.max",
        "jdbc.connections.min",
        "hikaricp.connections.max",
        "hikaricp.connections.min",
        "jvm.classes.unloaded",
        "tomcat.global.error",
        "tomcat.sessions.active.current",
        "tomcat.sessions.alive.max",
        "jvm.gc.live.data.size",
        "tomcat.servlet.request.max",
        "hikaricp.connections.usage",
        "tomcat.threads.current",
        "jdbc.connections.active",
        "tomcat.servlet.request",
        "hikaricp.connections.timeout",
        "process.files.open",
        "jvm.buffer.count",
        "jvm.buffer.total.capacity",
        "tomcat.sessions.active.max",
        "hikaricp.connections.acquire",
        "tomcat.threads.busy",
        "process.start.time",
        "tomcat.servlet.error"
    ]
}
```
4. "/actuator/{concreteMetricName}" - returns information about concrete metric.

   ***Request example:***
   curl http://localhost:8880/actuator/metrics/jvm.memory.max
   
   ***Response example:***
```json   
{
    "name": "jvm.memory.max",
    "description": "The maximum amount of memory in bytes that can be used for memory management",
    "baseUnit": "bytes",
    "measurements": [
        {
            "statistic": "VALUE",
            "value": 5466226687
        }
    ],
    "availableTags": [
        {
            "tag": "area",
            "values": [
                "heap",
                "nonheap"
            ]
        },
        {
            "tag": "id",
            "values": [
                "Compressed Class Space",
                "PS Survivor Space",
                "PS Old Gen",
                "Metaspace",
                "PS Eden Space",
                "Code Cache"
            ]
        }
    ]
 }
```

Updating properties in Runtime
-------------------------------------------------------------------------------------------------
1. Run project
2. Open https://github.com, find repository that already contains application.properties file. Update desired properties in     application.property file.
3. Commit changes (press "COMMIT CHANGES" button)
4. Open command line.
5. Check that server lookup to your changes --> send GET request to http://localhost:9990/application/default. You'll get response as json. Find updated values and confirm changes.

  ***Request example:***
    curl http://localhost:9990/application/default

6. Update project with new values --> send empty POST request to http://localhost:8880/actuator/refresh -d {} -H "Content-Type:application/json"

  ***Request example:***
    curl http://localhost:8880/actuator/refresh -d {} -H "Content-Type:application/json"
  
Now properties are updated and new values applied.
