package org.pipservices.runtime.data;

import java.util.*;
import com.fasterxml.jackson.annotation.*;

public class DataPage<T> {
	private Integer _total;
	private List<T> _data;
	
	public DataPage() {}
	
	public DataPage(Integer total, List<T> data) {
		_total = total;
		_data = data;
	}
	
    @JsonProperty("total")
	public Integer getTotal() { return _total; }
	public void setTotal(Integer value) { _total = value; }
	
    @JsonProperty("data")
	public List<T> getData() { return _data; }
	public void setData(List<T> value) { _data = value; }
}
