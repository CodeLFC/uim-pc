package gaozhi.online.uim.im.entity;

import gaozhi.online.ubtb.core.net.UMsg;
import gaozhi.online.ubtb.core.util.ByteUtil;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO  通信消息
 * @date 2022/3/17 10:25
 */
public class IMMsg {

    private long fromId;
    private long toId;
    private int routeCount;
    private int msgType;
    //数据
    private byte[] data;
    private long time;


    public long getFromId() {
        return fromId;
    }

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public long getToId() {
        return toId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public int getRouteCount() {
        return routeCount;
    }

    public void setRouteCount(int routeCount) {
        this.routeCount = routeCount;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "IMMsg{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", routeCount=" + routeCount +
                ", msgType=" + msgType +
                ", data=" + Arrays.toString(data) +
                ", time=" + time +
                '}';
    }

    /**
     * IMMsg编解码器
     */
    public static final class Codec {
        //最长数据长度
        private static final int MAX_DATA_LEN = 572 - 42;
        private static int id = 0;

        public static synchronized int[] generateIDs(int size) {
            if (id > Integer.MAX_VALUE - size) {
                id = 0;
            }
            int[] ids = new int[2];
            ids[0] = id;
            id += size;
            ids[1] = id;
            return ids;
        }

        public static UMsgQueue encode(IMMsg msg) {
            long time = msg.getTime();
            msg.data = ByteUtil.byteMerge(ByteUtil.longToByte(time), msg.data);
            //数据长度
            int len = msg.data.length;
            //分片片数
            int size = len / MAX_DATA_LEN;
            if (len % MAX_DATA_LEN != 0) {
                size += 1;
            }
            UMsgQueue msgQueue = new UMsgQueue(size);
            //id号区间
            int[] ids = generateIDs(size);
            //起始号
            int startSerial = ids[0];
            // --- 校验码
            int validateCode =0;
            //第四参数
            long param4 = ByteUtil.intToLong(validateCode, startSerial);
            //构建对象
            int cursor = 0;
            for (int id = ids[0]; id < ids[1]; id++) {
                UMsg uMsg = new UMsg();
                byte[] data = new byte[Math.min(len - cursor, MAX_DATA_LEN)];
                System.arraycopy(msg.data, cursor, data, 0, data.length);
                uMsg.setFromId(msg.fromId);
                uMsg.setToId(msg.toId);
                uMsg.setMsgType(msg.msgType);
                uMsg.setRouteCount(msg.routeCount);
                uMsg.setParam4(param4);
                uMsg.setParam5(ByteUtil.intToLong(size, id));
                uMsg.setData(data);

                msgQueue.add(uMsg);

                cursor += data.length;
            }

            return msgQueue;
        }

        public static IMMsg decode(UMsgQueue uMsgQueue) {
            IMMsg msg = new IMMsg();
            UMsg peek = uMsgQueue.peek();
            msg.fromId = peek.getFromId();
            msg.toId = peek.getToId();
            msg.routeCount = peek.getRouteCount();
            msg.msgType = peek.getMsgType();
            byte[][] datas = new byte[uMsgQueue.size()][];
            int i = 0;
            while (uMsgQueue.size() > 0) {
                UMsg uMsg = uMsgQueue.poll();
                datas[i++] = uMsg.getData();
            }
            byte[] data = ByteUtil.byteMerge(datas);
            msg.time = ByteUtil.byteToLong(data);
            msg.data = ByteUtil.subByte(data, 8, data.length - 1);
            return msg;
        }

        public static int getSize(UMsg msg) {
            return ByteUtil.longHigh(msg.getParam5());
        }

        public static int getSerial(UMsg msg) {
            return ByteUtil.longLow(msg.getParam5());
        }

        public static int getStartSerial(UMsg msg) {
            return ByteUtil.longLow(msg.getParam4());
        }

        public static int getValidateCode(UMsg msg) {
            return ByteUtil.longHigh(msg.getParam4());
        }

        /**
         * @author LiFucheng
         * @version 1.0
         * @description: TODO  id 在参数5的低位 使用减法后可以直接强制转换
         * @date 2022/3/17 20:20
         */
        public static class UMsgQueue extends PriorityBlockingQueue<UMsg> {
            public UMsgQueue(int initialCapacity) {
                super(initialCapacity, Comparator.comparingLong(UMsg::getParam5));
            }
        }
    }
}
