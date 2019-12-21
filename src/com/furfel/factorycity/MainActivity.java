package com.furfel.factorycity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import com.furfel.factorycity.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public GameMap gameMap;
	public GameInformation GameInfo = new GameInformation();
	public Drawing gameDraw;
	public boolean gfxmade=false, mapmade=false;
	public boolean timersenabled=true;
	
	public final int RESCOUNT=35;
	
	String mapname="0";
	
	public float camx,camy;
	public float updeltaX=0.0f, updeltaY=0.0f, motionX=0.0f, motionY=0.0f;
	public float previousX, previousY;
	public int sW,sH;
	public float scaleFactor=1.0f;
	
	public boolean btn_N, btn_W, btn_S, btn_E;
	public int hudmode=0;
	public boolean hudaccepted=false; public boolean putlock=true;
	
	public int selection=-1, selectionx=0, selectiony=0;
	public boolean putting=false; public int puttype=0;
	public int lastcharge=0;
	
	public boolean helpbtn=false; public boolean windowed=true;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		DisplayMetrics dm = new DisplayMetrics();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		sW=dm.widthPixels;
		sH=dm.heightPixels;
		
		TimerTask fps = new TimerTask(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if((gfxmade) && (mapmade) && (timersenabled))
					gameDraw.redraw(gameMap, camx, camy);
			}
		};
		
		TimerTask gupd = new TimerTask()
		{@Override public void run() {
			if((gameMap!=null) && (timersenabled)){
				for(int i=0;i<gameMap.w;i++)
					for(int j=0;j<gameMap.h;j++)
				{
					if(gameMap.objectsA[i][j].state==1)
					{
						if(gameMap.objectsA[i][j].time>0)
							gameMap.objectsA[i][j].time-=1;
						else {switch(gameMap.objectsA[i][j].type){
							case 1: {gameMap.resources[GameInfo.COAL]+=Math.round(Math.random()*10); gameMap.resources[GameInfo.SILICON]+=Math.round(Math.random()*5); gameMap.objectsA[i][j].type=2;} break;
							case 3: {gameMap.resources[GameInfo.WOOD]+=Math.round(Math.random()*6); gameMap.objectsA[i][j].type=2;} break;
							case 5: {gameMap.resources[GameInfo.MONEY]+=Math.round(Math.random()*100); gameMap.objectsA[i][j].type=0;} break;
							case 10: {gameMap.resources[GameInfo.WHEAT]+=Math.round(Math.random()*10); gameMap.objectsA[i][j].type=9;} break;
							case 13: {gameMap.resources[GameInfo.WATER]+=Math.round(Math.random())+1;}
						}
						gameMap.objectsA[i][j].state=0;
						}			
					}
					else if(gameMap.objectsA[i][j].state==4)
					{
						if(gameMap.objectsA[i][j].time>0)
							gameMap.objectsA[i][j].time-=1;
						else {gameMap.objectsA[i][j].type=0; gameMap.objectsA[i][j].state=0;}
					}
					else if(gameMap.objectsA[i][j].state==3)
					{
						if(gameMap.objectsA[i][j].time>0)
							gameMap.objectsA[i][j].time-=1;
						else {gameMap.objectsA[i][j].state=0;}
					}
					else if(gameMap.objectsA[i][j].state==2)
					{
						if(gameMap.objectsA[i][j].time>0)
							gameMap.objectsA[i][j].time-=1;
						else {
							switch(gameMap.objectsA[i][j].type)
							{
								case 4: {gameMap.resources[GameInfo.PLANKS]+=Math.round(Math.random()*gameMap.objectsA[i][j].workers);} break;
								case 6: {gameMap.resources[GameInfo.CHAIRS]+=Math.round(Math.random()*2*gameMap.objectsA[i][j].workers); gameMap.resources[GameInfo.TABLES]+=Math.round(Math.random()*gameMap.objectsA[i][j].workers);} break;
								case 8: {gameMap.resources[GameInfo.MONEY]+=Math.round(Math.random()*140*gameMap.objectsA[i][j].workers);} break;
								case 11: {gameMap.resources[GameInfo.FLOUR]+=Math.round(Math.random()*5*gameMap.objectsA[i][j].workers);} break;
								case 12: {gameMap.resources[GameInfo.MONEY]+=Math.round(Math.random()*50*gameMap.objectsA[i][j].workers);} break;
								case 15: {gameMap.resources[GameInfo.DIODE]+=Math.round(Math.random()*20)*gameMap.objectsA[i][j].workers; gameMap.resources[GameInfo.RESISTOR10K]+=Math.round(Math.random()*60)*gameMap.objectsA[i][j].workers;gameMap.resources[GameInfo.RESISTOR100]+=Math.round(Math.random()*60)*gameMap.objectsA[i][j].workers;gameMap.resources[GameInfo.CAPACITOR10N]+=Math.round(Math.random()*40)*gameMap.objectsA[i][j].workers;gameMap.resources[GameInfo.CAPACITOR10U]+=Math.round(Math.random()*40)*gameMap.objectsA[i][j].workers;gameMap.resources[GameInfo.LED]+=Math.round(Math.random()*20)*gameMap.objectsA[i][j].workers;gameMap.resources[GameInfo.TRANSISTORN]+=Math.round(Math.random()*10)*gameMap.objectsA[i][j].workers;gameMap.resources[GameInfo.TRANSISTORP]+=Math.round(Math.random()*10)*gameMap.objectsA[i][j].workers;}
							}
							gameMap.objectsA[i][j].state=0;
						}
					}
				}
				int tx=(int)Math.round(Math.random()*(gameMap.w-2))+1,ty=(int)Math.round(Math.random()*(gameMap.h-2))+1;
				if(gameMap.objectsA[tx][ty].type==0)
					{if(Math.random()>0.9)gameMap.objectsA[tx][ty].type=3;
					else if((Math.random()<0.1)&&(Math.random()>0.9))gameMap.objectsA[tx][ty].type=5;}
				else if(gameMap.objectsA[tx][ty].type==9)
					{if(Math.random()<0.5)gameMap.objectsA[tx][ty].type=10;
					}
			}
			}	
		};
		
		new Timer().scheduleAtFixedRate(fps, 100, 50);
		new Timer().scheduleAtFixedRate(gupd, 1000, 1000);
		
		setContentView(R.layout.menu_activity);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void makeTestGame()
	{
		EditText editText1 = (EditText) findViewById(R.id.editTextW);
		EditText editText2 = (EditText) findViewById(R.id.editTextH);
		gameMap = new GameMap(Integer.parseInt(editText1.getText().toString()),Integer.parseInt(editText2.getText().toString()));
		/*gameMap.w=Integer.parseInt(editText1.getText().toString());
		gameMap.h=Integer.parseInt(editText2.getText().toString());*/
		for(int i=0;i<gameMap.h;i++)
			for(int j=0;j<gameMap.w;j++)
			{
				if(Math.random()<0.7 && Math.random()>0.4)
				{
					gameMap.objectsA[j][i].type=3;
				}
				else if(Math.random()>0.7)
				{
					gameMap.objectsA[j][i].type=1;
				}
			}
		mapmade=true;
	}
	
	public void begingame(View v)
	{
		/*Date date= new Date();
		mapname=Long.toString(date.getTime());*/
		makeTestGame();
		if(!gfxmade)
			{gameDraw=new Drawing(this,sW,sH); gfxmade=true;}
		windowed=false;
		setContentView(gameDraw);
	}
	
	public void NewGame(View v)
	{
		setContentView(R.layout.newgame_layout);
	}
	
	public void SelectObject(float fx, float fy)
	{
		btn_N=false; btn_E=false; btn_W=false; btn_S=false;
		int ix=(int)Math.round(Math.floor(fx/128.0f*scaleFactor));
		int iy=(int)Math.round(Math.floor(fy/96.0f*scaleFactor));
		float rx=fx-ix*128.0f*scaleFactor,ry=fy-iy*96.0f*scaleFactor; int ax=0,ay=0;
		int o=-1; int l=0; int selectedstate=0; int seltype=0;
		//* selection = arr_index; seltype = type; selectedstate = state
		iy*=2;
		if((rx<64.0f*scaleFactor) && (ry<48.0f*scaleFactor) && (ry<(-0.75f*rx+48.0f*scaleFactor)))
			{ax=-1;ay=-1;}
		else if((rx<64.0f*scaleFactor) && (ry>48.0f*scaleFactor) && (ry>(0.75f*rx+48.0f*scaleFactor)) )
			{ax=-1;ay=1;}
		else if((rx>64.0f*scaleFactor) && (ry<48.0f*scaleFactor) && (ry<(0.75f*rx-48.0f*scaleFactor)))
			{ax=0;ay=-1;}
		else if((rx>64.0f*scaleFactor) && (ry>48.0f*scaleFactor) && (ry>(-0.75f*rx+144.0f*scaleFactor)))
			{ax=0;ay=1;}
		ix+=ax; iy+=ay;
		/*for(GameObject obj:gameMap.objects)
			{
				if( (ix==obj.x1) && (iy==obj.y1))
				{
					o=l; selectedstate=obj.state; seltype=obj.type; break;
				}
				l++;
			}*/
		if((ix>=0) && (ix<gameMap.w) && (iy>=0) && (iy<gameMap.h))
		{
		seltype=gameMap.objectsA[ix][iy].type;
		if(seltype!=0)
		{selection=seltype;
		selectedstate=gameMap.objectsA[ix][iy].state;}
		else selection=-1;
		//selection=o;
		selectionx=ix; selectiony=iy;
		gameDraw.selectionx=ix; gameDraw.selectiony=iy;
		gameDraw.selection=selection; gameDraw.selectedstate=selectedstate;
		gameDraw.exploitable=GameInfo.exploitable[seltype];
		gameDraw.workable=GameInfo.workable[seltype];
		btn_N=true;
		if( (GameInfo.exploitable[seltype]) || (GameInfo.workable[seltype]) )
			btn_E=true;
		if( ((GameInfo.workable[seltype])) ) {btn_S=true; gameDraw.selectionworkers=gameMap.objectsA[selectionx][selectiony].workers;gameDraw.satisfied=AreNeedsSatisfied(seltype);}
		gameDraw.btn_E=btn_E;
		gameDraw.btn_N=btn_N;
		gameDraw.btn_S=btn_S;
		gameDraw.btn_W=btn_W;
		if(hudmode==2){if(selection==-1) {gameDraw.notok=true; putlock=false;} else {gameDraw.notok=false; putlock=true;}}
		if(putting) {gameDraw.putx=selectionx; gameDraw.puty=selectiony; btn_E=false; btn_N=false; btn_W=false; btn_S=false;}
		}
		//Log.d("FC","SELECTED:[[["+Integer.toString(ix)+";"+Integer.toString(iy)+"]]];AAAAA"+Integer.toString(o)+";rx="+Float.toString(rx)+";ry="+Float.toString(ry));
	}
	
	public void ExploitObject()
	{
		int type=0; boolean workable=false; int cost=0,time=0;
		if(selection!=-1)
			{
				if(gameMap.objectsA[selectionx][selectiony].state==0){
					type=gameMap.objectsA[selectionx][selectiony].type;
					workable=GameInfo.workable[type];
					if(workable) {
						if(AreNeedsSatisfied(type))
							{
								ChargeNeeds(type);
								time=GameInfo.worktime[type];
								gameMap.objectsA[selectionx][selectiony].state=2;
								gameMap.objectsA[selectionx][selectiony].time=time;
							}
					}
					else {
						cost=GameInfo.exploitcost[type];
						time=GameInfo.exploittime[type];
						if(gameMap.resources[0]>=cost)
							{
								gameMap.objectsA[selectionx][selectiony].state=1;
								gameMap.objectsA[selectionx][selectiony].time=time;
								gameMap.resources[0]-=cost;
							}
						}
					}
				}
				selection=-1;
				gameDraw.selection=-1;
	}
	
	public boolean AreNeedsSatisfied(int type)
	{
		boolean are=true;
			for(int i=0;i<=RESCOUNT;i++)
				if(gameMap.resources[i]<GameInfo.needs[type][i])
					are=false;
		return are;
	}
	
	public void ChargeNeeds(int type)
	{
		for(int i=0;i<=RESCOUNT;i++)
			gameMap.resources[i]-=GameInfo.needs[type][i];
	}
	
	public void DestroyObject()
	{
		int cost=0,dtime=0, otype=0;
		if(selection!=-1)
		{
			if(gameMap.objectsA[selectionx][selectiony].state==0){
				otype=gameMap.objectsA[selectionx][selectiony].type;
				cost=GameInfo.destroycost[otype];
				dtime=GameInfo.destroytime[otype];
				if(gameMap.resources[0]>=cost){gameMap.objectsA[selectionx][selectiony].state=4; gameMap.objectsA[selectionx][selectiony].time=dtime; gameMap.resources[0]-=cost;}
			}
			selection=-1;
			gameDraw.selection=-1;
		}
	}
	
	public void putObject()
	{
		GameObject obj=new GameObject();
		if((selection==-1) && (putting) && (hudaccepted))
		{
			//obj.x1=selectionx;
			//obj.y1=selectiony;
			//obj.type=puttype;
			//obj.time=GameInfo.buildtime[puttype];
			//obj.state=3;
			//gameMap.objects.add(obj);
			gameMap.objectsA[selectionx][selectiony].type=puttype;
			gameMap.objectsA[selectionx][selectiony].state=3;
			gameMap.objectsA[selectionx][selectiony].time=GameInfo.buildtime[puttype];
			gameMap.objectsA[selectionx][selectiony].workers=1;
			putting=false; gameDraw.putting=false; putlock=false;
			Log.d("PutObject","putted");
		}
		hudaccepted=false;/*else if(gameMap.objects.get(selection).type==0)
			{
				gameMap.objects.get(selection).type=puttype;
				gameMap.objects.get(selection).time=GameInfo.buildtime[puttype];
				gameMap.objects.get(selection).state=3;
				putting=false; gameDraw.putting=false;
				Log.d("PutObject","putted2");
			}*/
			Log.d("PutObject","NOT putted");
	}
	
	public void buybuilding(View v)
	{
		switch(v.getId())
			{
				case R.id.icontimber_yard: case R.id.labeltimber_yard:
				{payObject(4);} break;
				case R.id.iconjoinery: case R.id.labeljoinery:
				{payObject(6);} break;
				case R.id.iconroad: case R.id.labelroad:
				{payObject(7);} break;
				case R.id.iconfurnitureshop: case R.id.labelfurnitureshop:
				{payObject(8);} break;
				case R.id.iconwheatfield: case R.id.labelwheatfield:
				{payObject(9);} break;
				case R.id.rlwindmill:
				{payObject(11);} break;
				case R.id.rlbakery:
				{payObject(12);} break;
				case R.id.rlwell:
				{payObject(13);} break;
				case R.id.rlstreetlamp:
				{payObject(14);} break;
				case R.id.rlelectronicp:
				{payObject(15);} break;
			}
		windowed=false;
		setContentView(gameDraw);
	}
	
	public void payObject(int type)
	{
		int cost=GameInfo.buildcost[type];
		if(gameMap.resources[0]>=cost) {
			lastcharge=cost;
			gameMap.resources[0]-=cost;
			hudmode=2; gameDraw.hudmode=2;
			putting=true; putlock=true; gameDraw.notok=false; puttype=type; gameDraw.putting=true; gameDraw.puttype=type;
			SelectObject(sW/2+camx,sH/2+camy);
		}
	}
	
	public void addWorker()
	{
		if(selection!=-1)
			if(GameInfo.workable[selection])
			{
				int cost = GameInfo.workercost[selection];
				if((gameMap.resources[0]>=cost) && (gameMap.objectsA[selectionx][selectiony].state==0))
				{
					gameMap.resources[0]-=cost;
					gameMap.objectsA[selectionx][selectiony].workers+=1;
				}
			}
		selection=-1;
		gameDraw.selection=-1;
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		float currentX = event.getX(); float currentY = event.getY();
		float deltaX, deltaY;
		super.onTouchEvent(event);
		if(!windowed){
		switch(event.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				{
					motionX=event.getX(); motionY=event.getY();
				} break;
			case MotionEvent.ACTION_UP:
				{
					if( (Math.abs(motionX-event.getX())<3) && (Math.abs(motionY-event.getY())<3) )
						{
						if((hudmode==0) && (event.getX()<sW) && (event.getX()>sW-128*scaleFactor) && (event.getY()>0) && (event.getY()<128*scaleFactor))
							{
								hudmode=1; gameDraw.hudmode=1;
							}
						else if((hudmode==1) && ( ((event.getX()<sW) && (event.getX()>sW-128*scaleFactor) && (event.getY()>0) && (event.getY()<128*scaleFactor))  || ((event.getX()<sW) && (event.getX()>sW-128*scaleFactor) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH)) || ((event.getX()<128*scaleFactor) && (event.getX()>0) && (event.getY()>0) && (event.getY()<128*scaleFactor)) || ((event.getX()<128*scaleFactor) && (event.getX()>0) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH)) ) )
							{
								if((event.getX()<sW) && (event.getX()>sW-128*scaleFactor) && (event.getY()>0) && (event.getY()<128*scaleFactor))
									{hudmode=0; gameDraw.hudmode=0;}
								else if((event.getX()<sW) && (event.getX()>sW-128*scaleFactor) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH))
									{hudmode=0; gameDraw.hudmode=0; windowed=true; setContentView(R.layout.buybuilding);}
								else if((event.getX()<128*scaleFactor) && (event.getX()>0) && (event.getY()>0) && (event.getY()<128*scaleFactor))
									{hudmode=0; gameDraw.hudmode=0; showResources();}
								else if((event.getX()<128*scaleFactor) && (event.getX()>0) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH))
									{hudmode=0; gameDraw.hudmode=0; prepareLibList();}
							}
						else if((hudmode==2) && ( ((event.getX()<sW) && (event.getX()>sW-128*scaleFactor) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH)) || ((event.getX()<128.0f) && (event.getX()>0) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH)) ) ) {
							if((event.getX()<sW) && (event.getX()>sW-128*scaleFactor) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH))
								{hudaccepted=true; putObject(); if(!putlock){hudmode=0; gameDraw.hudmode=0;}}
							else if((event.getX()<128.0f) && (event.getX()>0) && (event.getY()>sH-128*scaleFactor) && (event.getY()<sH))
								{gameMap.resources[0]+=lastcharge; putting=false; gameDraw.putting=false; hudmode=0; gameDraw.hudmode=0;}
						}
					else
						{
						if(
									( (selection!=-1) && (selectiony%2==0) 
											&& (event.getX()>(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor)) && (event.getX()<(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+96.0f*scaleFactor))
											&& (event.getY()>selectiony*48.0f*scaleFactor-camy-96.0f*scaleFactor) && (event.getY()<selectiony*48.0f*scaleFactor-camy-96.0f*scaleFactor+96.0f*scaleFactor)
									)
									|| ((selection!=-1) && (selectiony%2!=0) 
											&& (event.getX()>(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+64.0f*scaleFactor)) && (event.getX()<(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+96.0f*scaleFactor+64.0f*scaleFactor))
											&& (event.getY()>selectiony*48.0f*scaleFactor-camy-96.0f*scaleFactor) && (event.getY()<selectiony*48.0f*scaleFactor-camy-96.0f*scaleFactor+96.0f*scaleFactor) 
									) 
								){if(btn_N) {if(!putting)DestroyObject();} else {SelectObject(event.getX()+camx,event.getY()+camy);if(putting) putObject();} }
							else
								if(
										( (selection!=-1) && (selectiony%2==0) 
												&& (event.getX()>(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor)) && (event.getX()<(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+96.0f*scaleFactor))
												&& (event.getY()>selectiony*48.0f*scaleFactor-camy) && (event.getY()<selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor)
										)
										|| ((selection!=-1) && (selectiony%2!=0) 
												&& (event.getX()>(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+64.0f*scaleFactor)) && (event.getX()<(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+96.0f*scaleFactor+64.0f*scaleFactor))
												&& (event.getY()>selectiony*48.0f*scaleFactor-camy) && (event.getY()<selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor) 
										) 
									){if(btn_E) {if(!putting)ExploitObject();}else {SelectObject(event.getX()+camx,event.getY()+camy); if(putting) putObject();}}
							else
								if(
										( (selection!=-1) && (selectiony%2==0) 
												&& (event.getX()>(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor)) && (event.getX()<(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+96.0f*scaleFactor))
												&& (event.getY()>selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor) && (event.getY()<selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor+96.0f*scaleFactor)
										)
										|| ((selection!=-1) && (selectiony%2!=0) 
												&& (event.getX()>(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+64.0f*scaleFactor)) && (event.getX()<(selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+96.0f*scaleFactor+64.0f*scaleFactor))
												&& (event.getY()>selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor) && (event.getY()<selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor+96.0f*scaleFactor) 
										) 
									){if(btn_S) {addWorker(); Log.d("btns","southon");} else {Log.d("btns","southoff");SelectObject(event.getX()+camx,event.getY()+camy);if(putting) putObject();} }
							else {SelectObject(event.getX()+camx,event.getY()+camy); if(putting) putObject();}
						}
						}
				} break;
			case MotionEvent.ACTION_MOVE:
				{
					deltaX=currentX-previousX;
					deltaY=currentY-previousY;
					camx-=deltaX; camy-=deltaY;
				} break;
		}
		previousX=currentX;
		previousY=currentY;
		}
		return true;	
	}
	
	@Override
	public boolean onKeyDown(int Keycode, KeyEvent event)
	{
		if(event.getKeyCode()==KeyEvent.KEYCODE_VOLUME_DOWN)
		{
			if(helpbtn){setContentView(R.layout.object_librarylist);}
			else{
			Log.d("Key","CHEATS");
			//for(int i=0;i<=RESCOUNT;i++) gameMap.resources[i]+=100;
			gameMap.resources[0]+=100000;}
		}
		else if(event.getKeyCode()==KeyEvent.KEYCODE_VOLUME_UP)
		{
			if(helpbtn)
			{
				prepareLibList();
			}
			else
			{
				for(int i=0;i<gameMap.w;i++)
					for(int j=0;j<gameMap.h;j++)
						if(gameMap.objectsA[i][j].time>5) gameMap.objectsA[i][j].time=5; 
			}
		}
		else if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
		{
			if(!windowed) {windowed=true; timersenabled=false; setContentView(R.layout.pausemenu);}
		}
		else if(event.getKeyCode()==KeyEvent.KEYCODE_MENU)
			helpbtn=true;
		else super.onKeyDown(Keycode, event);
		return true;
	}
	
	public void saveMap()
	{
		Byte buf,kbuf,xbuf; int keyp=0; String key="t3hfunh4sb33nd0ubl3d";
		try
		{
			FileOutputStream fostream = new FileOutputStream(new File(getCacheDir(),"s1"));
			DataOutputStream dostream = new DataOutputStream(fostream);
			dostream.writeInt(gameMap.w);
			dostream.writeInt(gameMap.h);
			for(int i=0;i<gameMap.h;i++)
				for(int j=0;j<gameMap.w;j++)
					{
						dostream.writeInt(gameMap.objectsA[j][i].state);
						dostream.writeInt(gameMap.objectsA[j][i].time);
						dostream.writeInt(gameMap.objectsA[j][i].type);
						dostream.writeInt(gameMap.objectsA[j][i].workers);
						dostream.writeInt(gameMap.objectsA[j][i].x1);
						dostream.writeInt(gameMap.objectsA[j][i].y1);
					}
			for(int i=0;i<=RESCOUNT;i++)
				{
					dostream.writeInt(gameMap.resources[i]);
				}
			dostream.writeFloat(camx);
			dostream.writeFloat(camy);
			dostream.close();
			FileInputStream fistream = new FileInputStream(new File(getCacheDir(),"s1"));
			DataInputStream distream = new DataInputStream(fistream);
			OutputStream ostream = openFileOutput(mapname,Context.MODE_PRIVATE);
			dostream = new DataOutputStream(ostream);
			dostream.writeByte(1); //version 1
			while(distream.available()>0)
				{
					buf=distream.readByte();
					if(keyp<key.length()-1) keyp++; else keyp=0;
					kbuf=(byte)key.charAt(keyp);
					xbuf=(byte) (kbuf^buf);
					dostream.writeByte(xbuf);
				}
			distream.close(); dostream.close();
			File delcache=new File(getCacheDir(),"s1");
			if(delcache!=null) {delcache.delete();}
		}
		catch(IOException e){}
	}
	
	public void saveMapMeta()
	{
		String meta;
		try
		{
			OutputStream ostream = openFileOutput(mapname=".info",Context.MODE_PRIVATE);
			DataOutputStream dostream = new DataOutputStream(ostream);
			meta="Size: "+Integer.toString(gameMap.w)+"x"+Integer.toString(gameMap.h)+"\n Money:"+Integer.toString(gameMap.resources[0]);
			dostream.writeChars(meta);
			dostream.close();
		} catch(IOException e) {}
	}
	
	public void loadMap()
	{
		Byte buf,kbuf,xbuf; int keyp=0; String key="t3hfunh4sb33nd0ubl3d"; int w,h; byte version=0;
		try
		{
			FileOutputStream fostream = new FileOutputStream(new File(getCacheDir(),"l1"));
			DataOutputStream dostream = new DataOutputStream(fostream);
			InputStream istream = openFileInput(mapname);
			DataInputStream distream = new DataInputStream(istream);
			version=distream.readByte();
			Log.d("MapLoader","Map version: "+Integer.toString(version));
			while(distream.available()>0)
				{
					buf=distream.readByte();
					if(keyp<key.length()-1) keyp++; else keyp=0;
					kbuf=(byte)key.charAt(keyp);
					xbuf=(byte) (kbuf^buf);
					dostream.writeByte(xbuf);
				}
			distream.close(); dostream.close();
			FileInputStream fistream = new FileInputStream(new File(getCacheDir(),"l1"));
			distream = new DataInputStream(fistream);
			//distream.readByte();
			w=distream.readInt();
			h=distream.readInt();
			Log.d("MapLoader","W:"+w+" H:"+h);
			gameMap=new GameMap(w,h);
			for(int i=0;i<gameMap.h;i++)
				for(int j=0;j<gameMap.w;j++)
					{
						gameMap.objectsA[j][i].state=distream.readInt();
						gameMap.objectsA[j][i].time=distream.readInt();
						gameMap.objectsA[j][i].type=distream.readInt();
						gameMap.objectsA[j][i].workers=distream.readInt();
						gameMap.objectsA[j][i].x1=distream.readInt();
						gameMap.objectsA[j][i].y1=distream.readInt();
					}
			for(int i=0;i<=RESCOUNT;i++)
				{
					gameMap.resources[i]=distream.readInt(); Log.d("MapLoader","res"+i+":"+gameMap.resources[i]);
				}
			camx=distream.readFloat();
			camy=distream.readFloat();
			distream.close();
			//File delcache=new File(getCacheDir(),"l1");
			//if(delcache!=null) {delcache.delete();}
		}
		catch(IOException e){Log.d("MapLoader","Exception, ver: "+Integer.toString(version)+" signal:"+e.getMessage());}
	}
	
	public void saveMapSpawner()
	{
		timersenabled=false; windowed=true;
		AlertDialog.Builder dialogb = new AlertDialog.Builder(this);
		dialogb.setTitle("Saving map");
		View saving = null;
		saving = getLayoutInflater().inflate(R.layout.savingmap, null);
		dialogb.setView(saving);
		final AlertDialog dialog = dialogb.create();
		dialog.show();
		saveMap();
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				dialog.cancel();}
			}, 5000);
		Toast.makeText(this, "Saved.", Toast.LENGTH_SHORT).show();
		//timersenabled=true;
	}
	
	public void loadMapSpawner(View v)
	{
		timersenabled=false; windowed=true;
		gameMap=null;
		AlertDialog.Builder dialogb = new AlertDialog.Builder(this);
		dialogb.setTitle("Loading map");
		View saving = null;
		saving = getLayoutInflater().inflate(R.layout.savingmap, null);
		dialogb.setView(saving);
		final AlertDialog dialog = dialogb.create();
		dialog.show();
		loadMap();
		new Timer().schedule(new TimerTask(){
			@Override
			public void run() {
				dialog.cancel();}
			}, 1000);
		Toast.makeText(this, "Loaded.", Toast.LENGTH_SHORT).show();
		if(!gfxmade)
			{gameDraw=new Drawing(this,sW,sH); gfxmade=true;}
		timersenabled=true; windowed=false; setContentView(gameDraw);
		mapmade=true;
	}
	
	public void showResources()
	{
		String resText="";
		setContentView(R.layout.myresources);
		for(int i=0;i<=RESCOUNT;i++)
			{
				if(gameMap.resources[i]>0)
					resText+=GameInfo.resource_names[i]+": "+gameMap.resources[i]+"\n";
			}
		TextView resView = (TextView) findViewById(R.id.textResources);
		resView.setText(resText);
		windowed=true;
	}
	
	public void hideResources(View v)
	{
		if(v.getId()==R.id.imageCancelLib){
			prepareLibList();
		}else{
		windowed=false; setContentView(gameDraw);}
	}
	
	public void objectInfo(int t)
	{
		setContentView(R.layout.object_library);
		ImageView iv = (ImageView) findViewById(R.id.imageView1);
		iv.setImageResource(GameInfo.graphics[t]);
		TextView txtv = (TextView) findViewById(R.id.textView1);
		txtv.setText(GameInfo.objectname[t]);
		String q="";
		if(GameInfo.workable[t])
			q+="Additional worker: "+GameInfo.workercost[t]+"$\n"+"Work time: "+GameInfo.worktime[t]+"\n\n";
		for(int i=0;i<=RESCOUNT;i++)
			{
				if(GameInfo.needs[t][i]!=0)
					q+=GameInfo.resource_names[i]+": "+gameMap.resources[i]+"/"+GameInfo.needs[t][i]+"\n";
			}
		txtv = (TextView) findViewById(R.id.textView2);
		txtv.setText(q);
	}
	
	public void prepareLibList()
	{
		windowed=true;
		setContentView(R.layout.object_librarylist);
		ListView listView = (ListView) findViewById(R.id.libraryList);
		String[] namez = new String[GameInfo.objectname.length];
		for(int i=0;i<GameInfo.objectname.length;i++)
			if(GameInfo.objectname[i]!=null)
				namez[i]=GameInfo.objectname[i];
			else namez[i]=" ";
		//ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, namez);
		LibraryListAdapter adapter = new LibraryListAdapter(this,namez);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				objectInfo(arg2);
			}
		});
	}
	
	public void setSlot(View v)
	{
		switch(v.getId()){
		case R.id.radio0: mapname="0"; break;
		case R.id.radio1: mapname="1"; break;
		case R.id.radio2: mapname="2"; break;
		}
	}
	
	public void pauseclick(View v)
	{
		switch(v.getId())
		{
			case R.id.pause_continue: {windowed=false; timersenabled=true; setContentView(gameDraw);} break;
			case R.id.pause_save: {saveMapSpawner();} break;
			case R.id.pause_exit: {timersenabled=false; windowed=true; setContentView(R.layout.menu_activity);}
		}
	}
	
	@Override
	public boolean onKeyUp(int Keycode, KeyEvent event)
	{
		if(event.getKeyCode()==KeyEvent.KEYCODE_MENU)
			helpbtn=false;
		return true;
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		timersenabled=false;
		windowed=true;
		setContentView(R.layout.pausemenu);
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		windowed=true;
	}

}
