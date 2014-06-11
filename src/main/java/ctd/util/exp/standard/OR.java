package ctd.util.exp.standard;

import java.util.List;

import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;

public class OR extends Expression {

	public OR(){
		symbol = "or";
		needBrackets = true;
	}
	
	@Override
	public Object run(List<?> ls,ExpressionProcessor processor) throws ExprException {
		try{
			for(int i = 1,size = ls.size();i < size; i ++){
				boolean r = (Boolean)processor.run((List<?>)ls.get(i));
				if(r){
					return true;
				}
			}
			return false;
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
		
	}

}
