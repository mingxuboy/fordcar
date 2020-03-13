package fordcar.condition;

/**
 * 基本查询条件生成。 生成的是单个条件的查询字段。 对应于sys_user
 * 
 * @author mingxu
 *
 */
public interface BaseCondition {
	/**
	 * 生成PreparedStatement的SQL部分， 如city_id in (?,?,?)
	 * 
	 * @return
	 */
	public String genSQLTemplate();
	
	/**
	 * 生成PreparedStatement的set部分需要的参数， 与{@link #genSQLTemplate()}与之匹配的参数值部分。
	 * 
	 * @return
	 */
	public String[] genSetArray();

}
