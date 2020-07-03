package com.novice.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_Topic {

    public static final String ACTIVEMQ_URL ="tcp://47.106.11.96:61616";
    public static final String TOPIC_NAME ="Topic-Amber";  // 1对多 的队列

    public static void main(String[] args) throws JMSException, IOException {
       /* 多次启动同一个main方法设置
          https://blog.csdn.net/qq_41463655/article/details/100186587*/

        System.out.println("我是消费者2");
        //1.创建连接工厂,给定url
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);

        // 2.通过工厂,获得connection并启动
        Connection connection=activeMQConnectionFactory.createConnection();
        connection.start();
        // 3.创建会话session
        //##有2个参数(事务,签收)先不要过多关注
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        // 4.创建destination-->topic
        Topic topic=session.createTopic(TOPIC_NAME);
        // 5.创建msgConsumer
        MessageConsumer messageConsumer=session.createConsumer(topic);
        //lambada表达式 java8新特性
        messageConsumer.setMessageListener((message) -> {
            if (null != message && message instanceof TextMessage){
                //强转类型,记得和msgProducer放入的信息类型对应
                TextMessage textMessage=(TextMessage)message;
                try {
                    //7.读取信息
                    System.out.println("消费者接收到的Topic消息---->"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //wins通过TCP连接linux,进行一系列操作
        //避免控制台关的太快
        //在解析前##注意保持控制台不灭 即system.in.read();
        System.in.read();

        //8.倒序关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
