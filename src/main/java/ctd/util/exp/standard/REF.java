package ctd.util.exp.standard;

import java.util.HashMap;
import java.util.List;
import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;
import ctd.util.context.ContextUtils;

public class REF extends Expression {
	
	public REF(){
		symbol = "$";
		name = symbol;
	}
	
	@Override
	public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException {
		try{
			String nm = (String)ls.get(1);
			if (nm.startsWith("%")) {
				nm = nm.substring(1);
			}
			return ContextUtils.get(nm);
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public String toString(List<?> ls,  ExpressionProcessor processor) throws ExprException {
		try{
			String nm = (String)ls.get(1);
			if (!nm.startsWith("%")) {
				return nm;
			}
			Boolean forPreparedStatement = ContextUtils.get("$exp.forPreparedStatement",Boolean.class);
			Object o = run(ls,processor);
			
			if(forPreparedStatement != null && forPreparedStatement == true){

				HashMap<String,Object> parameters = ContextUtils.get("$exp.statementParameters",HashMap.class);
				String key = "arg" + parameters.size();
				parameters.put(key, o);
				return ":"  + key;
			}
			else{	
				if(o instanceof Number){
					return String.valueOf(o);
				}
				else{
					return "'" + String.valueOf(o) + "'";
				}
			}
		}
		catch(Exception e){
			throw new ExprException(e);
		}
	}

}
