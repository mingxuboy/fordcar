/**
 * 
 */
package fordcar;

/**
 * @author mingxu
 *
 */
public enum OperEnum {
	EQUALS("="),
	NOT_EQUALS("!="),
	AND("and"),
	OR("or");
	
	private String oper;
	private OperEnum(String oper) {
		this.oper = oper;
	}
	
	public String toString() {
		return " " + this.oper + " ";
	}
	
	public static OperEnum parseValue(String oper) {
		for(OperEnum v:OperEnum.values()) {
			if(v.oper.equals(oper)) return v;
		}
		
		throw new NullPointerException(String.format("Illegal operation %s", oper));
	}
}
