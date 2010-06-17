package jge3d.controller;

public class Command {
	private Object classInstance;
	private String methodToInvoke;
	
	public Command(Object _classInstance, String _methodToInvoke) {
		classInstance=_classInstance;
		methodToInvoke=_methodToInvoke;
	}
	
	public void setClassInstance(Object _classInstance) {
		classInstance=_classInstance;
	}
	
	public void setMethodToInvoke(String _methodToInvoke) {
		methodToInvoke=_methodToInvoke;
	}
	
	public Object getClassInstance() {
		return classInstance;
	}
	
	public String getMethodToInvoke(){
		return methodToInvoke;
	}
}
