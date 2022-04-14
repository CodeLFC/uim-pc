package gaozhi.online.uim.core.activity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author LiFucheng
 * @version 1.0
 * @description: TODO 各上下文间传输数据
 * @date 2022/3/13 16:24
 */
public class Intent {
    private final Map<String, Object> dataMap = new HashMap<>();

    public Intent() {

    }

    public void put(String key, Object data) {
        dataMap.put(key, data);
    }

    public <T> T getObject(String key) {
        return (T) dataMap.get(key);
    }

    public String getString(String key) {
        return getObject(key);
    }

    public int getInt(String key, int defaultValue) {
        Integer value = getObject(key);
        return value == null ? defaultValue : value;
    }

    public short getShort(String key, short defaultValue) {
        Short value = getObject(key);
        return value == null ? defaultValue : value;
    }

    public long getLong(String key, long defaultValue) {
        Long value = getObject(key);
        return value == null ? defaultValue : value;
    }

    public byte getByte(String key, byte defaultValue) {
        Byte value = getObject(key);
        return value == null ? defaultValue : value;
    }

    public float getFloat(String key, float defaultValue) {
        Float value = getObject(key);
        return value == null ? defaultValue : value;
    }

    public double getDouble(String key, double defaultValue) {
        Double value = getObject(key);
        return value == null ? defaultValue : value;
    }

    public char getChar(String key, char defaultValue) {
        Character value = getObject(key);
        return value == null ? defaultValue : value;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Boolean bool = getObject(key);
        return bool == null ? defaultValue : bool;
    }

    public Intent getIntent(String key) {
        return getObject(key);
    }

}
