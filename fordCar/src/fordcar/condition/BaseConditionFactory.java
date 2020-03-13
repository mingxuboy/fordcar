package fordcar.condition;

import fordcar.OperEnum;

public class BaseConditionFactory {
	private static BaseConditionFactory instance = new BaseConditionFactory();
	private BaseConditionFactory() {
		// TODO Auto-generated constructor stub
	}
	
	public BaseCondition createBaseCondition(String key, String value, OperEnum oper) {
		return new KVBaseCondition(key, value, oper);
	}
	
	public static BaseConditionFactory getInstance() {
		return instance;
	}

}
