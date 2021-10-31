package org.example;

import org.example.ledgerapi.State;
import org.hyperledger.fabric.Logger;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;
import org.hyperledger.fabric.shim.ChaincodeStub;


/**
 * 智能合约
 * @author liuxu
 * @date 2021/10/31 11:14
 */
@Contract(name = "org.papernet.commercialpaper", info = @Info(title = "MyAsset contract", description = "", version = "0.0.1", license = @License(name = "SPDX-License-Identifier: Apache-2.0", url = ""),contact = @Contact(email = "java-contract@example.com", name = "java-contract", url = "http://java-contract.me")))
@Default
public class CommercialPaperContract implements ContractInterface {

    private final static Logger LOG = Logger.getLogger(CommercialPaperContract.class.getName());

    @Override
    public Context createContext(ChaincodeStub stub) {
        return new CommercialPaperContext(stub);
    }

    public CommercialPaperContract() {
    }

    @Transaction
    public void instantiate(CommercialPaperContext ctx){
        // 此方法不需要具体实现
        // 如果有必要，可以在此进行数据迁移
        LOG.info(" 无需要迁移的数据");
    }

    /**
     * 发行票据
     * @param ctx 内容上下文
     * @param issuer 发行者
     * @param paperNumber 票据编号
     * @param issueDateTime 发行时间
     * @param maturityDateTime 到期时间
     * @param faceValue 面值
     * @return 发行的票据
     */
    @Transaction
    public CommercialPaper issue(CommercialPaperContext ctx, String issuer, String paperNumber, String issueDateTime, String maturityDateTime, int faceValue) {

        CommercialPaper paper = CommercialPaper.createInstance(issuer, paperNumber, issueDateTime, maturityDateTime, issuer, faceValue, "");

        // 设置票据当前状态为发行状态
        paper.setIssued();

        // 设置当前所有人（发行时默认为发行者）  个人认为此条数据多余
        paper.setOwner(issuer);

        // 在票据清单中加入当前票据
        ctx.paperList.addPaper(paper);

        return paper;

    }

    /**
     * 购买
     * @param ctx 交易上下文
     * @param issuer 发行者
     * @param paperNumber 票据号码
     * @param currentOwner 当前拥有者
     * @param newOwner 新的拥有者
     * @param price 成交价格
     * @param purchaseDateTime 购买日期
     * @return 交易后的票据信息
     */
    @Transaction
    public CommercialPaper buy(CommercialPaperContext ctx, String issuer, String paperNumber, String currentOwner,
                               String newOwner, int price, String purchaseDateTime) {

        String paperKey = State.makeKey(new String[]{paperNumber});
        CommercialPaper paper = ctx.paperList.getPaper(paperKey);

        // 验证一下取出的票据是不是属于当前所有人
        if (!paper.getOwner().equals(currentOwner)) {
            throw new RuntimeException("当前" + issuer + paperNumber + "票据不属于" + currentOwner);
        }

        // 如果是第一次交易，修改票据交易状态
        if (paper.isIssued()){
            paper.setTrading();
        }

        // 如果处于交易状态交易（即交易合法，修改所有人），否则抛出异常
        if (paper.isTrading()){
            paper.setOwner(newOwner);
        }else {
            throw new RuntimeException("当前票据并不处于交易状态，当前状态为" + paper.getState());
        }

        // 更新票据清单（账本）
        ctx.paperList.updatePaper(paper);

        return paper;

    }

    /**
     * 赎回策略
     * @param ctx 交易上下文
     * @param issuer 发行者
     * @param paperNumber 票据编号
     * @param redeemingOwner 赎回者
     * @param redeemDateTime 赎回时间
     * @return 更新后的票据
     */
    @Transaction
    public CommercialPaper redeem(CommercialPaperContext ctx, String issuer, String paperNumber, String redeemingOwner,
                                  String redeemDateTime) {
        String key = CommercialPaper.makeKey(new String[]{paperNumber});
        CommercialPaper paper = ctx.paperList.getPaper(key);

        if (paper.isRedeemed()){
            throw new RuntimeException("该票据已经被赎回了");
        }

        if (redeemingOwner.equals(issuer)){
            paper.setOwner(paper.getIssuer());
            paper.setRedeemed();
        }else {
            throw new RuntimeException("赎回人不合法");
        }
        ctx.paperList.updatePaper(paper);
        return paper;
    }
}
