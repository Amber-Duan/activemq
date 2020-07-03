package com.novice.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsProduce {
    public static final String ACTIVEMQ_URL ="tcp://47.106.11.96:61616";
    public static final String QUEUE_NAME ="queue01";  // 1对1 的队列
    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂,给定url
        ActiveMQConnectionFactory activeMQConnectionFactory=new ActiveMQConnectionFactory(ACTIVEMQ_URL);
                
        // 2.通过工厂,获得connection并启动
        Connection connection=activeMQConnectionFactory.createConnection();
        connection.start();
        // 3.创建会话session
        //##有2个参数(事务,签收)先不要过多关注
        Session session=connection.createSession(false,Session.AUTO_ACKNOWLEDGE);
        // 4.创建destination-->queue
        Queue queue=session.createQueue(QUEUE_NAME); //类似 Collection collection=Collection.newArrayList();
        // 5.创建msgProduce
        MessageProducer messageProducer=session.createProducer(queue);
        //6.通过msgProduce生产3条message发送到queue里面(for循环)
        for (int i=1;i<=3;i++) {
            // session创建了message
            TextMessage textMessage=session.createTextMessage("message"+i) ;
            //通过messageProducer发送给mq
            messageProducer.send(textMessage);
        }
        //7.倒序关闭资源
        messageProducer.close();
        session.close();
        connection.close();

        System.out.printf("******消息发布到MQ完成");
    }
}
