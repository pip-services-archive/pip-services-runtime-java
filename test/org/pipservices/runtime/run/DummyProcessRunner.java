package org.pipservices.runtime.run;

public class DummyProcessRunner extends ProcessRunner {
	public DummyProcessRunner() {
		super(new DummyMicroservice());
	}

    public static void main(String[] args) {
        DummyProcessRunner runner = new DummyProcessRunner();
        try {
        	runner.runWithDefaultConfigFile(args, "config/config.yaml");
        } catch (Exception ex) {
        	System.err.println(ex);
        }
    }
}
