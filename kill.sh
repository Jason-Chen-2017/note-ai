#!/usr/bin/env bash
ps -ef|grep 'note-ai'|awk '{print $2}'| xargs kill -9