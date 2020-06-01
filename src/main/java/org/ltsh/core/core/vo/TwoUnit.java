package org.ltsh.core.core.vo;

/**
 * 实体存储单元
 * 2017年9月19日
 * @param <E1>
 * @param <E2>
 */
public class TwoUnit<E1, E2> {

	private E1 one;
	private E2 two;
	
	public TwoUnit(E1 one, E2 two){
		this.one = one;
		this.two = two;
	}

	public E1 getOne() {
		return one;
	}

	public void setOne(E1 one) {
		this.one = one;
	}

	public E2 getTwo() {
		return two;
	}

	public void setTwo(E2 two) {
		this.two = two;
	}
	
}
