package ctd.util.converter.support;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Element;
import org.dom4j.Attribute;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;



import ctd.util.BeanUtils;



public class ElementToObject implements GenericConverter {
	
	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if(Element.class.isInstance(source)){
			try {
				Element el = (Element)source;
				Object dest = targetType.getType().newInstance();
				
				List<Attribute> attrs = el.attributes();
				for(Attribute attr : attrs){
					BeanUtils.setProperty(dest, attr.getName(), attr.getValue());
				}
				return dest;
			} 
			catch(Exception e){
        		throw new IllegalStateException("failed to convert element to bean",e);
        	}
		}
		else{
			throw new IllegalStateException("source object must be a Element");
		}
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> set = new HashSet<ConvertiblePair>();
		set.add(new ConvertiblePair(Element.class,Object.class));
		return set;
	}

}
