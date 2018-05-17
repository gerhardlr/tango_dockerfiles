#!/bin/sh

if [ "$#" -ne 3 ]; then
  echo "usage: $0 <exec>/<inst> <class> <device>"
  exit 1
fi

tango_admin --check-device $3 || \
tango_admin --add-server $1 $2 $3 || \
exit 1
