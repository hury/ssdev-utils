package ctd.util.exp.standard;

import java.util.List;

import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;
import ctd.util.converter.ConversionUtils;

public class MUL extends Expression {
	
	public MUL(){
		symbol = "*";
	}
	
	@Override
	public Object run(List<?> ls,ExpressionProcessor processor)throws ExprException {
		try{
			Number result = null;
			Object lso = ls.get(1);
			if(lso instanceof List){
				result = (Number)processor.run((List<?>)lso);
			}
			else{
				result = ConversionUtils.convert(lso, Number.class);
			}
			
			for(int i = 2,size = ls.size(); i < size; i ++){
				Number v = null;
				lso = ls.get(i);
				if(lso instanceof List){
					v = (Number)processor.run((List<?>)lso);
				}
				else{
					v = ConversionUtils.convert(lso, Number.class);
				}
				result = result.doubleValue() * v.doubleValue();
			}
			return result;
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
	}

}
