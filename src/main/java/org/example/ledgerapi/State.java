package org.example.ledgerapi;


import org.json.JSONObject;
import java.nio.charset.StandardCharsets;

/**
 * 状态类
 * 此类包含key字段
 * 且有生命周期
 * 每一个当前状态都由其子类来决定
 *
 * @author liuxu
 * @date 2021/10/30 22:05
 */
public class State {

    protected String key;

    public State() {
    }

    String getKey(){
        return this.key;
    }

    /**
     * 分割 key
     * @return 分割后的数组
     */
    public String[] getSplitKey(){
        return State.splitKey(this.key);
    }

    public static String[] splitKey(String key){
        return key.split(":");
    }

    /**
     * 对象序列化
     * @param object 待字符序列化的对象
     * @return 字符流
     */
    public static byte[] serialize(Object object){
        String jsonStr = new JSONObject(object).toString();
        return jsonStr.getBytes(StandardCharsets.UTF_8);
    }

    /**
     * 字符数组拼接成为字符串
     * @param keyParts 字符串数组
     * @return 拼接后的字符串
     */
    public static String makeKey(String[] keyParts) {
        return String.join(":", keyParts);
    }
}
