package ctd.util.exp.standard;

import java.util.List;

import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;

public class AND extends Expression {

	public AND(){
		symbol = "and";
		needBrackets = true;
	}
	
	@SuppressWarnings({"unchecked" })
	@Override
	public Object run(List<?> ls,ExpressionProcessor processor) throws ExprException {
		try{
			for(int i = 1,size = ls.size();i < size; i ++){
				boolean r = (Boolean)processor.run((List<Object>)ls.get(i));
				if(!r){
					return false;
				}
			}
			return true;
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
	}

}
