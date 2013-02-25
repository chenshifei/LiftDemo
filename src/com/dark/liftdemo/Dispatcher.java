package com.dark.liftdemo;

import java.lang.Math;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.ImageIcon;

public class Dispatcher {

	final int maxLift=5;
	final int maxFloor=20;
	
	Thread threadLift[]=new Thread[maxLift];	//�洢ÿ�����ݸ��Ե��߳�
	Lift lift[]=new Lift[maxLift];				//�洢5������ģ��
	
	Dispatcher(){
		//����5�����ݵ��߳�
		for(int i=0;i!=maxLift;i++){
			Lift targetLift=new Lift();
			lift[i]=targetLift;
			Thread thdLift=new Thread(targetLift);
			threadLift[i]=thdLift;
			threadLift[i].start();
		}
	}
	
	//��������ⲿ����
	public void dealOutRequest(int floor,LiftStatus direction){
		int num=findNearestLift(floor,direction);	//Ѱ�ҳ�����ķ���Ҫ��ĵ���
		//���ݵ�����Ŀ��¥���������Ϊ�䰲��Ŀ�ĵ�
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
		//������ݾ���Ŀ��¥���ֱ�ӿ���
		else{
			lift[num].open();
			if(direction==LiftStatus.up)
				Lift.lblUp[floor-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/up.png")));
			else
				Lift.lblDown[floor-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/down.png")));
		}
	}
	
	//��������ڲ�����
	public void dealInRequest(int floor,int num){
		if(lift[num].tellCurrStatus()!=LiftStatus.emergency){
			if(lift[num].tellCurrFloor()>floor){
				if(!lift[num].destFloorDown.contains(floor)){	//�����ǰĿ�ĵ���û����һ��ͼ����µ�Ŀ�ĵ�
					lift[num].destFloorDown.add(floor);
					Comparator<Integer> comparator=Collections.reverseOrder();	//��Ŀ�ĵ�������Զ�����������ǰ
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
	
	//����������
	public void dealEmergency(int num){
		if(lift[num].tellCurrStatus()!=LiftStatus.emergency){
			lift[num].currStatus=LiftStatus.emergency;
			lift[num].lblDisplayer.setVisible(false);	//��ʾ������
			lift[num].lblDisplayerUp.setVisible(false);
			lift[num].lblDisplayerDown.setVisible(false);
			lift[num].lblDisplayerAlarm.setVisible(true);
			lift[num].destFloorUp.clear();	//���Ŀ�ĵ�
			lift[num].destFloorDown.clear();
			lift[num].open();	//����
		}
		else{
			lift[num].close();
			lift[num].lblDisplayerAlarm.setVisible(false);
			lift[num].lblDisplayer.setVisible(true);
			lift[num].currStatus=LiftStatus.idle;
		}
	}
	
	//Ѱ������ķ��������ĵ���
	private int findNearestLift(int floor,LiftStatus direction){
		int nearest=maxFloor;
		int num=0;
		int distance=maxFloor;
		for(int i=0;i!=maxLift;i++){
			if(lift[i].tellCurrStatus()!=LiftStatus.emergency){
				//Ѱ�Һ�Ŀ�귽����ͬ�ĵ���
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
				//���û�У���Ѱ�ҿ��еĵ���
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