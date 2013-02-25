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
	
	private int currFloor;	//当前楼层
	static LinkedList<Integer> requiredUp=new LinkedList<Integer>();	//保存所有外部请求
	static LinkedList<Integer> requiredDown=new LinkedList<Integer>();	
	LinkedList<Integer> destFloorUp=new LinkedList<Integer>();	//上行目的地
	LinkedList<Integer> destFloorDown=new LinkedList<Integer>();	//下行目的地
	LiftStatus currStatus;	//当前状态
	Thread self=null;
	
	//各种控件
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
	
	//构造函数
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
			//上行时设置
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
			//下行时设置
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
			//没有任务就休眠一段时间，防止线程阻塞
			else{
				try {
				self.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//到达目的地时处理
	@SuppressWarnings("static-access")
	public void arrive(){
		//让线程休眠一段时间控制电梯运行速度
		try {
			self.sleep(waitSpeed);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		//关闭显示器上下行指示
		lblDisplayerUp.setVisible(false);
		lblDisplayerDown.setVisible(false);
		//从目的地中移除这个已到达楼层
		if(tellCurrStatus()==LiftStatus.up){
			destFloorUp.removeFirst();
		}
		else if(tellCurrStatus()==LiftStatus.down){
			destFloorDown.removeFirst();
		}
		//顺带接收同方向的外部乘客
		if(destFloorUp.size()==0&&requiredDown.contains(tellCurrFloor())){
			lblDown[tellCurrFloor()-2].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/down.png")));
			requiredDown.remove((Integer)tellCurrFloor());
		}
		else if(destFloorDown.size()==0&&requiredUp.contains(tellCurrFloor())){
			lblUp[tellCurrFloor()-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/up.png")));
			requiredUp.remove((Integer)tellCurrFloor());
		}
		//移除当前已到达目的地的显示
		lblFloor[tellCurrFloor()-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/"+tellCurrFloor()+".png")));
		//控制门，同样让线程休眠一段时间来控制开关门速度
		open();
		try {
			self.sleep(doorSpeed);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		close();
		//让电梯状态改回空闲
		this.currStatus=LiftStatus.idle;
	}

	//开门
	public void open(){
		this.lblLift.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/lifto.png")));
	}
	
	//关门
	public void close(){
		lblLift.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/liftc.png")));
	}
	
	//返回当前楼层
	public int tellCurrFloor(){
		return currFloor;
	}
	
	//返回当前状态
	public LiftStatus tellCurrStatus(){
		return currStatus;
	}
}
