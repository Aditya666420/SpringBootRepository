package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

public class SomeBusieness {
	
	
	SumLogic sb;
	public void setService(SumLogic sc) {
		this.sb=sc;
	}
	
	public int calculate() {
		List<Integer> arr = sb.getAlldata();
		int sum = arr.stream().mapToInt(Integer::intValue).sum();
		return sum;
		
	}

}
