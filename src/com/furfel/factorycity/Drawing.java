package com.furfel.factorycity;

import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.MaskFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class Drawing extends View {

	
	public float camx, camy;
	public GameMap gmap;
	public float scaleFactor=1.0f;
	
	public int selection=-1; public int selectionx, selectiony;
	public int frames=0;
	
	public int hudmode=0;
	
	public int screenW, screenH;
	
	public int silicon,coal,money,wood,planks,wheat;
	
	public boolean showbuttons=false; public int selectedstate;
	public boolean exploitable=false, workable=false; public int selectionworkers=0;
	public boolean putting=false; public int puttype=0; public int puty=0,putx=0;
	public boolean notok=false; public boolean satisfied=false;
	
	public boolean dialog=false;
	public String dialogtext="";
	
	public boolean sMile=false;
	public Bitmap bMile;
	public boolean sMileready=false;
	
	Bitmap emptyspace,bitmapselection,bit_exploiting,bit_workerscounter,bit_moneycounter,bit_timercounter;
	
	Bitmap b_exploit, b_delete, b_work, bd_exploit, bd_delete, bd_work, bd_addworker, b_addworker;
	Bitmap b_hud,b_nohud,b_shop,b_ok,b_notok,b_cancel,b_res,b_lib;
	
	public boolean btn_N, btn_W, btn_S, btn_E;
	
	Bitmap[] objs = new Bitmap[30];
	int[] calibobj = new int[30];
	Bitmap[] bit_destroying = new Bitmap[4];
	Bitmap[] bit_building = new Bitmap[4];
	
	Paint textpaint = new Paint();
	Paint timepaint = new Paint();
	Paint redpaint = new Paint();
	Paint counterpaint = new Paint();
	Paint dialogpaint = new Paint();
	Paint dialogtextpaint = new Paint();
	
	public Drawing(Context context, int sW, int sH) {
		super(context);
		scaleFactor = (float)(sW/800.0f);
		screenW=sW; screenH=sH;
		textpaint.setColor(Color.CYAN);
		textpaint.setTextSize(18.0f*scaleFactor);
		timepaint.setTextSize(16.0f*scaleFactor);
		timepaint.setColor(Color.MAGENTA);
		timepaint.setTextAlign(Align.CENTER);
		redpaint.setColor(Color.RED);
		counterpaint.setColor(Color.BLACK);
		counterpaint.setTextSize(24.0f*scaleFactor);
		counterpaint.setAntiAlias(true);
		counterpaint.setTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD));
		counterpaint.setTextAlign(Align.CENTER);
		
		dialogpaint.setColor(Color.argb(150, 150, 150, 150));
		dialogtextpaint.setColor(Color.WHITE);
		dialogtextpaint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
		dialogtextpaint.setTextSize(24.0f);
		dialogtextpaint.setTextAlign(Align.CENTER);
		
		setBackgroundColor(Color.rgb(0, 40, 40));
		emptyspace = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.empty),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		bitmapselection = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.selection),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		b_exploit = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.b_exploit),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		bd_exploit = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.bd_exploit),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		b_work = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.b_work),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		bd_work = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.bd_work),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		b_delete = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.b_delete),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		bd_delete = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.bd_delete),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		b_addworker = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.b_addworker),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		bd_addworker = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.bd_addworker),Math.round(96*scaleFactor),Math.round(96*scaleFactor),true);
		
		bit_workerscounter = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.workerscounter),Math.round(96*scaleFactor),Math.round(32*scaleFactor),true);
		bit_moneycounter = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.moneycounter),Math.round(96*scaleFactor),Math.round(32*scaleFactor),true);
		bit_timercounter = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.timercounter),Math.round(96*scaleFactor),Math.round(32*scaleFactor),true);
		
		bit_exploiting = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.exploiting),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		
		b_hud = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.showhud),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		b_nohud = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.hidehud),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		b_shop = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.hudshop),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		b_ok = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.hudok),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		b_notok = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.hudokn),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		b_cancel = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.hudcancel),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		b_res = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.hudresources),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		b_lib = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.objectlibrary),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		
		objs[0]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.field),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		objs[1]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.stones),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		objs[2]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.sand),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		objs[3]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.forest),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		objs[4]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.timber_yard),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[4]=-32;
		objs[5]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.treasure),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		objs[6]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.joinery),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[6]=-32;
		objs[7]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.road),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		objs[8]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.furnitureshop),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[8]=-32;
		objs[9]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.wheatfield),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[9]=-32;
		objs[10]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.wheatfieldgrown),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[10]=-32;
		objs[11]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.windmill),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[11]=-32;
		objs[12]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.bakery),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[12]=-32;
		objs[13]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.well),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[13]=-32;
		objs[14]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.street_lamp),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[14]=-32;
		objs[15]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.electronicparts),Math.round(128*scaleFactor),Math.round(160*scaleFactor),true);
		calibobj[15]=-64;
		objs[16]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.cpufactory),Math.round(128*scaleFactor),Math.round(160*scaleFactor),true);
		calibobj[16]=-64;
		objs[17]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.hddfactory),Math.round(128*scaleFactor),Math.round(160*scaleFactor),true);
		calibobj[17]=-64;
		objs[18]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.mousekeyboard),Math.round(128*scaleFactor),Math.round(160*scaleFactor),true);
		calibobj[18]=-64;
		objs[19]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.pcshop),Math.round(128*scaleFactor),Math.round(128*scaleFactor),true);
		calibobj[19]=-32;
		objs[20]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.pcfactory),Math.round(128*scaleFactor),Math.round(160*scaleFactor),true);
		calibobj[20]=-64;
		
		bit_destroying[0]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.destroy1),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		bit_destroying[1]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.destroy2),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		bit_destroying[2]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.destroy3),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		bit_destroying[3]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.destroy4),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		
		bit_building[0]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.build1),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		bit_building[1]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.build2),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		bit_building[2]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.build3),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		bit_building[3]=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(),R.drawable.build4),Math.round(128*scaleFactor),Math.round(96*scaleFactor),true);
		
	}
	
	@Override
	public void onDraw(Canvas canvas)
	{
		int l=-1; int mvstx=-4, mvsty=-4; float calib=0;
		//TODO make drawing objects
		if(gmap!=null)
		for(int i=0;i<gmap.h;i++)
			for(int j=0;j<gmap.w;j++)
				if(i%2==0)
				canvas.drawBitmap(emptyspace, j*128.0f*scaleFactor-camx,i*48.0f*scaleFactor-camy, null);
				else canvas.drawBitmap(emptyspace, (j*128.0f+64.0f)*scaleFactor-camx,i*48.0f*scaleFactor-camy, null);
		if(selection!=-1)
		{if(selectiony%2==0)
			canvas.drawBitmap(bitmapselection, selectionx*128.0f*scaleFactor-camx,selectiony*48.0f*scaleFactor-camy, null);
		else canvas.drawBitmap(bitmapselection, selectionx*128.0f*scaleFactor-camx+64.0f*scaleFactor,selectiony*48.0f*scaleFactor-camy, null);}
		if(gmap!=null)
		for(int i=0;i<gmap.h;i++)
			for(int j=0;j<gmap.w;j++)
			{
				if(i%2==0)calib=0;else calib=64.0f*scaleFactor;
				{
				if(gmap.objectsA[j][i].type==4) {canvas.drawBitmap(objs[gmap.objectsA[j][i].type], j*128.0f*scaleFactor-camx+calib,i*48.0f*scaleFactor-camy-32.0f*scaleFactor, null);}
				else {canvas.drawBitmap(objs[gmap.objectsA[j][i].type], j*128.0f*scaleFactor-camx+calib,i*48.0f*scaleFactor-camy+calibobj[gmap.objectsA[j][i].type]*scaleFactor, null);}
					if(gmap.objectsA[j][i].state==1)
						{
							if(frames==0) {mvstx=-4; mvsty=-4;} if(frames==1) {mvstx=-4; mvsty=4;} if(frames==2) {mvstx=4; mvsty=4;} if(frames==3) {mvstx=4; mvsty=-4;}
							canvas.drawBitmap(bit_exploiting, j*128.0f*scaleFactor-camx+mvstx+calib,i*48.0f*scaleFactor-camy+mvsty, null);
							canvas.drawBitmap(bit_timercounter, j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, i*48.0f*scaleFactor-camy,null);
							canvas.drawText(Integer.toString(gmap.objectsA[j][i].time), j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib+60.0f*scaleFactor, i*48.0f*scaleFactor-camy+25.0f*scaleFactor, counterpaint);
						}
					else if(gmap.objectsA[j][i].state==4)
						{
							canvas.drawBitmap(bit_destroying[frames], j*128.0f*scaleFactor-camx+calib,i*48.0f*scaleFactor-camy, null);
							canvas.drawBitmap(bit_timercounter, j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, i*48.0f*scaleFactor-camy,null);
							canvas.drawText(Integer.toString(gmap.objectsA[j][i].time), j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib+60.0f*scaleFactor, i*48.0f*scaleFactor-camy+25.0f*scaleFactor, counterpaint);
						}
					else if(gmap.objectsA[j][i].state==3)
						{
							canvas.drawBitmap(bit_building[frames], j*128.0f*scaleFactor-camx+calib,i*48.0f*scaleFactor-camy, null);
							canvas.drawBitmap(bit_timercounter, j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, i*48.0f*scaleFactor-camy,null);
							canvas.drawText(Integer.toString(gmap.objectsA[j][i].time), j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib+60.0f*scaleFactor, i*48.0f*scaleFactor-camy+25.0f*scaleFactor, counterpaint);
						}
					else if(gmap.objectsA[j][i].state==2)
						{
						canvas.drawBitmap(bit_timercounter, j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, i*48.0f*scaleFactor-camy,null);
						canvas.drawText(Integer.toString(gmap.objectsA[j][i].time), j*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib+60.0f*scaleFactor, i*48.0f*scaleFactor-camy+25.0f*scaleFactor, counterpaint);
						//canvas.drawText(, (j*128.0f+64.0f)*scaleFactor-camx+calib, i*48.0f*scaleFactor-camy, timepaint);
						}
				}
				/*else
				{canvas.drawBitmap(bitmapselection, (selectionx*128.0f+64.0f)*scaleFactor-camx,selectiony*48.0f*scaleFactor-camy, null);
				if(gmap.objectsA[j][i].type==4) {canvas.drawBitmap(objs[gmap.objectsA[j][i].type], (j*128.0f+64.0f)*scaleFactor-camx,i*48.0f*scaleFactor-camy-32.0f*scaleFactor, null);}
				else {canvas.drawBitmap(objs[gmap.objectsA[j][i].type], (j*128.0f+64.0f)*scaleFactor-camx,i*48.0f*scaleFactor-camy, null);}
					if(gmap.objectsA[j][i].state==1)
					{
						if(frames==0) {mvstx=-4; mvsty=-4;} if(frames==1) {mvstx=-4; mvsty=4;} if(frames==2) {mvstx=4; mvsty=4;} if(frames==3) {mvstx=4; mvsty=-4;}
						canvas.drawBitmap(bit_exploiting, (j*128.0f+64.0f)*scaleFactor-camx+mvstx,i*48.0f*scaleFactor-camy+mvsty, null);
						canvas.drawText(Integer.toString(gmap.objectsA[j][i].time), (j*128.0f+128.0f)*scaleFactor-camx, i*48.0f*scaleFactor-camy, timepaint);
					}
					else if(gmap.objectsA[j][i].state==4)
					{
						canvas.drawBitmap(bit_destroying[frames], (j*128.0f+64.0f)*scaleFactor-camx,i*48.0f*scaleFactor-camy, null);
						canvas.drawText(Integer.toString(gmap.objectsA[j][i].time), (j*128.0f+128.0f)*scaleFactor-camx, i*48.0f*scaleFactor-camy, timepaint);
					}
				else if(gmap.objectsA[j][i].state==3)
					{
						canvas.drawText(Integer.toString(gmap.objectsA[j][i].time), (j*128.0f+128.0f)*scaleFactor-camx, i*48.0f*scaleFactor-camy, timepaint);
					}
				}*/
			}
		/*for(GameObject obj:gmap.objects)
			{
				l++;
				if(obj.y1%2==0)
				{
				if(l==selection) {canvas.drawBitmap(bitmapselection, obj.x1*128.0f*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy, null); selectionx=obj.x1; selectiony=obj.y1;}
				if(obj.type==4) {canvas.drawBitmap(objs[obj.type], obj.x1*128.0f*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy-32.0f*scaleFactor, null);}
				else {canvas.drawBitmap(objs[obj.type], obj.x1*128.0f*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy, null);}
					if(obj.state==1)
						{
							if(frames==0) {mvstx=-4; mvsty=-4;} if(frames==1) {mvstx=-4; mvsty=4;} if(frames==2) {mvstx=4; mvsty=4;} if(frames==3) {mvstx=4; mvsty=-4;}
							canvas.drawBitmap(bit_exploiting, obj.x1*128.0f*scaleFactor-camx+mvstx,obj.y1*48.0f*scaleFactor-camy+mvsty, null);
							canvas.drawText(Integer.toString(obj.time), (obj.x1*128.0f+64.0f)*scaleFactor-camx, obj.y1*48.0f*scaleFactor-camy, timepaint);
						}
					else if(obj.state==4)
						{
							canvas.drawBitmap(bit_destroying[frames], obj.x1*128.0f*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy, null);
							canvas.drawText(Integer.toString(obj.time), (obj.x1*128.0f+64.0f)*scaleFactor-camx, obj.y1*48.0f*scaleFactor-camy, timepaint);
						}
					else if(obj.state==3)
						{
							canvas.drawText(Integer.toString(obj.time), (obj.x1*128.0f+64.0f)*scaleFactor-camx, obj.y1*48.0f*scaleFactor-camy, timepaint);
						}
				}
				else
				{if(l==selection) {canvas.drawBitmap(bitmapselection, (obj.x1*128.0f+64.0f)*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy, null); selectionx=obj.x1; selectiony=obj.y1;}
				if(obj.type==4) {canvas.drawBitmap(objs[obj.type], (obj.x1*128.0f+64.0f)*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy-32.0f*scaleFactor, null);}
				else {canvas.drawBitmap(objs[obj.type], (obj.x1*128.0f+64.0f)*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy, null);}
					if(obj.state==1)
					{
						if(frames==0) {mvstx=-4; mvsty=-4;} if(frames==1) {mvstx=-4; mvsty=4;} if(frames==2) {mvstx=4; mvsty=4;} if(frames==3) {mvstx=4; mvsty=-4;}
						canvas.drawBitmap(bit_exploiting, (obj.x1*128.0f+64.0f)*scaleFactor-camx+mvstx,obj.y1*48.0f*scaleFactor-camy+mvsty, null);
						canvas.drawText(Integer.toString(obj.time), (obj.x1*128.0f+128.0f)*scaleFactor-camx, obj.y1*48.0f*scaleFactor-camy, timepaint);
					}
					else if(obj.state==4)
					{
						canvas.drawBitmap(bit_destroying[frames], (obj.x1*128.0f+64.0f)*scaleFactor-camx,obj.y1*48.0f*scaleFactor-camy, null);
						canvas.drawText(Integer.toString(obj.time), (obj.x1*128.0f+128.0f)*scaleFactor-camx, obj.y1*48.0f*scaleFactor-camy, timepaint);
					}
				else if(obj.state==3)
					{
						canvas.drawText(Integer.toString(obj.time), (obj.x1*128.0f+128.0f)*scaleFactor-camx, obj.y1*48.0f*scaleFactor-camy, timepaint);
					}
				}
			}*/
		if(putting)
		{if(selectiony%2==0)
			canvas.drawBitmap(objs[puttype], selectionx*128.0f*scaleFactor-camx,selectiony*48.0f*scaleFactor-camy-48.0f*scaleFactor, null);
		else canvas.drawBitmap(objs[puttype], selectionx*128.0f*scaleFactor-camx+64.0f*scaleFactor,selectiony*48.0f*scaleFactor-camy-48.0f*scaleFactor, null);
		}
		if(!putting)
		if(selection>-1)
		{
			if(selectiony%2==0) calib=0; else calib=64.0f*scaleFactor;
					if(selectedstate==0)
					{
					if(btn_N) canvas.drawBitmap(b_delete, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy-96.0f*scaleFactor,null);
					if(btn_E) {if(exploitable)canvas.drawBitmap(b_exploit, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy,null);
					else if(workable){if(satisfied)canvas.drawBitmap(b_work, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy,null); else canvas.drawBitmap(bd_work, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy,null);}
					if(btn_S) if(workable)canvas.drawBitmap(b_addworker, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor,null);
						}
					}
					else{
					if(btn_N) canvas.drawBitmap(bd_delete, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy-96.0f*scaleFactor,null);
					if(btn_E) {if(exploitable) canvas.drawBitmap(bd_exploit, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy,null);
					else if(workable)canvas.drawBitmap(bd_work, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+128.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy,null);
					if(btn_S) if(workable)canvas.drawBitmap(bd_addworker, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy+96.0f*scaleFactor,null);
						}
					}
					if(workable){canvas.drawBitmap(bit_workerscounter, selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib, selectiony*48.0f*scaleFactor-camy+32.0f*scaleFactor,null); canvas.drawText(Integer.toString(selectionworkers), selectionx*128.0f*scaleFactor-camx+16.0f*scaleFactor+calib+60.0f*scaleFactor, selectiony*48.0f*scaleFactor-camy+32.0f*scaleFactor+25.0f*scaleFactor, counterpaint);}
		}
		canvas.drawBitmap(bit_moneycounter, (screenW/2)-48.0f*scaleFactor, 4.0f,null); canvas.drawText(Integer.toString(money), (screenW/2)-48.0f*scaleFactor+60.0f*scaleFactor, 4.0f+25.0f*scaleFactor, counterpaint);
		//canvas.drawText("Wheat:"+Integer.toString(wheat),0.0f, 18.0f, textpaint);
		switch(hudmode)
		{
			case 0: {canvas.drawBitmap(b_hud, screenW-128*scaleFactor,0, null);}break;
			case 1: {canvas.drawBitmap(b_nohud, screenW-128*scaleFactor,0, null);canvas.drawBitmap(b_shop, screenW-128*scaleFactor,screenH-128*scaleFactor, null); canvas.drawBitmap(b_res, 0, 0, null);canvas.drawBitmap(b_lib, 0,screenH-128*scaleFactor, null);}break;
			case 2: {if(notok)canvas.drawBitmap(b_ok, screenW-128*scaleFactor,screenH-128*scaleFactor, null); else canvas.drawBitmap(b_notok, screenW-128*scaleFactor,screenH-128*scaleFactor, null); canvas.drawBitmap(b_cancel, 0, screenH-128.0f*scaleFactor,null);}break;
		}
		if(dialog)
			{canvas.drawRect(30, 30, screenW-30, screenH-30, dialogpaint);
			canvas.drawText(dialogtext, screenW/2, screenH/2+12, dialogtextpaint);}
		if(sMile)
			{
				Bitmap tmpbit = Bitmap.createBitmap(canvas.getWidth(),canvas.getHeight(),Bitmap.Config.ARGB_8888);
				bMile = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
				Canvas minicanvas = new Canvas(bMile);
				canvas.setBitmap(tmpbit);
				minicanvas.drawBitmap(tmpbit, -100,-100,null);
				sMile=false;
				sMileready=true;
			}
	}
	
	private Handler drawHandler = new Handler(){
		@Override
		public void handleMessage(Message msg)
		{
			invalidate();
		}
	};
	
	public void redraw(GameMap map, float camerax, float cameray)
	{
		gmap = map;
		camx = camerax;
		camy = cameray;
		if(frames<3) frames+=1; else frames=0;
		wheat=gmap.resources[7];
		money=gmap.resources[0];
		drawHandler.sendEmptyMessage(0);
	}
	
	public void quickRedraw()
	{
		drawHandler.sendEmptyMessage(0);
	}
	
	public void preparePreview()
	{
		sMile=true;
		quickRedraw();
	}
	
	public Bitmap takePreview()
	{
		return bMile;
	}
	
}
