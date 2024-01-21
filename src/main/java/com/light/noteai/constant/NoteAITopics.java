package com.light.noteai.constant;

import java.util.Arrays;
import java.util.HashSet;

public class NoteAITopics {


    public final static String[] topicsArray = {
            "软件系统架构黄金法则",
            "AI大模型应用开发实战代码案例详解",
            "ChatGPT开发实战代码案例详解",
            "ChatGPT与AIGC开发实战代码案例详解",
            "Spring Boot开发实战代码案例详解",
            "机器学习开发实战代码案例详解",
            "Python开发实战代码案例详解",
            "Python数据分析开发实战代码案例详解",
            "Go开发实战代码案例详解",
            "Java开发实战代码案例详解",
            "MyBatis开发实战代码案例详解",
            "PyTorch人工智能开发实战代码案例详解",
            "Zookeeper开发实战代码案例详解",
            "Redis开发实战代码案例详解",
            "ElasticSearch开发实战代码案例详解",
            "ClickHouse开发实战代码案例详解",
            "Spark开发实战代码案例详解",
            "MySQL开发实战代码案例详解",
            "HBase开发实战代码案例详解",
            "Docker开发实战代码案例详解",
            "NLP自然语言处理开发实战代码案例详解",
            "工作流引擎开发实战代码案例详解",
            "RPA开发实战代码案例详解",
            "UI自动化开发实战代码案例详解",
            "深度学习开发实战代码案例详解",
            "神经网络开发实战代码案例详解",
            "知识图谱开发实战代码案例详解",
            "NoSQL开发实战代码案例详解",
            "ROS机器人开发实战代码案例详解",
            "强化学习RL开发实战代码案例详解",
            "因果推断与机器学习开发实战代码案例详解",
            "平台治理开发实战代码案例详解",
            "智能数据应用开发实战代码案例详解",
            "实时Flink大数据分析平台开发实战代码案例详解",
            "机器学习公式详解开发实战代码案例详解",
            "ReactFlow开发实战代码案例详解",
            "推荐系统开发实战代码案例详解",
            "DMP数据平台开发实战代码案例详解",
            "CRM平台开发实战代码案例详解",
            "电商交易系统开发实战代码案例详解",
            "金融支付系统开发实战代码案例详解",
            "RPC分布式服务框架开发实战代码案例详解",
            "MQ消息队列开发实战代码案例详解",
            "分布式事务开发实战代码案例详解",
    };

    public static HashSet<String> getTopics() {
        HashSet<String> topics = new HashSet<>();
        // 将数组转换为 HashSet
        topics.addAll(Arrays.asList(topicsArray));
        return topics;
    }

}
