#!/bin/bash
cd /home/ec2-user
aws s3 cp s3://saplabs-20231208/video-game-db-0.0.1-SNAPSHOT.jar .
java -jar video-game-db-0.0.1-SNAPSHOT.jar