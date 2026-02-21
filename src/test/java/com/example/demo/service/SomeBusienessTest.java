package com.example.demo.service;

import static org.junit.Assert.*;


import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

//Creating stub

class SomeDataServiceStub implements SumLogic{

	@Override
	public List<Integer> getAlldata() {
		// TODO Auto-generated method stub
		List<Integer> num = new ArrayList<>();
		num.add(1);
		num.add(2);
		num.add(3);
		return num;
	}
	
}

public class SomeBusienessTest {

	@Test
	public void calculate_test() {
		
		SomeBusieness impl = new SomeBusieness();
		impl.setService(new SomeDataServiceStub());
		List<Integer> numb = new ArrayList<>();
		numb.add(1);
		numb.add(2);
		numb.add(3);
		int result = impl.calculate();
		int exp= 6;
		assertEquals(exp,result);
		
	}

	

}
