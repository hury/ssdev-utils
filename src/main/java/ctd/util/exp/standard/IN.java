package ctd.util.exp.standard;

import java.util.HashSet;
import java.util.List;

import ctd.util.exp.Expression;
import ctd.util.exp.ExpressionProcessor;
import ctd.util.exp.exception.ExprException;
import ctd.util.converter.ConversionUtils;

public class IN extends Expression {

	public IN(){
		symbol = "in";
	}
	
	@Override
	public Object run(List<?> ls,ExpressionProcessor processor) throws ExprException {
		try{
			Object o = processor.run((List<?>)ls.get(1));
			List<?> rang = (List<?>)ls.get(2);
			HashSet<Object> set = new HashSet<Object>();
			set.addAll(rang);
			return set.contains(o);
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
	}
	
	@Override
	public String toString(List<?> ls,ExpressionProcessor processor) throws ExprException{
		try{
			Object o = processor.run((List<?>)ls.get(1));
			StringBuffer sb = new StringBuffer(ConversionUtils.convert(o,String.class));
			sb.append(" ").append(symbol).append("(");
			List<?> rang = (List<?>)ls.get(2);
			
			for(int i = 0, size = rang.size(); i < size; i ++){
				if(i > 0){
					sb.append(",");
				}
				Object r = rang.get(i);
				String s =ConversionUtils.convert(r,String.class);
				if(r instanceof Number){
					sb.append(s);
				}
				else{
					sb.append("'").append(s).append("'");
				}
				
			}
			
			return sb.append(")").toString();
		}
		catch(Exception e){
			throw new ExprException(e.getMessage());
		}
	}

}
