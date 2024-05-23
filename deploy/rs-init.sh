#!/bin/bash

mongo <<EOF
var config = {
    "_id": "${REPLICA_NAME}",
    "version": 1,
    "members": [
        {
            "_id": 1,
            "host": "${MONGODB1}:27017",
            "priority": 1
        },
        {
            "_id": 2,
            "host": "${MONGODB2}:27017",
            "priority": 2
        }
    ]
};
rs.initiate(config, { force: true });
rs.status();
EOF