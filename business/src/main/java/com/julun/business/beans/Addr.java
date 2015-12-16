package com.julun.business.beans;

import java.util.ArrayList;
import java.util.List;

public class Addr {
	
	private Integer addrId;
	private String addrName;
	private Integer addrPid;
	private List<Addr> children = new ArrayList<Addr>();

	public Integer getAddrId() {
		return addrId;
	}

	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
	}

	public String getAddrName() {
		return addrName;
	}

	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}

	public Integer getAddrPid() {
		return addrPid;
	}

	public void setAddrPid(Integer addrPid) {
		this.addrPid = addrPid;
	}

	public List<Addr> getChildren() {
		return children;
	}

	public void setChildren(List<Addr> children) {
		this.children = children;
	}

}
