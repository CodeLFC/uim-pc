package gaozhi.online.uim.example.im.conversation.message;

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
    FILE(1003);
    private final int type;

    IMMsgType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public static IMMsgType getType(int type) {
        for (IMMsgType typeE : values()) {
            if (typeE.getType() == type) {
                return typeE;
            }
        }
        return null;
    }
}
