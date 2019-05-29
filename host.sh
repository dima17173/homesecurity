#!/usr/bin/env bash

#сборная команда состоит из подключения из докер контейнера к удаленному хосту через ssh и запуск скрипта
sshpass -p "пароль машины" ssh -o StrictHostKeyChecking=no pavel@172.17.0.1 'bash -s' < deploy.sh

