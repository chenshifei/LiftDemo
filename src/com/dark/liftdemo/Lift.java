package com.dark.liftdemo;

import java.util.LinkedList;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import com.dark.liftdemo.Window;;

public class Lift implements Runnable {

	final int runSpeed=700;
	final int doorSpeed=2000;
	final int waitSpeed=500;
	final static int maxFloor=20;
	
	private int currFloor;	//��ǰ¥��
	static LinkedList<Integer> requiredUp=new LinkedList<Integer>();	//���������ⲿ����
	static LinkedList<Integer> requiredDown=new LinkedList<Integer>();	
	LinkedList<Integer> destFloorUp=new LinkedList<Integer>();	//����Ŀ�ĵ�
	LinkedList<Integer> destFloorDown=new LinkedList<Integer>();	//����Ŀ�ĵ�
	LiftStatus currStatus;	//��ǰ״̬
	Thread self=null;
	
	//���ֿؼ�
	JLabel lblPanelIn=new JLabel("");
	static JLabel lblPanelOut=new JLabel("");
	static JLabel lblUp[]=new JLabel[maxFloor-1];
	static JLabel lblDown[]=new JLabel[maxFloor-1];
	JLabel lblWell=new JLabel("");
	JLabel lblAlarm=new JLabel("");
	JLabel lblOpen=new JLabel("");
	JLabel lblClose=new JLabel("");
	JLabel lblFloor[]=new JLabel[maxFloor];
	JLabel lblLift=new JLabel("");
	JLabel lblDisplayer=new JLabel("");
	JLabel lblDisplayerUp=new JLabel("");
	JLabel lblDisplayerDown=new JLabel("");
	JLabel lblNum=new JLabel("");
	JLabel lblDisplayerAlarm=new JLabel("");
	
	//���캯��
	Lift(){
		currFloor=1;
		currStatus=LiftStatus.idle;
		
		for(int i=0;i!=maxFloor;i++)
		 	lblFloor[i]=new JLabel("");

	}
	
	@SuppressWarnings("static-access")
	@Override
	public void run() {
		while(true){
			//����ʱ����
			if(destFloorUp.size()!=0){
				lblDisplayerUp.setVisible(true);
				lblLift.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/liftc.png")));
				try {
					self.sleep(runSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(currFloor==destFloorUp.getFirst())
					arrive();
				else if(currFloor!=destFloorUp.getFirst()){
					currStatus=LiftStatus.up;
					currFloor++;
					lblLift.setBounds(lblLift.getX(), lblLift.getY()-26, 36, 33);
					lblDisplayer.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/dig"+tellCurrFloor()+".png")));
				}
			}
			//����ʱ����
			else if(destFloorDown.size()!=0){
				lblDisplayerDown.setVisible(true);
				lblLift.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/liftc.png")));
				try {
					self.sleep(runSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				if(currFloor==destFloorDown.getFirst())
					arrive();
				else if(currFloor!=destFloorDown.getFirst()){
					currStatus=LiftStatus.down;
					currFloor--;
					lblLift.setBounds(lblLift.getX(), lblLift.getY()+26, 36, 33);
					lblDisplayer.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/dig"+tellCurrFloor()+".png")));
				}
			}
			//û�����������һ��ʱ�䣬��ֹ�߳�����
			else{
				try {
				self.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//����Ŀ�ĵ�ʱ����
	@SuppressWarnings("static-access")
	public void arrive(){
		//���߳�����һ��ʱ����Ƶ��������ٶ�
		try {
			self.sleep(waitSpeed);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//�ر���ʾ��������ָʾ
		lblDisplayerUp.setVisible(false);
		lblDisplayerDown.setVisible(false);
		//��Ŀ�ĵ����Ƴ�����ѵ���¥��
		if(tellCurrStatus()==LiftStatus.up){
			destFloorUp.removeFirst();
		}
		else if(tellCurrStatus()==LiftStatus.down){
			destFloorDown.removeFirst();
		}
		//˳������ͬ������ⲿ�˿�
		if(destFloorUp.size()==0&&requiredDown.contains(tellCurrFloor())){
			lblDown[tellCurrFloor()-2].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/down.png")));
			requiredDown.remove((Integer)tellCurrFloor());
		}
		else if(destFloorDown.size()==0&&requiredUp.contains(tellCurrFloor())){
			lblUp[tellCurrFloor()-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/up.png")));
			requiredUp.remove((Integer)tellCurrFloor());
		}
		//�Ƴ���ǰ�ѵ���Ŀ�ĵص���ʾ
		lblFloor[tellCurrFloor()-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/"+tellCurrFloor()+".png")));
		//�����ţ�ͬ�����߳�����һ��ʱ�������ƿ������ٶ�
		open();
		try {
			self.sleep(doorSpeed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		close();
		//�õ���״̬�Ļؿ���
		this.currStatus=LiftStatus.idle;
	}

	//����
	public void open(){
		this.lblLift.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/lifto.png")));
	}
	
	//����
	public void close(){
		lblLift.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/liftc.png")));
	}
	
	//���ص�ǰ¥��
	public int tellCurrFloor(){
		return currFloor;
	}
	
	//���ص�ǰ״̬
	public LiftStatus tellCurrStatus(){
		return currStatus;
	}
}
