package com.furfel.factorycity;

public class GameObject {
	public int type;
	/*
	 Types of objects:
	 	0 - nothing
	 	1 - stones
	 	2 - sand
	 	3 - forest
	 */
	public int time;
	public int money;
	public int workers;
	public int x1,y1,x2,y2;
	
	public int state=0;
	
	/*
	 * States of object
	 * 0 - idle
	 * 1 - exploiting
	 * 2 - working
	 * 3 - building
	 * 4 - destroying
	 * 5 - has money
	*/
}
