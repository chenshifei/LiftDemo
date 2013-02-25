package com.dark.liftdemo;

import java.lang.Math;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.ImageIcon;

public class Dispatcher {

	final int maxLift=5;
	final int maxFloor=20;
	
	Thread threadLift[]=new Thread[maxLift];	//存储每个电梯各自的线程
	Lift lift[]=new Lift[maxLift];				//存储5部电梯模型
	
	Dispatcher(){
		//启动5部电梯的线程
		for(int i=0;i!=maxLift;i++){
			Lift targetLift=new Lift();
			lift[i]=targetLift;
			Thread thdLift=new Thread(targetLift);
			threadLift[i]=thdLift;
			threadLift[i].start();
		}
	}
	
	//处理电梯外部请求
	public void dealOutRequest(int floor,LiftStatus direction){
		int num=findNearestLift(floor,direction);	//寻找出最近的符合要求的电梯
		//根据电梯在目标楼层的上下来为其安排目的地
		if(floor>lift[num].tellCurrFloor()){	
			if(!lift[num].destFloorUp.contains(floor)){
				lift[num].destFloorUp.add(floor);
				Collections.sort(lift[num].destFloorUp);
				if(direction==LiftStatus.up){
					Lift.requiredUp.add(floor);
				}
				else{
					Lift.requiredDown.add(floor);
				}
			}
		}
		else if(floor<lift[num].tellCurrFloor()){
			if(!lift[num].destFloorDown.contains(floor)){
				lift[num].destFloorDown.add(floor);
				Comparator<Integer> comparator=Collections.reverseOrder();
				Collections.sort(lift[num].destFloorDown,comparator);
				if(direction==LiftStatus.up){
					Lift.requiredUp.add(floor);
				}
				else{
					Lift.requiredDown.add(floor);
				}
			}
		}
		//如果电梯就在目标楼层就直接开门
		else{
			lift[num].open();
			if(direction==LiftStatus.up)
				Lift.lblUp[floor-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/up.png")));
			else
				Lift.lblDown[floor-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/down.png")));
		}
	}
	
	//处理电梯内部请求
	public void dealInRequest(int floor,int num){
		if(lift[num].tellCurrStatus()!=LiftStatus.emergency){
			if(lift[num].tellCurrFloor()>floor){
				if(!lift[num].destFloorDown.contains(floor)){	//如果当前目的地中没有这一层就加入新的目的地
					lift[num].destFloorDown.add(floor);
					Comparator<Integer> comparator=Collections.reverseOrder();	//对目的地排序，永远是最近的在最前
					Collections.sort(lift[num].destFloorDown,comparator);
				}
			}
			else if(lift[num].tellCurrFloor()<floor){
				if(!lift[num].destFloorUp.contains(floor)){
					lift[num].destFloorUp.add(floor);
					Collections.sort(lift[num].destFloorUp);
				}
			}
			else{
				lift[num].open();
			}
		}
	}
	
	//处理紧急情况
	public void dealEmergency(int num){
		if(lift[num].tellCurrStatus()!=LiftStatus.emergency){
			lift[num].currStatus=LiftStatus.emergency;
			lift[num].lblDisplayer.setVisible(false);	//显示器设置
			lift[num].lblDisplayerUp.setVisible(false);
			lift[num].lblDisplayerDown.setVisible(false);
			lift[num].lblDisplayerAlarm.setVisible(true);
			lift[num].destFloorUp.clear();	//清空目的地
			lift[num].destFloorDown.clear();
			lift[num].open();	//开门
		}
		else{
			lift[num].close();
			lift[num].lblDisplayerAlarm.setVisible(false);
			lift[num].lblDisplayer.setVisible(true);
			lift[num].currStatus=LiftStatus.idle;
		}
	}
	
	//寻找最近的符合条件的电梯
	private int findNearestLift(int floor,LiftStatus direction){
		int nearest=maxFloor;
		int num=0;
		int distance=maxFloor;
		for(int i=0;i!=maxLift;i++){
			if(lift[i].tellCurrStatus()!=LiftStatus.emergency){
				//寻找和目标方向相同的电梯
				if(lift[i].tellCurrStatus()==direction&&direction==LiftStatus.up&&lift[i].tellCurrFloor()<floor){
					distance=Math.abs(lift[i].tellCurrFloor()-floor);
					if(distance<nearest){
						nearest=distance;
						num=i;
					}
				}
				else if(lift[i].tellCurrStatus()==direction&&direction==LiftStatus.down&&lift[i].tellCurrFloor()>floor){
					distance=Math.abs(lift[i].tellCurrFloor()-floor);
					if(distance<nearest){
						nearest=distance;
						num=i;
					}
				}
				//如果没有，则寻找空闲的电梯
				else if(lift[i].tellCurrStatus()==LiftStatus.idle){
					distance=Math.abs(lift[i].tellCurrFloor()-floor);
					if(distance<nearest){
						nearest=distance;
						num=i;
					}
					else if(distance==nearest){
						if(lift[i].tellCurrFloor()<floor)
							num=i;
					}
				}
			}
			else
				continue;
		}
		return num;
	}
}