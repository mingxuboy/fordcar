/**
 * 
 */
package fordcar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

import fordcar.condition.BaseCondition;
import fordcar.condition.BaseConditionFactory;

/**
 * @author mingxu
 *
 */
public class UserCollection {
	private StringTokenizer tokens;
	
	//检索条件与sys_user 中的检索Key的映射表
	private final ResourceBundle keyMapping = ResourceBundle.getBundle("fordcar/mapping");
	private final ResourceBundle sqlTemplate = ResourceBundle.getBundle("fordcar/sql_template");
	
	private BaseConditionFactory bcf;

	public UserCollection(String inTokens, BaseConditionFactory bcf) {
		this.tokens = new StringTokenizer(inTokens);
		this.bcf = bcf;
	}
	
	public QueryStatementBuilder<String> createStatementBuilder() {
		QueryStatementBuilder<String> qsb = new QueryStatementBuilder<>(sqlTemplate.getString("sys_user_sql"));
		ArrayList<String> conds = new ArrayList<String>();
		
		BaseCondition cond = this.parseBaseCondition(conds);
		qsb.appendSQL(cond.genSQLTemplate());
		while(this.tokens.hasMoreTokens()) {
			qsb.appendCond(OperEnum.parseValue(this.tokens.nextToken()).toString())
				.appendSQL(this.parseBaseCondition(conds).genSQLTemplate());
		}
		
		conds.forEach(v -> qsb.appendParam(v));
		
		return qsb;
	}
	
	private BaseCondition parseBaseCondition(ArrayList<String> conds) {
		String key = this.tokens.nextToken();
		OperEnum oper = OperEnum.parseValue(this.tokens.nextToken());
		String value = this.tokens.nextToken();
		
		BaseCondition cond = this.bcf.createBaseCondition(keyMapping.getString(key), value, oper);
		conds.addAll(Arrays.asList(cond.genSetArray()));
		
		return cond;
	}
	
	
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println(new UserCollection("city  = 北京 and service_region = 天上人间,明月江南  or city != 河南 and city = C", BaseConditionFactory.getInstance()).createStatementBuilder());

	}

}
