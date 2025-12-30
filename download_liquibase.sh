#!/bin/sh
mkdir -p src/main/resources/db/liquibase
cd src/main/resources/db/liquibase


wget https://github.com/liquibase/liquibase/releases/download/v4.25.1/liquibase-4.25.1.tar.gz

wget -O mysql-connector-j-9.4.0.jar https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/9.4.0/mysql-connector-j-9.4.0.jar

tar -xzf liquibase-4.25.1.tar.gz
mkdir -p lib
mv mysql-connector-j-9.4.0.jar lib/

rm -f liquibase-4.25.1.tar.gz