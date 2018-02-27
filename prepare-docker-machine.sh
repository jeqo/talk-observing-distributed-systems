#!/usr/bin/env bash
docker-machine ssh default sudo sysctl -w vm.max_map_count=262144
