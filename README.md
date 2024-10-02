### About 

This is a very simple Spring Boot application to try out different libraries and technologies. The very first take is to create end-point ``/generate`` which triggers sending Kafka event to the listener. The event is a simple entity containing dummy flight number and flight status (for example ``FW8020 "WAIT FOR CHECK-IN"`` or `` 6D5714 "GO TO GATE"``). For the sake of simplicity Kafka producer and consumer are located in the same service. 

### Setup

For Windows

* Install Docker Desktop


### Useful commands Kafka CLI commands

Open interactive terminal ``docker exec -it kafka bash`` (where `kafka` is a container name from [docker-compose.yml](docker-compose.yml))

Create topic ``kafka-topics --bootstrap-server kafka:19092 --create --topic flights-topic --replication-factor 1 --partitions 1`` (where `flights-topic` is a topic name which should be specified in [application.properties](src/main/resources/application.properties))

Consume events ``docker exec --interactive --tty kafka kafka-console-consumer --bootstrap-server kafka:19092 --topic flights-topic --formatter kafka.tools.DefaultMessageFormatter --property print.timestamp=true --property print.key=true --property print.value=true --from-beginning``

### Tips
When using Git-bash on windows prefix commands with ``winpty`` (for more info read: [Some native console programs don't work when run from Git Bash](https://github.com/git-for-windows/git/wiki/FAQ#some-native-console-programs-dont-work-when-run-from-git-bash-how-to-fix-it)) 

When running java app locally (not in container), use ``local`` Spring profile by adding ``-Dspring.profiles.active=local`` to VM options 