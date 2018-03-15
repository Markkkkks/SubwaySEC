package huawei.biz.impl;

import huawei.biz.CardManager;
import huawei.exam.CardEnum;
import huawei.exam.ReturnCodeEnum;
import huawei.exam.SubwayException;
import huawei.model.Card;
import huawei.model.ConsumeRecord;
import huawei.model.InvalidCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import Utils.PriceUtil;

/**
 * <p>Title: 待考生实现类</p>
 * <p>
 * <p>Description: 卡票中心</p>
 * <p>
 * <p>Copyright: Copyright (c) 2013</p>
 * <p>
 * <p>Company: </p>
 *
 * @author
 * @version 1.0 OperationCenter V100R002C20, 2015/9/7]
 */
public class CardManagerImpl implements CardManager {
    private Card[] cardPool;
    private final int MAX_CARD = 100;
    private int total = 0;
    private int currentId = 0;
    private Queue<Integer> empty;
    private boolean isFull = false;
    private HashMap<String, ArrayList<ConsumeRecord>> records;
    private HashMap<String, Card> historyMap;

    public CardManagerImpl() {
        cardPool = new Card[MAX_CARD];
        empty = new LinkedList<>();
        for (int i = 0; i < MAX_CARD; i++) {
            empty.offer(i);
        }
        records = new HashMap<>();
        historyMap = new HashMap<>();
    }

    @Override
    public Card buyCard(String enterStation, String exitStation)
            throws SubwayException {
        if (isFull)
            throw new SubwayException(ReturnCodeEnum.E08, InvalidCard.getInstance());

        total++;
        if (total == 100)
            isFull = true;
        Card card = new Card();
        card.setCardId(String.valueOf(currentId));
        currentId++;
        card.setCardType(CardEnum.A);
        cardPool[empty.poll()] = card;
        return card;
    }

    @Override
    public Card buyCard(CardEnum cardEnum, int money)
            throws SubwayException {
        if (isFull)
            throw new SubwayException(ReturnCodeEnum.E08, InvalidCard.getInstance());

        if (!cardEnum.equals(CardEnum.A) && !cardEnum.equals(CardEnum.B) && !cardEnum.equals(CardEnum.C) && !cardEnum.equals(CardEnum.D))
            //TODO 非法指令转无效卡指令
            throw new SubwayException(ReturnCodeEnum.E07, InvalidCard.getInstance());//无效卡类型

        total++;
        if (total == 100)
            isFull = true;
        Card card = new Card();
        card.setCardId(String.valueOf(currentId));
        currentId++;
        card.setCardType(cardEnum);
        card.setMoney(money);
        cardPool[empty.poll()] = card;
        return card;
    }

    @Override
    public Card recharge(String cardId, int money)
            throws SubwayException {
        int index = findCardById(cardId);
        if (index == -1)
            throw new SubwayException(ReturnCodeEnum.E05, InvalidCard.getInstance());//未找到卡

        Card card = cardPool[index];
        int tempMoney = card.getMoney();
        card.setMoney(tempMoney + money);

        return card;
    }

    @Override
    public Card queryCard(String cardId) throws SubwayException {
        Card card;
        int index = findCardById(cardId);
        if (index == -1) {
            if (!historyMap.containsKey(cardId)) {
                throw new SubwayException(ReturnCodeEnum.E06, InvalidCard.getInstance());
            }
            card = historyMap.get(cardId);
        } else {
            card = cardPool[index];
        }
        return card;
    }

    @Override
    public Card deleteCard(String cardId)
            throws SubwayException {

        Card card = queryCard(cardId);
        if (card.isHistoryCard())
            throw new SubwayException(ReturnCodeEnum.E06, InvalidCard.getInstance());
        card.setHistoryCard(true);
        historyMap.put(card.getCardId(), card);
        int index = findCardById(cardId);
        cardPool[index] = null;
        empty.offer(index);
        total--;
        if (isFull)
            isFull = false;
        return card;
    }

    @Override
    public Card consume(String cardId, int billing)
            throws SubwayException {
        Card card = queryCard(cardId);
        if(card.isHistoryCard()==true)
            throw new SubwayException(ReturnCodeEnum.E06,InvalidCard.getInstance());

        if (card.getMoney() < billing) {
            throw new SubwayException(ReturnCodeEnum.E02, card);
        }
//        ConsumeRecord record = new ConsumeRecord();
//        record.setConsumeMoney(billing);
//        record.setEnterStation();
        //oneway ticket
        if (card.getCardType().equals(CardEnum.A)) {
            card.setMoney(0);
            card = deleteCard(cardId);
        }
        else {
            int tempMoney = card.getMoney();
            card.setMoney(tempMoney - billing);
        }
        if(card.getMoney() < 20 && card.getCardType().equals(CardEnum.A))
            throw new SubwayException(ReturnCodeEnum.E03,card);
        return card;
    }

    @Override
    public List<ConsumeRecord> queryConsumeRecord(String cardId)
            throws SubwayException {
        List<ConsumeRecord> recordbyId = records.get(cardId);
        if (recordbyId == null)
            return new ArrayList<ConsumeRecord>();

        return recordbyId;

    }

    private int findCardById(String cardId) {
        for (int i = 0; i < cardPool.length; i++) {
            if (cardPool[i] != null) {
                if (cardPool[i].getCardId().equals(cardId))
                    return i;
            }
        }
        return -1;
    }

    public void addRecord(String cardId, ConsumeRecord record) {
        ArrayList<ConsumeRecord> list;

        if (records.get(cardId) == null) {
            list = new ArrayList<>();
            list.add(record);
            records.put(cardId, list);
        } else {
            list = records.get(cardId);
            list.add(record);
            records.put(cardId, list);
        }

    }
}