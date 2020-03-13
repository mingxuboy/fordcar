package fordcar;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import fordcar.condition.BaseConditionFactory;

/**
 * 搜索条件语法：
 * 
 * Key = value and key2 = value2,value3,value4 or h != i,j,k
 * 
 * <ol>
 * 其中：<li> 检索条件： key不支持中文， Value是支持中文的
 *             每一个检索条件需要在： mapping.properties文件配置过。否则会抛错误。
 *                  配置的格式为{Key} = {对应于sys_user表中对应的检索字段}, 比如城市这个字段，Key为city, 但user表里面叫做city_id, 那么就
 *                  要配置爱city = city_id
 *     </li>
 *     <li>基本条件为： key = value。 其中key、=、value都必须用空格分开。 时间关系我用的是StringTokenize解析的。 只认空格。 </li>
 *     <li>等于不等于的关键字为 “=”和“！=”</li>
 *     <li>对于多选的情况，使用逗号分隔，但中间不要有空格。 例如不要写成： i, j, k 会出错。</li>
 *     <li>对于城市、区域等，和用户表不直接关联的情况， 可以加一个用户关联城市的冗余字段，就能够满足这个框架了, 如果加起来太麻烦， 我预留的入口。 <br>
 *     可以重写一个BaseCodition，然后Facotry那里根据核实的Key来创建即可 ，比如用户-经销商-城市。 可以先根据城市计算出那些经销商，然后变成 in(经销商1，经销商2，。。。。），拼在sys_user的后面即可</li>
 *     <li>由于时间关系， SQL执行的那块代码我没有做测试（因为要连接你实际的数据库表才可以），Connect ==> ResultSet那块跑起来可能是不通的</li>
 * </ol>
 * 
 */
public class Main {
	

	//下面是一个例子
	private static String  demoCondition = "city = 北京 and area = 大兴,四环  or city != 上海";
	private static String yourConnectionUrl = "XXX";
	
	public static void main(String[] args) throws SQLException, ClassNotFoundException {
		UserCollection c = new UserCollection(demoCondition, BaseConditionFactory.getInstance());
		
		Class.forName("com.mysql.jdbc");
		try(Connection conn = DriverManager.getConnection(yourConnectionUrl)) {
		
			try(PreparedStatement pstmt = c.createStatementBuilder().getResult(conn)) {
				ResultSet rs = pstmt.executeQuery();
				//read your result
			}
		}
	}
	
	

}
