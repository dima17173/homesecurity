#!/usr/bin/env bash
set -e
BASE_IMAGE="registry"
REGISTRY="registry.hub.docker.com"
IMAGE="impltechteam/home-security:1.2"
CID=$(docker ps | grep $IMAGE | awk '{print $1}')
docker pull $IMAGE

for im in $CID
do
    LATEST=`docker inspect --format "{{.Id}}" $IMAGE`
    RUNNING=`docker inspect --format "{{.Image}}" $im`
    NAME=`docker inspect --format "{{.Name}}" $im | sed "s/\///g"`
    echo "Latest:" $LATEST
    echo "Running:" $RUNNING
    if [[ "$RUNNING" != "$LATEST" ]];then
        echo "upgrading $NAME"
        nohup $(docker stop $NAME && docker rm $(docker ps -a -f status=exited -q) && docker rmi $(docker images -f dangling=true -q) && docker run -p 9990:9990 $IMAGE)
    else
        echo "$NAME up to date!"
    fi
done