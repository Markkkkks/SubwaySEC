package huawei.model;

import huawei.exam.CardEnum;

public class InvalidCard {
	private static Card instance = new Card();
	static{
		instance.setCardId(String.valueOf(Integer.MAX_VALUE));
		instance.setCardType(CardEnum.E);
	}
	private InvalidCard(){
		
	}
	
	public static Card getInstance(){
		return instance;
	}
}
