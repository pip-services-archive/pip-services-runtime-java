package org.pipservices.runtime.data;

import org.pipservices.runtime.portability.*;

public class PagingParams {
	private Integer _skip;
	private Integer _take;
	private boolean _total;
	
    public PagingParams() { }

    public PagingParams(Object skip, Object take, Object total) {
        _skip = Converter.toNullableInteger(skip);
        _take = Converter.toNullableInteger(take);
        _total = Converter.toBooleanWithDefault(total, false);
    }

    public Integer getSkip() { 
    	return _skip; 
	}

    public int getSkip(int minSkip) {
    	if (_skip == null) return minSkip;
    	if (_skip < minSkip) return minSkip;
    	return _skip; 
	}

    public Integer getTake() {
    	return _take; 
	}

    public int getTake(int maxTake) {
    	if (_take == null) return maxTake;
    	if (_take < 0) return 0;
    	if (_take > maxTake) return maxTake;
    	return _take; 
	}
    
    public boolean isTotal() { 
    	return _total; 
	}

	public static PagingParams fromValue(Object value) {
		if (value instanceof PagingParams)
			return (PagingParams)value;
		if (value instanceof DynamicMap)
			return PagingParams.fromMap((DynamicMap)value);
		
		DynamicMap map = DynamicMap.fromValue(value);
		return PagingParams.fromMap(map);
	}
	
	public static PagingParams fromTuples(Object... tuples) {
		DynamicMap map = DynamicMap.fromTuples(tuples);
		return PagingParams.fromMap(map);
	}

	public static PagingParams fromMap(DynamicMap map) {
        Integer skip = map.getNullableInteger("skip");
        Integer take = map.getNullableInteger("take");
        boolean total = map.getBooleanWithDefault("total", true);
		return new PagingParams(skip, take, total);
	}
}
