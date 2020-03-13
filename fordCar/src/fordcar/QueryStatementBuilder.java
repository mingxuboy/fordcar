package fordcar;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

public class QueryStatementBuilder<V> {
	private StringBuilder sqlBuilder;
	private ArrayList<V> paramBuilder = new ArrayList<V>();

	public QueryStatementBuilder(String baseSQL) {
		this.sqlBuilder = new StringBuilder(baseSQL);
	}
	
	public QueryStatementBuilder<V> appendSQL(String sql) {
		this.sqlBuilder.append(sql);
		return this;
	}
	
	public QueryStatementBuilder<V> appendCond(String cond) {
		this.sqlBuilder.append(cond);
		return this;
	}
	
	public QueryStatementBuilder<V> appendParam(V param) {
		this.paramBuilder.add(param);
		return this;
	}
	
	public PreparedStatement getResult(Connection conn) throws SQLException {
		PreparedStatement pstmt = conn.prepareStatement(this.sqlBuilder.toString());
		
		for(int i = 1; i <= this.paramBuilder.size(); i++) {
			pstmt.setObject(i, this.paramBuilder.get(i-1));
		}
		
		return pstmt;
	}
	
	public String toString() {
		final StringBuilder params = new StringBuilder();
		paramBuilder.forEach(v -> params.append(v.toString()).append(" "));
		return String.format("Base SQL: %s, params %s", this.sqlBuilder, params);
	}

}
