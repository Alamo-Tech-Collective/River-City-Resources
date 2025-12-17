#!/usr/bin/env bash

docker stop mysql
docker rm mysql
sudo rm -rf data/*

docker run -p127.0.0.1:3306:3306 --name mysql -e MYSQL_ROOT_PASSWORD='LocalDevelopment@2025' -e MYSQL_ROOT_HOST='%' -e MYSQL_USER='rcr' -e MYSQL_DATABASE='rivercityresources' -e MYSQL_PASSWORD='LocalDevelopment@2025' -v $(pwd)/custom_config:/etc/mysql/conf.d -v $(pwd)/data:/var/lib/mysql -v $(pwd)/docker-entrypoint-initdb.d:/docker-entrypoint-initdb.d  -d --restart=unless-stopped mysql:8 --mysql-native-password=ON
