package com.sky.config;

import com.ctrip.framework.apollo.Config;

/**
 * 
 * @author
 * 命名空间基类，带排序
 */
public class SortedNamespaces implements Comparable<SortedNamespaces> {
	private Config namespaceCfg;
	private String nameSpace;
	private int priority;
	public String getNameSpace() {
		return nameSpace;
	}
	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	
	public Config getNamespaceCfg() {
		return namespaceCfg;
	}
	public void setNamespaceCfg(Config namespaceCfg) {
		this.namespaceCfg = namespaceCfg;
	}
	@Override
	public int compareTo(SortedNamespaces otherNameSpace){
		return  this.getPriority() - otherNameSpace.getPriority();
	}
	
	
}
