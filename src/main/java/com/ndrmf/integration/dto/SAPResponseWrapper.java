package com.ndrmf.integration.dto;

import java.util.List;

public class SAPResponseWrapper<T> {
	private NestedD<T> d;
	
	public NestedD<T> getD() {
		return d;
	}

	public void setD(NestedD<T> d) {
		this.d = d;
	}
	
	public List<T> getResults(){
		if(this.d == null) {
			return null;
		}
		
		return this.d.getResults();
	}

	public static class NestedD<T>{
		private List<T> results;

		public List<T> getResults() {
			return results;
		}

		public void setResults(List<T> results) {
			this.results = results;
		}
	}
}