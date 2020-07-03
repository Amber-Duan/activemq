package com.novice.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class JmsConsumer {
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
        // 5.创建msgConsumer
        /**
         * 同步阻塞方式receive()方法来接受信息,
         * receive()方法再能够接受信息之前(或者超时之前)将一直阻塞
         */
        MessageConsumer messageConsumer=session.createConsumer(queue);
        //6.读取信息(while循环)
        while (true){
            //强转类型,记得和msgProducer放入的信息类型对应
            TextMessage textMessage=(TextMessage)messageConsumer.receive();
            //TextMessage textMessage=(TextMessage)messageConsumer.receive(4000L);
            if (null != textMessage){
                System.out.println("消费者的信息 --->"+textMessage.getText());
            }else{
                break;
            }
        }
        //7.倒序关闭资源
        messageConsumer.close();
        session.close();
        connection.close();
    }
}
