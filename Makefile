local-run:
	mvn clean spring-boot:run

start:
	docker-compose up

stop:
	docker-compose down

test:
	mvn clean test

sonar:
	mvn clean test verify sonar:sonar

report:
	mvn clean package