package ctd.util.exp.standard;

import java.util.List;

import ctd.util.converter.ConversionUtils;
import ctd.util.exp.exception.ExprException;
import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;

@SuppressWarnings("rawtypes")
public class ISNULL extends Expression {
	public ISNULL() {
		name = "isNull";
	}

	public Object run(List ls,ExpressionProcessor processor) throws ExprException {
		Object lso = ls.get(1);
		if(lso instanceof List){
			lso = processor.run((List<?>)lso);
		}
		return lso == null;
	}

	public String toString(List ls, ExpressionProcessor processor) throws ExprException {
		Object lso = ls.get(1);
		if(lso instanceof List){
			lso = processor.toString((List<?>)lso);
		}
		StringBuffer sb = new StringBuffer(ConversionUtils.convert(lso, String.class));
		sb.append(" is null");
		return sb.toString();
	}

}
