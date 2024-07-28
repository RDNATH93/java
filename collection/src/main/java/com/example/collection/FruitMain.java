package com.example.collection;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FruitMain {

	public static void main(String[] args) {
		
		//remove null and empty entries from list
		List<Fruit> fruits = Data.fruits.stream()
		.filter(fruit->fruit.name()!=null)
		.filter(fruit->fruit.size()!=null)
		.filter(fruit->!fruit.name().isBlank())
		.filter(fruit->!fruit.size().isBlank())
		.toList();
		
		System.out.println(fruits);
		
		//group by fruit size
		Map<String, List<Fruit>> groupBySize = fruits.stream().collect(Collectors.groupingBy(Fruit::size));
		System.out.println(groupBySize);

		//group by fruit name
		Map<String, List<Fruit>> groupByName = fruits.stream().collect(Collectors.groupingBy(Fruit::name));
		System.out.println(groupByName);
		
		//group by fruit name and size
		 Map<String, Map<String, Long>> groupByNameandSize = fruits.stream()
				 .collect(Collectors.groupingBy(Fruit::name,Collectors.groupingBy(Fruit::size,Collectors.counting())));
		System.out.println(groupByNameandSize);
		
	}

}


record Data() {
	static List<Fruit> fruits=Arrays.asList(
				new Fruit("Apple",""),
				new Fruit("Apple","Small"),
				new Fruit("Mango","Small"),
				new Fruit(null,"Medium"),
				new Fruit("Mango","Large"),
				new Fruit("Mango","Large"),
				new Fruit("Orange","Small"),
				new Fruit("Orange","Large"),
				new Fruit("Orange","Large"),
				new Fruit("Orange",null),
				new Fruit("Mango","")
			);
}
record Fruit(String name,String size) {}
