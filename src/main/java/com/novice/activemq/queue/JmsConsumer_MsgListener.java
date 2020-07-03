package com.novice.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

public class JmsConsumer_MsgListener {
    public static final String ACTIVEMQ_URL ="tcp://47.106.11.96:61616";
    public static final String QUEUE_NAME ="queue01";  // 1对1 的队列
    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("我是消费者2");
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
        MessageConsumer messageConsumer=session.createConsumer(queue);
        //6.在消费者上添加监听器，实现其接口的方法
        /**
         * 哎哟喂是在##消费者messageConsumer###上添加监听器，原先在session上添加listener一直读不到值~
         * 你真SB!!
         */

        /**
         * 异步非阻塞方式setMessageListener()方法来接受信息,
         * 订阅者先注册一个消息监听器
         * 当消息到达后，系统自动调用监听器MessageListener的onMessage()方法
         */
        messageConsumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage){
                    //强转类型,记得和msgProducer放入的信息类型对应
                    TextMessage textMessage=(TextMessage)message;
                    try {
                        //7.读取信息
                        System.out.println("MessageListener 接收到的消息---->"+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
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
