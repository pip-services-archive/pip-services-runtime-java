package org.pipservices.runtime.run;

import java.util.*;

import org.pipservices.runtime.*;
import org.pipservices.runtime.errors.*;
import org.pipservices.runtime.errors.UnknownError;
import org.pipservices.runtime.portability.DynamicMap;

public class LifeCycleManager {
	
	public static int getState(List<? extends IComponent> components) {
		int state = State.Undefined; // Fake state
		for (IComponent component : components) {
			if (state == State.Undefined || component.getState() < state)
				state = component.getState();
		}
		return state;
	}
	
    public static void linkComponents(DynamicMap context, List<? extends IComponent> components) throws MicroserviceError {    	
    	link(context, new ComponentSet(components));
    }

    public static void link(DynamicMap context, ComponentSet components) throws MicroserviceError {
    	List<IComponent> orderedList = components.getAllOrdered();
        for (IComponent component : orderedList) {
        	if (component.getState() == State.Configured)
        		component.link(context, components);
        }
    }

    public static void linkAndOpenComponents(DynamicMap context, List<? extends IComponent> components) throws MicroserviceError {
    	linkAndOpen(context, new ComponentSet(components));
    }

    public static void linkAndOpen(DynamicMap context, ComponentSet components) throws MicroserviceError {
        link(context, components);
        open(components);
    }

    public static void openComponents(List<? extends IComponent> components) throws MicroserviceError {
        List<IComponent> opened = new ArrayList<IComponent>();
        try {
            for (IComponent component : components) {
            	if (component.getState() != State.Opened)
            		component.open();
                opened.add(component);
            }
        } catch (Exception ex) {
            LogWriter.trace(components, "Microservice opening failed with error " + ex);
            LifeCycleManager.forceCloseComponents(opened, false);
            throw ex;
        }
    }

    public static void open(ComponentSet components) throws MicroserviceError {
    	openComponents(components.getAllOrdered());
    }

    public static void closeComponents(List<? extends IComponent> components) throws MicroserviceError {
        // Close in reversed order
        List<IComponent> toClose = new ArrayList<IComponent>();
        for (IComponent component: components) {
        	toClose.add(0, component);
        }

        try {
            for (IComponent component : toClose) {
            	if (component.getState() == State.Opened)
            		component.close();
            }
        } catch (Exception ex) {
            LogWriter.trace(components, "Microservice closure failed with error " + ex);
            throw ex;
        }
    }

    public static void close(ComponentSet components) throws MicroserviceError {
    	closeComponents(components.getAllOrdered());
    }

    public static void forceCloseComponents(List<? extends IComponent> components) throws MicroserviceError {
    	forceCloseComponents(components, true);
    }

    public static void forceCloseComponents(
		List<? extends IComponent> components, boolean throwException
	) throws MicroserviceError {	
        // Close in reversed order
        List<IComponent> toClose = new ArrayList<IComponent>();
        for (IComponent component: components) {
        	toClose.add(0, component);
        }
        
        MicroserviceError firstError = null;

        for (IComponent component : toClose) {
            try {
            	if (component.getState() == State.Opened)
            		component.close();
	        } catch (MicroserviceError ex) {
	            LogWriter.trace(components, "Microservice closure failed with error " + ex);
	            firstError = firstError != null ? firstError : ex;
            } catch (Exception ex) {
                LogWriter.trace(components, "Microservice closure failed with error " + ex);
                firstError = firstError != null 
            		? firstError 
    				: new UnknownError(
						"CloseFailed", 
						"Failed to close component " + component + ": " + ex
					).wrap(ex);
	        }
        }

        if (firstError != null && throwException)
            throw firstError;
    }

    public static void forceClose(ComponentSet components) throws MicroserviceError {    	
    	forceCloseComponents(components.getAllOrdered(), true);
    }

    public static void forceClose(
		ComponentSet components, boolean throwException
	) throws MicroserviceError {
    	forceCloseComponents(components.getAllOrdered(), throwException);
    }

}
