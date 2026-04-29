#!/bin/bash

# 环境变量
export APP_NAME=mailman
export APP_PORT=8080
export APP_VERSION=1.0.0
export APP_ENV=dev

JAR_FILE="target/*.jar"
PID_FILE="${APP_NAME}.pid"
LOG_FILE="${APP_NAME}.log"

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

echo -e "${GREEN}========================================${NC}"
echo -e "${GREEN}开始部署 ${APP_NAME} ${APP_VERSION}${NC}"
echo -e "${GREEN}========================================${NC}"

# 1. Maven 打包
echo -e "${YELLOW}[1/4] 开始 Maven 打包...${NC}"
mvn clean install -Dmaven.test.skip=true -Dmaven.resources.overwrite=true
if [ $? -ne 0 ]; then
    echo -e "${RED}Maven 打包失败，退出脚本${NC}"
    exit 1
fi
echo -e "${GREEN}✓ Maven 打包完成${NC}"

# 2. 检查并停止正在运行的进程
echo -e "${YELLOW}[2/4] 检查并停止正在运行的进程...${NC}"
if [ -f "$PID_FILE" ]; then
    OLD_PID=$(cat "$PID_FILE")
    if ps -p $OLD_PID > /dev/null 2>&1; then
        echo -e "${YELLOW}检测到进程 (PID: $OLD_PID) 正在运行，正在停止...${NC}"
        kill $OLD_PID
        sleep 3
        if ps -p $OLD_PID > /dev/null 2>&1; then
            echo -e "${YELLOW}正常停止失败，强制终止进程...${NC}"
            kill -9 $OLD_PID
        fi
        rm -f "$PID_FILE"
        echo -e "${GREEN}✓ 已停止旧进程${NC}"
    else
        echo -e "${YELLOW}PID 文件存在但进程未运行${NC}"
        rm -f "$PID_FILE"
    fi
else
    # 尝试通过进程名查找
    PID=$(ps aux | grep "${JAR_FILE}" | grep -v grep | awk '{print $2}')
    if [ ! -z "$PID" ]; then
        echo -e "${YELLOW}检测到进程 (PID: $PID) 正在运行，正在停止...${NC}"
        kill $PID
        sleep 3
        if ps -p $PID > /dev/null 2>&1; then
            kill -9 $PID
        fi
        echo -e "${GREEN}✓ 已停止旧进程${NC}"
    else
        echo -e "${GREEN}✓ 没有正在运行的进程${NC}"
    fi
fi

# 3. 启动新项目
echo -e "${YELLOW}[3/4] 启动新项目...${NC}"
echo -e "${GREEN}✓ 使用环境: ${APP_ENV}${NC}"
echo -e "${YELLOW}========================================${NC}"
echo -e "${YELLOW}服务启动中，日志将实时显示在下方${NC}"
echo -e "${YELLOW}按 Ctrl+C 可停止服务${NC}"
echo -e "${YELLOW}========================================${NC}"
echo ""

# 前台启动，日志直接输出到控制台
java -Dspring.profiles.active=${APP_ENV} -jar ${JAR_FILE}
EXIT_CODE=$?

# 检查退出码
if [ $EXIT_CODE -eq 0 ]; then
    echo -e "${GREEN}========================================${NC}"
    echo -e "${GREEN}✓ ${APP_NAME} 已正常停止${NC}"
    echo -e "${GREEN}========================================${NC}"
else
    echo -e "${RED}========================================${NC}"
    echo -e "${RED}✗ ${APP_NAME} 异常退出 (退出码: ${EXIT_CODE})${NC}"
    echo -e "${RED}========================================${NC}"
    exit $EXIT_CODE
fi