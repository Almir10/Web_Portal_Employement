FROM ubuntu:latest
LABEL authors="DS User"

ENTRYPOINT ["top", "-b"]