package org.example;

import org.example.ledgerapi.State;
import org.hyperledger.fabric.contract.annotation.Property;
import org.json.JSONObject;
import org.json.JSONPropertyIgnore;

import java.nio.charset.StandardCharsets;

/**
 * @author liuxu
 * @date 2021/10/30 22:04
 */
public class CommercialPaper extends State {
    /**
     * ISSUED: 发行中
     * TRADING：交易中
     * REDEEMED：赎回中
     */
    public final static String ISSUED = "ISSUED";
    public final static String TRADING = "TRADING";
    public final static String REDEEMED = "REDEEMED";

    @Property()
    private String state = "";


    public String getState() {
        return state;
    }

    public CommercialPaper setState(String state) {
        this.state = state;
        return this;
    }

    @JSONPropertyIgnore()
    public boolean isIssued() {
        return this.state.equals(CommercialPaper.ISSUED);
    }

    @JSONPropertyIgnore()
    public boolean isTrading() {
        return this.state.equals(CommercialPaper.TRADING);
    }

    @JSONPropertyIgnore()
    public boolean isRedeemed() {
        return this.state.equals(CommercialPaper.REDEEMED);
    }

    public CommercialPaper setIssued() {
        this.state = CommercialPaper.ISSUED;
        return this;
    }

    public CommercialPaper setTrading() {
        this.state = CommercialPaper.TRADING;
        return this;
    }

    public CommercialPaper setRedeemed() {
        this.state = CommercialPaper.REDEEMED;
        return this;
    }

    @Property()
    private String paperNumber;

    @Property
    private String issuer;

    @Property
    private String issuerDataTime;

    @Property
    private int faceValue;

    @Property()
    private String maturityDateTime;

    @Property
    private String owner;

    /**
     * 无参构造
     */
    public CommercialPaper() {
        super();
    }
    /**
     * 全参构造
     * @param issuer 发行者
     * @param paperNumber 票据编号
     * @param issueDateTime 发行时间
     * @param maturityDateTime 到期时间
     * @param owner 当前拥有人
     * @param faceValue 票据面额
     * @param state 状态
     * @Defaultparamm key 继承于state
     * @return
     */
    public static CommercialPaper createInstance(String issuer, String paperNumber, String issueDateTime, String maturityDateTime, String owner, int faceValue, String state){
        return new CommercialPaper().setIssuer(issuer).setPaperNumber(paperNumber).setIssuerDataTime(issueDateTime).setMaturityDateTime(maturityDateTime).setOwner(owner).setFaceValue(faceValue).setState(state).setKey();
    }

    @Override
    public String toString() {
        return "Paper::" + this.key + "   " + this.getPaperNumber() +"  " + getIssuer() + "  " + getFaceValue();
    }

    public String getPaperNumber() {
        return paperNumber;
    }

    public CommercialPaper setKey() {
        this.key = State.makeKey(new String[]{ this.paperNumber });
        return this;
    }

    public CommercialPaper setPaperNumber(String paperNumber) {
        this.paperNumber = paperNumber;
        return this;
    }

    public String getIssuer() {
        return issuer;
    }

    public CommercialPaper setIssuer(String issuer) {
        this.issuer = issuer;
        return this;
    }

    public String getIssuerDataTime() {
        return issuerDataTime;
    }

    public CommercialPaper setIssuerDataTime(String issuerDataTime) {
        this.issuerDataTime = issuerDataTime;
        return this;
    }

    public int getFaceValue() {
        return faceValue;
    }

    public CommercialPaper setFaceValue(int faceValue) {
        this.faceValue = faceValue;
        return this;
    }

    public String getMaturityDateTime() {
        return maturityDateTime;
    }

    public CommercialPaper setMaturityDateTime(String maturityDateTime) {
        this.maturityDateTime = maturityDateTime;
        return this;
    }

    public String getOwner() {
        return owner;
    }

    public CommercialPaper setOwner(String owner) {
        this.owner = owner;
        return this;
    }

    /**
     * 反序列化
     * @param data 字节数据
     * @return 反序列化后创建实例
     */
    public static CommercialPaper deserialize(byte[] data){
        JSONObject json = new JSONObject(new String(data, StandardCharsets.UTF_8));

        String issuer = json.getString("issuer");
        String paperNumber = json.getString("paperNumber");
        String issueDateTime = json.getString("issueDateTime");
        String maturityDateTime = json.getString("maturityDateTime");
        String owner = json.getString("owner");
        int faceValue = json.getInt("faceValue");
        String state = json.getString("state");
        return createInstance(issuer, paperNumber, issueDateTime, maturityDateTime, owner, faceValue, state);
    }

    /**
     * 序列化票据
     * @param paper 票据
     * @return 票据
     */
    public static byte[] serialize(CommercialPaper paper){
        return State.serialize(paper);
    }


}
