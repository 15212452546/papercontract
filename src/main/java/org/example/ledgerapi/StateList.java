package org.example.ledgerapi;

import org.example.ledgerapi.impl.StateListImpl;
import org.hyperledger.fabric.contract.Context;

/**
 * 状态列表
 * 主要用于添加状态、获取当前状态列表、获取特定状态、更新状态
 *
 * @author liuxu
 * @date 2021/10/30 22:06
 */
public interface StateList {
    static StateList getStateList (Context ctx, String listName, StateDeserializer deserializer) {
        return new StateListImpl(ctx, listName,deserializer);
    }

    public StateList addState(State state);

    public State getState(String key);

    public StateList updateState(State state);
}
