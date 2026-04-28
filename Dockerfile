FROM centos:7.9.2009

MAINTAINER 2024-05-13 sunkolin sunkolin@qq.com

# config
RUN cp -f /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone &&\
    alias ll="ls -al"

# software
RUN yum install -y wget gcc net-tools telnet.x86_64 xinetd.x86_64 &&\
    yum clean all

# java
RUN mkdir -p /opt/ &&\
    cd /opt/ &&\
    curl -L -O  -J -H "Cookie: key=value" -k "https://repo.huaweicloud.com/java/jdk/8u202-b08/jdk-8u202-linux-x64.tar.gz" &&\
    tar -zxvf jdk-8u202-linux-x64.tar.gz > /dev/null &&\
    rm -rf /opt/jdk-8u202-linux-x64.tar.gz &&\
    ln -s /opt/jdk1.8.0_202 /opt/java
ENV JAVA_HOME=/opt/java
ENV PATH=${PATH}:/opt/java/bin
RUN java -version

# arthas
RUN mkdir -p /opt/arthas/ &&\
    cd /opt/arthas/ &&\
    wget https://alibaba.github.io/arthas/arthas-boot.jar

# application
WORKDIR /opt/mailman
ADD ./target/mailman-1.0.0-SNAPSHOT.jar /opt/mailman

# start
CMD java -jar -Xmx1G -Xmx2G -server -XX:+UseConcMarkSweepGC -XX:+UseCMSCompactAtFullCollection -XX:+PrintGCDetails -XX:+PrintGCDateStamps -XX:+PrintGCTimeStamps -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/logs/mailman/ -Xloggc:~/opt/log/mailman-gc.log /opt/mailman/mailman-1.0.0-SNAPSHOT.jar
