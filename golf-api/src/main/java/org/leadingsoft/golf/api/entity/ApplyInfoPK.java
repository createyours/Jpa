package org.leadingsoft.golf.api.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ApplyInfoPK implements Serializable{

	private static final long serialVersionUID = 1L;
		/**
	    * 募集id
	    */
	    private int roundserialno;
	    
	    /**
	     * 受付通番
	     */
	     private int regno;
	     
	     public ApplyInfoPK(){}

		public ApplyInfoPK(int roundserialno, int regno) {
			super();
			this.roundserialno = roundserialno;
			this.regno = regno;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + regno;
			result = prime * result + roundserialno;
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
			ApplyInfoPK other = (ApplyInfoPK) obj;
			if (regno != other.regno)
				return false;
			if (roundserialno != other.roundserialno)
				return false;
			return true;
		}

}
