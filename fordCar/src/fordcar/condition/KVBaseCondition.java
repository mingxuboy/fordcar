package fordcar.condition;

import fordcar.OperEnum;

/**
 * 生成能够与sys_user表中有直接外键关联的查询条件。 比如：<br>
 * 经销商表----merchant_id----sys_user，这样的关联<br>
 * 
 * 最终生成的效果：<br>
 * <code>
 * merchant_id in (merchantA, merchantB,...)
 * </code>
 * 
 * <br>
 * 不适合的情况： 过滤条件并不是和sys_user直接关联的。 则需要自定义BaseCondition的实现。比如<br>
 * 大区---region_id---经销商表----merchant_id----sys_user<br>。 这样的关联。
 * 
 * 最终仍然需要生成<br>
 * <code>
 * merchant_id in (merchantA, merchantB,...)
 * </code>
 * 
 * <br>的效果。 但是因为查询条件是大区的id，因此需要做硬编码的查询, 伪代码实现如下：<br>
 * 
 * <br>
 * <pre>
 * <code>
 * merchantIdList = select m.merchant_id from 经销商 m where m.region_id in (区id1,区id2,....);
 * 
 * 可以通过继承KVBaseCondition来实现
 * class RegionCondtion extends KVBaseCondition {
 * 	RegionCondtion(String key, String value, OperEnum oper) {
 * 		merchantIdList = select m.merchant_id from 经销商 m where m.region_id in (value);
 *      super(key, merchantIdList.toValue(), oper) 
 *  }
 * }
 * 
 * 
 * </code> <br><br>
 * </pre>
 * 这样就知道了区下有哪些经销商。 而经销商的merchant_id是与sys_user表关联的。 
 * 
 * @author mingxu
 *
 */
public class KVBaseCondition implements BaseCondition {
	protected String key;
	protected String[] setArray;
	protected OperEnum oper;

	KVBaseCondition(String key, String value, OperEnum oper) {
		this.key = key;
		this.setArray = value.split(",");;
		this.oper = oper;
	}

	/**
	 * 这里生成的是SQL的部分
	 * 
	 * @return
	 */
	public String genSQLTemplate() {
		
		String sqlOper = OperEnum.EQUALS.equals(oper) ? "in" : " not in";
		
		StringBuilder params = new StringBuilder("?");
		for(int i = 1; i < this.genSetArray().length;i++) {
			params.append(",?");
		}
		
		//由于MySQL 的Driver不支持conection.getArray操作，所以这里拆开来了。 生成类似于
		//key in或 not in (value1, value2的形式),当然这里value1,2是用问号代替的，为的是做PreparedStatement的setString操作
		//不直接拼写SQL是为了防止SQL注入攻击。
		return String.format("%s %s(%s)", this.key, sqlOper, params.toString());
	}
	
	public String[] genSetArray() {
		return this.setArray;
	}	
	
	public static void main(String[] args) {
		KVBaseCondition cond1 = new KVBaseCondition("经销商id", "经销商A", OperEnum.EQUALS);
		
		System.out.println(cond1.genSQLTemplate());
		
		KVBaseCondition cond2 = new KVBaseCondition("经销商id", "经销商A,经销商B,经销商C", OperEnum.NOT_EQUALS);
			
		System.out.println(cond2.genSQLTemplate());
		
	}

}
