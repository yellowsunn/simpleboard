#!/bin/bash

docker run --rm --name certbot -v '/home/yellowsunn/simpleforum/docker/volume/certbot/conf:/etc/letsencrypt' -v '/home/yellowsunn/simpleforum/docker/volume/cerbot/logs:/var/log/letsencrypt' -v '/home/yellowsunn/simpleforum/docker/volume/certbot/data:/var/www/certbot' certbot/certbot renew --server https://acme-v02.api.letsencrypt.org/directory --cert-name yellowsunn.com

