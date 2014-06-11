package ctd.util.converter.support;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;

import ctd.util.BeanUtils;


public class MapToObject implements GenericConverter {
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if(sourceType.isMap()){
			try {
				Object target = targetType.getType().newInstance();
				Map<String,Object> map = (Map<String,Object>)source;
				Set<String> keys = map.keySet();
				for(String k : keys){
					BeanUtils.setProperty(target, k, map.get(k));
				}
				return target;
			} 
			catch(Exception e){
        		throw new IllegalStateException("falied to convert map to bean",e);
        	}
		}
		else{
			throw new IllegalStateException("source object must be a map");
		}
	}

	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		Set<ConvertiblePair> set = new HashSet<ConvertiblePair>();
		set.add(new ConvertiblePair(Map.class,Object.class));
		return set;
	}

}
