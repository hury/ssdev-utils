package ctd.util.exp.standard;

import java.util.List;

import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;
import ctd.util.PyConverter;
import ctd.util.converter.ConversionUtils;

public class PY extends Expression {

	public PY(){
		symbol = "pingyin";
	}
	
	@Override
	public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException {
		
		try{
			Object lso = ls.get(1);
			String str = null;
			if(lso instanceof List){
				str = (String)processor.run((List<?>)lso);
			}
			else{
				str = ConversionUtils.convert(lso, String.class);
			}
			return PyConverter.getFirstLetter(str);
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
		
	}
	
	@Override
	public String toString(List<?> ls,ExpressionProcessor processor) throws ExprException{
		return symbol + "(" + processor.toString((List<?>)ls.get(1)) + ")";
	}

}
