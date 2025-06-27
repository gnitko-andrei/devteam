#!/bin/sh
if [ -f /run/secrets/mysql_env ]; then
  set -a
  . /run/secrets/mysql_env
  set +a
fi

exec java -cp app:app/lib/* by.teachmeskills.devteam.DevteamApplication