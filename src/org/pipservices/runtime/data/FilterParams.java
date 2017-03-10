package org.pipservices.runtime.data;

import org.pipservices.runtime.portability.*;

public class FilterParams extends DynamicMap {
	private static final long serialVersionUID = -5476176704133599595L;

	public FilterParams() {}
	
	public FilterParams(DynamicMap map) {
		putAll(map);
	}
		
	public static FilterParams fromValue(Object value) {
		if (value instanceof FilterParams)
			return (FilterParams)value;
		if (value instanceof DynamicMap)
			return new FilterParams((DynamicMap)value);
		
		return new FilterParams(DynamicMap.fromValue(value));
	}
	
	public static FilterParams fromTuples(Object... tuples) {
		FilterParams filter = new FilterParams();
		filter.setTuplesArray(tuples);
		return filter;
	}

	public static FilterParams fromMap(DynamicMap map) {
		return new FilterParams(map);
	}
}
