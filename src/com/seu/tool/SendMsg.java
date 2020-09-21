package com.seu.tool;

import com.server.BusinessThreadUtil;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SendMsg {

    private boolean isOk = true;
    private ConcurrentLinkedQueue<ISendMsg> MsgQue = new ConcurrentLinkedQueue<ISendMsg>();//使用线程安全的类

    public boolean addQue(ISendMsg iSendMsg) {
        try {
            if (MsgQue.size() > 5000) {
                MsgQue.poll();
            }
            MsgQue.offer(iSendMsg);
            if (isOk) {
                send();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void send() {
        isOk = false;
        BusinessThreadUtil.executor.submit(
                () -> {
                    while (MsgQue.size() > 0) {
                        try {
                            ISendMsg nowmsg = MsgQue.poll();
                            nowmsg.Send();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    isOk = true;
                }
        );
    }

    public interface ISendMsg {
        /**
         * @return 返回是否发送成功
         */
        boolean Send();
    }
}


