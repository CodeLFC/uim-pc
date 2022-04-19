package gaozhi.online.uim.im.conversation.message;

import gaozhi.online.uim.im.entity.IMMsg;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 消息
 * @date 2022/3/23 15:20
 */
public class IMMessage {
    private long fromId;
    private long toId;
    private IMMsgType msgType;
    private long time;
    private byte[] data;

    public void setFromId(long fromId) {
        this.fromId = fromId;
    }

    public void setToId(long toId) {
        this.toId = toId;
    }

    public long getFromId() {
        return fromId;
    }

    public long getToId() {
        return toId;
    }

    public IMMsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(IMMsgType msgType) {
        this.msgType = msgType;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "IMMessage{" +
                "fromId=" + fromId +
                ", toId=" + toId +
                ", msgType=" + msgType +
                ", time=" + time +
                ", dataLen=" + data.length +
                '}';
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 数据编码
     * @date 2022/3/23 17:02
     */
    public interface DataCoder<T> {

        IMMsgType getMsgType();

        byte[] parse2Binary(T data);

        T parse2T(byte[] data);

        String parse2Tip(byte[] data);
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 编解码器
     * @date 2022/3/23 15:30
     */
    public static class Codec {
        private static final Map<IMMsgType, DataCoder> dataCoderMap = new HashMap<>();

        public static void registerDataCoder(DataCoder dataCoder) {
            dataCoderMap.put(dataCoder.getMsgType(), dataCoder);
        }

        public static <T> DataCoder<T> getDataCoder(IMMsgType msgType) {
            return (DataCoder<T>) dataCoderMap.get(msgType);
        }

        public static IMMsg encode(long fromId, long toId, IMMessage message) {
            IMMsg msg = new IMMsg();
            msg.setFromId(fromId);
            msg.setToId(toId);
            msg.setMsgType(message.getMsgType().getType());
            msg.setTime(message.time);
            msg.setData(message.getData());
            return msg;
        }

        public static IMMessage decode(IMMsg src) {
            IMMessage message = new IMMessage();
            message.setMsgType(IMMsgType.getType(src.getMsgType()));
            message.setTime(src.getTime());
            message.setData(src.getData());
            message.fromId = src.getFromId();
            message.toId = src.getToId();
            return message;
        }
    }

    /**
     * @author LiFucheng
     * @version 1.0
     * @description: TODO 消息类型
     * @date 2022/3/18 20:09
     */
    public enum IMMsgType {
        TEXT(1000),
        REAL_AUDIO(1001),
        REAL_VIDEO(1002),
        FILE(1003),
        P2PNetInfoBeat(1004),
        P2PConnectionSniffing(1005);
        private final int type;

        IMMsgType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public static IMMsgType getType(int type) {
            for (IMMsgType typeE : IMMsgType.values()) {
                if (typeE.getType() == type) {
                    return typeE;
                }
            }
            return null;
        }
    }
}
