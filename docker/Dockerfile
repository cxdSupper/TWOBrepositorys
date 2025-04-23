# 指定基础镜像
FROM openjdk:8-jdk-alpine

WORKDIR /app

# 复制文件到镜像中
COPY ./mytool.jar /tmp/mytool.jar

# 暴露端口
EXPOSE 18080

# 指定容器启动时运行的指令
ENTRYPOINT ["java", "-jar", "/tmp/mytool.jar"]
