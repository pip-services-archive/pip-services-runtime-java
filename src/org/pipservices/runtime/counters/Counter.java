package org.pipservices.runtime.counters;

import java.time.*;
import com.fasterxml.jackson.annotation.*;

public class Counter {
    private String _name;
    private int _type;
    private Float _last;
    private Integer _count;
    private Float _min;
    private Float _max;
    private Float _avg;
    private ZonedDateTime _time;

    public Counter() {}
    
    public Counter(String name, int type) {
        _name = name;
        _type = type;
    }

    @JsonProperty("name")
    public String getName() { return _name; }
    public void setName(String name) { _name = name; }

    @JsonProperty("type")
    public int getType() { return _type; }
    public void setType(int type) { _type = type; }

    @JsonProperty("last")
    public Float getLast() { return _last; }
    public void setLast(Float last) { _last = last; }

    @JsonProperty("count")
    public Integer getCount() { return _count; }
    public void setCount(Integer count) { _count = count; }

    @JsonProperty("min")
    public Float getMin() { return _min; }
    public void setMin(Float min) { _min = min; }

    @JsonProperty("max")
    public Float getMax() { return _max; }
    public void setMax(Float max) { _max = max; }

    @JsonProperty("avg")
    public Float getAvg() { return _avg; }
    public void setAvg(Float avg) { _avg = avg; }

    @JsonProperty("time")
    public ZonedDateTime getTime() { return _time; }
    public void setTime(ZonedDateTime time) { _time = time; }
}
