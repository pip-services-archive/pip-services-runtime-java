package org.pipservices.runtime.persistence;

import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.config.*;
import org.pipservices.runtime.data.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.portability.*;

import com.fasterxml.jackson.databind.*;

public abstract class FilePersistence<T extends IIdentifiable> extends AbstractPersistence {
	private final static DynamicMap DefaultConfig = DynamicMap.fromTuples(
		"options.max_page_size", 100
	); 

	private Random _random = new Random();
	private Class<?> _itemType;
    ObjectMapper _mapper = new ObjectMapper();
    JavaType _typeRef;
    
    protected String _path;
    protected File _file;
    protected Collection<T> _initialData;
    protected int _maxPageSize;
    protected List<T> _items;

    // Pass the item type since Jackson cannot recognize type from generics
    // This is related to Java type erasure issue
    protected FilePersistence(ComponentDescriptor descriptor, Class<?> itemType) {
        super(descriptor);
        
        _itemType = itemType;
        _typeRef = _mapper.getTypeFactory().
    		constructCollectionType(List.class, _itemType);
    }

	/**
	 * Sets component configuration parameters and switches from component
	 * to 'Configured' state. The configuration is only allowed once
	 * right after creation. Attempts to perform reconfiguration will 
	 * cause an exception.
	 * @param config the component configuration parameters.
	 * @throws MicroserviceError when component is in illegal state 
	 * or configuration validation fails. 
	 */
	@Override
    @SuppressWarnings("unchecked")
    public void configure(ComponentConfig config) throws MicroserviceError {
    	checkNewStateAllowed(State.Configured);
    	
    	config = config.withDefaults(DefaultConfig);
        DynamicMap options = config.getOptions();
    	
        if (options == null || options.hasNot("path"))
            throw new ConfigError(this, "NoPath", "Data file path is not set");
        
        super.configure(config);

        _path = options.getString("path");
        _maxPageSize = options.getInteger("max_page_size");
        _initialData = (Collection<T>)options.get("data");
    }
    
	/**
	 * Opens the component, performs initialization, opens connections
	 * to external services and makes the component ready for operations.
	 * Opening can be done multiple times: right after linking 
	 * or reopening after closure.  
	 * @throws MicroserviceError when initialization or connections fail.
	 */
	@Override
    public void open() throws MicroserviceError {
    	checkNewStateAllowed(State.Opened);
    	
        _file = new File(_path);

        // Fill with predefined data (for testing)
        if (_initialData != null)
            _items = new ArrayList<T>(_initialData);
        else load();

        super.open();
    }

	/**
	 * Closes the component and all open connections, performs deinitialization
	 * steps. Closure can only be done from opened state. Attempts to close
	 * already closed component or in wrong order will cause exception.
	 * @throws MicroserviceError with closure fails.
	 */
    @Override
    public void close() throws MicroserviceError {
    	checkNewStateAllowed(State.Closed);

    	save();
        
    	super.close();
    }

    public void load() throws MicroserviceError {
        trace(null, "Loading data from file at " + _path);

        // If doesn't exist then consider empty data
        if (!_file.exists()) {
            _items = new ArrayList<T>();
            return;
        }

        try {
            _items = _mapper.readValue(_file, _typeRef);
        } catch (Exception ex) {
        	throw new FileError(this, "ReadFailed", "Failed to read data file: " + ex)
        		.withCause(ex);
        }
    }

    public void save() throws MicroserviceError {
        trace(null, "Saving data to file at " + _path);

        try {
            _mapper.writeValue(_file, _items);
        } catch (Exception ex) {
        	throw new FileError(this, "WriteFailed", "Failed to write data file: " + ex)
        		.withCause(ex);
        }
    }

    @Override
    public void clearTestData() throws MicroserviceError {
        _items = new ArrayList<T>();
        save();
    }

    public DataPage<T> getPage(String correlationId, Predicate<T> filter, PagingParams paging, Comparator<T> sort) {
        Stream<T> items = this._items.stream();

        // Apply filter
        if (filter != null)
            items = items.filter(filter);

        // Extract a page
        paging = paging != null ? paging : new PagingParams();
        int skip = paging.getSkip(-1);
        int take = paging.getTake(_maxPageSize);

        Integer total = null;
        if (paging.isTotal()) {
        	List<T> selectedItems = items.collect(Collectors.toList());
        	total = selectedItems.size();
        	items  = selectedItems.stream();
        }
        
        if (skip > 0)
            items = items.skip(skip);
        items = items.limit(take);

        // Apply sorting
        if (sort != null)
            items = items.sorted(sort);

        return new DataPage<T>(total, items.collect(Collectors.toList()));
    }

    public <S> DataPage<S> getPage(String correlationId, Predicate<T> filter, PagingParams paging,
		Comparator<T> sort, Function<T, S> select) {
    	
        DataPage<T> page = getPage(correlationId, filter, paging, sort);
        Integer total = page.getTotal();
        List<S> items = page.getData().stream()
    		.map(select)
    		.collect(Collectors.toList());

        return new DataPage<S>(total, items);
    }

    public List<T> getList(String correlationId, Predicate<T> filter, Comparator<T> sort) {
        Stream<T> items = this._items.stream();

        // Apply filter
        if (filter != null)
            items = items.filter(filter);

        // Apply sorting
        if (sort != null)
            items = items.sorted(sort);

        return items.collect(Collectors.toList());
    }

    public <S> List<S> getList(String correlationId, Predicate<T> filter, Comparator<T> sort, Function<T, S> select) {
        List<S> items = getList(correlationId, filter, sort).stream()
    		.map(select)
    		.collect(Collectors.toList());
        return items;
    }

    protected T getById(String correlationId, String id) {
        Optional<T> item = _items.stream()
    		.filter((v) -> v.getId().equals(id))
    		.findFirst();
        return item.isPresent() ? item.get() : null;
    }

    protected T getRandom(String correlationId) {
        if (_items.size() == 0)
            return null;

        T item = _items.get(_random.nextInt(_items.size()));
        return item;
    }

    protected T create(String correlationId, T item) throws MicroserviceError {
        item.setId(item.getId() != null ? item.getId() : createUuid());
        _items.add(item);

        save();
        return item;
    }

    protected T replace(String correlationId, String id, T newItem) throws MicroserviceError {
    	T item = getById(correlationId, id);
    	if (item == null) return null;
    	
        int index = _items.indexOf(item);
        if (index < 0) return null;

        newItem.setId(id);
        _items.set(index, newItem);

        save();
        return newItem;
    }

    protected T update(String correlationId, String id, DynamicMap newValues) throws MicroserviceError {
        T item = getById(correlationId, id);
        if (item == null) return null;

        newValues.assignTo(item);

        save();
        return item;
    }

    protected T update(String correlationId, String id, Object newValues) throws MicroserviceError {
        return update(correlationId, id, Converter.toNullableMap(newValues));
    }

    protected void delete(String correlationId, String id) throws MicroserviceError {
    	T item = getById(correlationId, id);
    	if (item == null) return;
    	
        int index = _items.indexOf(item);
        if (index < 0) return;

        _items.remove(index);

        save();
    }

}