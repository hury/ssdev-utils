package ctd.util.converter.support;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import ctd.util.BeanUtils;
import ctd.util.converter.ConversionUtils;



public class ObjectToElement implements GenericConverter {
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		
			try {
				Map<String,Object> map = BeanUtils.map(source, HashMap.class);
				Element beanEl = DocumentHelper.createElement(source.getClass().getSimpleName());
				Set<String> fields  = map.keySet();
				for(String field : fields){
					Element fieldEl = DocumentHelper.createElement(field);
					Object val = map.get(field);
					
					if(val != null){
						fieldEl.setText(ConversionUtils.convert(val, String.class));
					}
					beanEl.add(fieldEl);
				}
				return beanEl;
			} 
			catch(Exception e){
        		throw new IllegalStateException("falied to convert bean to element",e);
        	}
		
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> set = new HashSet<ConvertiblePair>();
		set.add(new ConvertiblePair(Object.class,Element.class));
		return set;
	}

}
