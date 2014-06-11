package ctd.util.exp.standard;

import java.util.List;

import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;
import ctd.util.converter.ConversionUtils;

public class GT extends Expression {
	
	public GT(){
		symbol = ">";
	}
	
	@Override
	public Object run(List<?> ls, ExpressionProcessor processor)throws ExprException {
		try{
			Object lso = ls.get(1);
			Number v1 = null;
			if(lso instanceof List){
				v1 = ConversionUtils.convert(processor.run((List<?>)lso),Number.class);
			}
			else{
				v1 = ConversionUtils.convert(lso, Number.class);
			}
			for(int i = 2,size = ls.size(); i < size; i ++){
				Number v2 = null;
				lso = ls.get(i);
				if(lso instanceof List){
					v2 = ConversionUtils.convert(processor.run((List<?>)lso),Number.class);
				}
				else{
					v2 = ConversionUtils.convert(lso, Number.class);
				}
				if(v1.doubleValue() <= v2.doubleValue()){
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
