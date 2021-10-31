package org.example.ledgerapi.impl;

import org.example.ledgerapi.State;
import org.example.ledgerapi.StateDeserializer;
import org.example.ledgerapi.StateList;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.CompositeKey;

/**
 * 世界状态实现
 *
 *
 * @author liuxu
 * @date 2021/10/30 22:06
 */
public class StateListImpl implements StateList {

    private Context ctx;
    private String name;
    private StateDeserializer deserializer;

    public StateListImpl(Context ctx, String name, StateDeserializer deserializer) {
        this.ctx = ctx;
        this.name = name;
        this.deserializer = deserializer;
    }

    @Override
    public StateList addState(State state) {
        ChaincodeStub chaincodeStub = this.ctx.getStub();
        String[] splitKey = state.getSplitKey();

        CompositeKey ledgerKey = chaincodeStub.createCompositeKey(this.name, splitKey);

        byte[] data = State.serialize(state);
        this.ctx.getStub().putState(ledgerKey.toString(), data);
        return this;
    }

    @Override
    public State getState(String key) {
        CompositeKey ledgerKey = this.ctx.getStub().createCompositeKey(this.name, key);

        byte[] data = this.ctx.getStub().getState(ledgerKey.toString());
        if (data != null){
            return this.deserializer.deserialize(data);
        }
        return null;
    }

    @Override
    public StateList updateState(State state) {
        CompositeKey ledgerKey = this.ctx.getStub().createCompositeKey(this.name, state.getSplitKey());

        byte[] data = State.serialize(state);

        this.ctx.getStub().putState(ledgerKey.toString(), data);

        return this;
    }
}
