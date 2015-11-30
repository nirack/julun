package com.julun.datas.beans;

import java.util.ArrayList;
import java.util.List;

public class Node {
	private Integer nodeId;
	private String nodeName;
	private List<Node> children;
	
	public Node() {
		super();
	}

	public Node(int id, String name) {
		this.nodeId = id;
		this.nodeName = name;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}
	public String getNodeName() {
		return nodeName;
	}
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}
	public List<Node> getChildren() {
		return children;
	}
	public void setChildren(List<Node> children) {
		this.children = children;
	}

	public void addChild(Node child) {
		if(children == null){
			children = new ArrayList<>();
		}
		children.add(child);
	}
	
}
