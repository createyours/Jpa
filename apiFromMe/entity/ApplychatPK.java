package org.leadingsoft.golf.api.entity;

import java.io.Serializable;


public class ApplychatPK implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
    * 募集id
    */
    private int roundserialno;
    /**
    * 表示順
    */
    private int sortorder;
    
    
    public ApplychatPK(){}
	public ApplychatPK(int roundserialno, int sortorder) {
		super();
		this.roundserialno = roundserialno;
		this.sortorder = sortorder;
	}
    
    
    
    
	public int getRoundserialno() {
		return roundserialno;
	}
	public void setRoundserialno(int roundserialno) {
		this.roundserialno = roundserialno;
	}
	public int getSortorder() {
		return sortorder;
	}
	public void setSortorder(int sortorder) {
		this.sortorder = sortorder;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + roundserialno;
		result = prime * result + sortorder;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApplychatPK other = (ApplychatPK) obj;
		if (roundserialno != other.roundserialno)
			return false;
		if (sortorder != other.sortorder)
			return false;
		return true;
	}
}
