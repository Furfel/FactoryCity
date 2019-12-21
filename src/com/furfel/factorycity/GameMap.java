package com.furfel.factorycity;

import java.util.ArrayList;
import java.util.List;

public class GameMap {
	public int w,h;
	public List<GameObject> objects = new ArrayList<GameObject>();
	public GameObject[][] objectsA;
	//public int coal=2, silicon=2, money=1000, wood=5, planks=0,chairs=0,tables=0;
	
	public int[] resources = new int[50];
	
	public GameMap(int wi, int he)
	{
		w=wi; h=he;
		objectsA = new GameObject[w+1][h+1];
		for(int i=0;i<h;i++)
			for(int j=0;j<w;j++)
				objectsA[j][i]=new GameObject();
		resources[0]=8000;
	}
}
