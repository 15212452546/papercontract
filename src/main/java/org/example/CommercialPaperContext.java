package org.example;

import org.example.ledgerapi.State;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeStub;

/**
 * @author liuxu
 * @date 2021/10/31 11:13
 */
public class CommercialPaperContext extends Context {

    public PaperList paperList;

    public CommercialPaperContext(ChaincodeStub stub) {
        super(stub);
    }
}
