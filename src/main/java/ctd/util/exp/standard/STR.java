package ctd.util.exp.standard;

import java.util.List;

import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;
import ctd.util.converter.ConversionUtils;

public class STR extends Expression {
	
	public STR(){
		name = "s";
	}

	@Override
	public Object run(List<?> ls, ExpressionProcessor processor) throws ExprException {
		try{
			return ConversionUtils.convert(ls.get(1),String.class);
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
	}
	
	@Override
	public String toString(List<?> ls, ExpressionProcessor processor) throws ExprException {
		return "'" + run(ls,processor) + "'";
	}

}
