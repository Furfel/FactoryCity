package com.furfel.factorycity;

public class GameInformation {
	
	public boolean[] exploitable = new boolean[30];
	
	public boolean[] workable = new boolean[30];
	
	public int[] destroycost = new int[30];
	
	public int[] destroytime = new int[30];
	
	public int[] exploittime = new int[30];
	
	public int[] exploitcost = new int[30];
	
	public String[] objectname = new String[30];
	
	public int[] buildtime = new int[30];
	
	public int[] buildcost = new int[30];
	
	public int[] worktime = new int[30];
	
	public int[] workercost = new int[30];
	
	public int[][] needs = new int[30][50];
	
	public static final int MONEY=0;
	public static final int WOOD=1;
	public static final int SILICON=2;
	public static final int COAL=3;
	public static final int PLANKS=4;
	public static final int CHAIRS=5;
	public static final int TABLES=6;
	public static final int WHEAT=7;
	public static final int FLOUR=8;
	public static final int WATER=9;
	public static final int GERMANIUM=10;
	public static final int CHLORIDE=11;
	public static final int STEEL=12;
	public static final int COPPER=13;
	public static final int ZINC=14;
	public static final int CARBON=15;
	public static final int ALUMINIUM=16;
	public static final int NICKEL=17;
	public static final int RESISTOR10K=18;
	public static final int RESISTOR100=19;
	public static final int CAPACITOR10U=20;
	public static final int CAPACITOR10N=21;
	public static final int TRANSISTORN=22;
	public static final int TRANSISTORP=23;
	public static final int DIODE=24;
	public static final int LED=25;
	public static final int CPU=26;
	public static final int HDD=27;
	public static final int LAPTOP=28;
	public static final int PC=29;
	public static final int TABLET=30;
	public static final int SMARTPHONE=31;
	public static final int MOUSE=32;
	public static final int KEYBOARD=33;
	public static final int MILK=34;
	public static final int SUGAR=35;
	public static final int SILVER=36;
	public static final int GOLD=37;
	
	public static final String[] resource_names={
			"Money",
			"Wood",
			"Silicon",
			"Coal",
			"Planks",
			"Chairs",
			"Tables",
			"Wheat",
			"Flour",
			"Water",
			"Germanium",
			"Chloride",
			"Steel",
			"Copper",
			"Zinc",
			"Carbon",
			"Aluminium",
			"Nickel",
			"10K Ohm resistor",
			"100 Ohm resistor",
			"10uF capacitor",
			"10nF capacitor",
			"NPN transistor",
			"PNP transistor",
			"Diode",
			"Light Emitting Diode",
			"CPU",
			"Hard drive",
			"Laptop",
			"PC",
			"Tablet",
			"Smartphone",
			"Mouse",
			"Keyboard",
			"Milk",
			"Sugar",
			"Silver",
			"Gold"};
	
	/*public final byte[] resource_categories={
			0, 0, 1, 0, 
	};*/
	
	public static final int[] graphics = {
			R.drawable.field,
			R.drawable.stones,
			R.drawable.sand,
			R.drawable.forest,
			R.drawable.timber_yard,
			R.drawable.treasure,
			R.drawable.joinery,
			R.drawable.road,
			R.drawable.furnitureshop,
			R.drawable.wheatfield,
			R.drawable.wheatfieldgrown,
			R.drawable.windmill,
			R.drawable.bakery,
			R.drawable.well,
			R.drawable.street_lamp,
			R.drawable.electronicparts,
			R.drawable.cpufactory,
			R.drawable.hddfactory,
			R.drawable.mousekeyboard,
			R.drawable.pcshop,
			R.drawable.pcfactory
	};
	
	public void addObject(int index, String s_name, boolean b_exploitable, int i_destroycost, int i_destroytime, int i_exploittime, int i_exploitcost, int i_buildtime, int i_buildcost)
	{
		exploitable[index]=b_exploitable;
		workable[index]=false;
		destroycost[index]=i_destroycost;
		destroytime[index]=i_destroytime;
		exploittime[index]=i_exploittime;
		exploitcost[index] = i_exploitcost;
		buildtime[index] = i_buildtime;
		buildcost[index] = i_buildcost;
		objectname[index] = s_name;
	}
	
	public void addFactoryObject(int index, String s_name, boolean b_workable, int i_destroycost, int i_destroytime, int i_buildtime, int i_buildcost, int i_worktime, int i_workercost)
	{
		exploitable[index]=false;
		workable[index]=b_workable;
		destroycost[index]=i_destroycost;
		destroytime[index]=i_destroytime;
		exploittime[index]= 0;
		exploitcost[index] = 0;
		buildtime[index] = i_buildtime;
		buildcost[index] = i_buildcost;
		worktime[index] = i_worktime;
		workercost[index] = i_workercost;
		objectname[index] = s_name;
	}
	
