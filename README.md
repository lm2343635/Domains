# Customer [![Build Status](https://travis-ci.org/lm2343635/Customer.svg?branch=master)](https://travis-ci.org/lm2343635/Customer)
A customer management system by Java EE.
You can view this code and comiple it for study and research.

### Development

This system runs on Tomcat 8.x server.
To use the domain protection system, the **sshpass** command should be installed.
You can use the following commands to install **sshpass**.

- Installing sshpass on OS X

```shell
brew install https://raw.githubusercontent.com/kadwanev/bigboybrew/master/Library/Formula/sshpass.rb
```

- Installing sshpass on Ubuntu

```shell
apt install sshpass
```

- Installing sshpass on Cent OS

```shell
yum install sshpass
```

### Entity Design

![entity](https://raw.githubusercontent.com/lm2343635/Customer/master/doc/entity.png)

### Run with Docker

Build with docker.

```shell
docker build . --tag customer:latest
```

Create a database and import `the initdb/customer.sql` to the database.
Then, run with docker using the following script.

```shell
docker run \
	--name customer \
	--restart=always \
	-e DB_SERVER=host.docker.internal \
	-e DB_PORT=3306 \
	-e DB_NAME=customer \
	-e DB_USER=www \
	-e DB_PASSWORD= \
	-p 8080:8080 -d \
	customer
```

Check the logs.

```shell
docker logs customer --follow
```

### Copyright

Any commercial purposes without permission is prohibited.
Contact lm2343635@126.com for commercial purposes.