	public void setFactoryNeed(int index, int need, int count)
	{
		needs[index][need]=count;
	}
	
	public GameInformation()
		{
			/*index,name,exploitable,workable,dcost,dtime,etime,ecost,btime,bcost*/
			/*index,name,workable,dcost,dtime,btime,bcost,wtime,wcost*/
			addObject(0,"Field",false,0,0,0,0,0,0);
			addObject(1,"Rocks",true,30,15,15,30,0,0);
			addObject(2,"Sand",false,15,10,0,0,0,0);
			addObject(3,"Forest",true,40,12,15,50,0,0);
			addFactoryObject(4,"Timber Yard",true,50,10,100,200,100,200);
			setFactoryNeed(4,WOOD,5);
			setFactoryNeed(4,MONEY,10);
			addObject(5,"Treasure",true,0,1,3,0,0,0);
			addFactoryObject(6,"Joinery",true,40,20,140,400,200,230);
			setFactoryNeed(6,PLANKS,10);
			setFactoryNeed(6,MONEY,50);
			addFactoryObject(7,"Road",false,20,10,7,40,0,0);
			addFactoryObject(8,"Furniture Shop",true,40,20,350,1500,500,600);
			setFactoryNeed(8,CHAIRS,5);
			setFactoryNeed(8,TABLES,2);
			setFactoryNeed(8,MONEY,70);
			addFactoryObject(9,"Wheat field",false,10,5,20,50,0,0);
			addObject(10,"Grown wheat field",true,10,10,20,5,0,0);
			addFactoryObject(11,"Windmill",true,50,20,150,400,130,40);
			setFactoryNeed(11,WHEAT,10);
			setFactoryNeed(11,MONEY,10);
			addFactoryObject(12,"Bakery",true,60,20,260,800,400,400);
			setFactoryNeed(12,FLOUR,20);
			setFactoryNeed(12,MONEY,55);
			setFactoryNeed(12,WATER,5);
			addObject(13,"Well",true,30,40,140,10,300,70);
			addObject(14,"Street lamp",false,10,5,0,0,5,60);
			addFactoryObject(15,"Electronic parts factory",true,120,100,800,6000,700,1900);
			setFactoryNeed(15,SILICON,20);
			setFactoryNeed(15,CHLORIDE,10);
			setFactoryNeed(15,STEEL,25);
			setFactoryNeed(15,COPPER,20);
			setFactoryNeed(15,CARBON,20);
			setFactoryNeed(15,GERMANIUM,10);
			setFactoryNeed(15,ALUMINIUM,20);
			setFactoryNeed(15,ZINC,20);
			setFactoryNeed(15,NICKEL,10);
			setFactoryNeed(15,GOLD,5);
			setFactoryNeed(15,SILVER,5);
			setFactoryNeed(15,MONEY,700);
			addFactoryObject(16,"Processor factory",true,300,120,1000,13000,1200,2000);
			setFactoryNeed(16,MONEY,800);
			setFactoryNeed(16,GOLD,20);
			setFactoryNeed(16,SILVER,30);
			setFactoryNeed(16,TRANSISTORN,20);
			setFactoryNeed(16,TRANSISTORP,20);
			setFactoryNeed(16,SILICON,100);
			addFactoryObject(17,"HDD factory",true,250,120,900,11000,1000,1900);
			setFactoryNeed(17,MONEY,800);
			setFactoryNeed(17,ALUMINIUM,100);
			setFactoryNeed(17,GOLD,10);
			setFactoryNeed(17,SILVER,15);
			setFactoryNeed(17,TRANSISTORN,3);
			setFactoryNeed(17,TRANSISTORP,2);
			addFactoryObject(18,"Mouse & keyboard factory",true,100,90,800,8000,900,1800);
			setFactoryNeed(18,MONEY,500);
			setFactoryNeed(18,TRANSISTORP,2);
			setFactoryNeed(18,TRANSISTORN,2);
			addFactoryObject(19,"Computer shop",true,90,90,600,5000,1000,1300);
			setFactoryNeed(19,MONEY,450);
			setFactoryNeed(19,PC,5);
			setFactoryNeed(19,LAPTOP,7);
			setFactoryNeed(19,MOUSE,10);
			setFactoryNeed(19,KEYBOARD,10);
			addFactoryObject(20,"Computer factory",true,90,90,700,4500,600,1200);
			setFactoryNeed(20,MONEY,700);
			setFactoryNeed(20,RESISTOR10K,10);
			setFactoryNeed(20,RESISTOR100,20);
			setFactoryNeed(20,CAPACITOR10U,10);
			setFactoryNeed(20,CAPACITOR10N,10);
			setFactoryNeed(20,LED,20);
			setFactoryNeed(20,DIODE,12);
			setFactoryNeed(20,CPU,12);
			setFactoryNeed(20,HDD,10);
		}
	
}
