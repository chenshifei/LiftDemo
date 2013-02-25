package com.dark.liftdemo;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;

public class Window {
	
	final int maxLift=5;
	final int maxFloor=20;
	final int doorSpeed=2000;

	private JFrame frmLiftdemo;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Window window = new Window();
					window.frmLiftdemo.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Window() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		final Dispatcher dispatcher=new Dispatcher();
		frmLiftdemo = new JFrame();
		frmLiftdemo.setTitle("LiftDemo");
		frmLiftdemo.setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/com/dark/liftdemo/logo.png")));
		frmLiftdemo.setBounds(0, 0, 761, 630);
		frmLiftdemo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmLiftdemo.getContentPane().setLayout(null);
		
		//设置外部面板属性
		Lift.lblPanelOut.setBounds(656, 10, 79, 573);
		Lift.lblPanelOut.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/panelout.png")));
		frmLiftdemo.getContentPane().add(Lift.lblPanelOut,-1);
		
		for(int i=0;i!=maxLift;i++){
			//设置内部面板属性
			dispatcher.lift[i].lblPanelIn.setBounds(10+i*85, 10, 80, 573);
			dispatcher.lift[i].lblPanelIn.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/panelin.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblPanelIn,-1);
			//设置电梯井属性
			dispatcher.lift[i].lblWell.setBounds(471+i*37, 10, 37, 573);
			dispatcher.lift[i].lblWell.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/well.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblWell,-1);
			//各个按钮和显示器属性
			dispatcher.lift[i].lblAlarm.setBounds(42+i*85, 168, 16, 17);
			dispatcher.lift[i].lblAlarm.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/alarm.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblAlarm,0);
			dispatcher.lift[i].lblOpen.setBounds(58+i*85, 200, 16, 17);
			dispatcher.lift[i].lblOpen.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/open.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblOpen,0);
			dispatcher.lift[i].lblClose.setBounds(26+i*85, 200, 16, 17);
			dispatcher.lift[i].lblClose.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/close.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblClose,0);
			dispatcher.lift[i].lblLift.setBounds(471+i*37, 533, 36, 33);
			dispatcher.lift[i].lblLift.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/liftc.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblLift,0);
			dispatcher.lift[i].lblDisplayer.setBounds(32+i*85, 60, 37, 27);
			dispatcher.lift[i].lblDisplayer.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/dig1.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblDisplayer,0);
			dispatcher.lift[i].lblDisplayerUp.setBounds(30+i*85, 37, 19, 11);
			dispatcher.lift[i].lblDisplayerUp.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/digup.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblDisplayerUp,0);
			dispatcher.lift[i].lblDisplayerUp.setVisible(false);
			dispatcher.lift[i].lblDisplayerDown.setBounds(52+i*85, 37, 19, 11);
			dispatcher.lift[i].lblDisplayerDown.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/digdown.png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblDisplayerDown,0);
			dispatcher.lift[i].lblDisplayerDown.setVisible(false);
			dispatcher.lift[i].lblNum.setBounds(54+i*85, 123, 18, 18);
			dispatcher.lift[i].lblNum.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/no"+(i+1)+".png")));
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblNum,0);
			dispatcher.lift[i].lblDisplayerAlarm.setBounds(27+i*85, 28, 48, 64);
			dispatcher.lift[i].lblDisplayerAlarm.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/digalarm.png")));
			dispatcher.lift[i].lblDisplayerAlarm.setVisible(false);
			frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblDisplayerAlarm,0);
			
			//设置各个按钮的鼠标事件
			final int itemp=i;
			dispatcher.lift[i].lblOpen.addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e){
					dispatcher.lift[itemp].lblOpen.setBounds(dispatcher.lift[itemp].lblOpen.getX(),dispatcher.lift[itemp].lblOpen.getY()+1,16,17);
					if(dispatcher.lift[itemp].tellCurrStatus()==LiftStatus.idle){
						dispatcher.lift[itemp].open();
					}
				}
				@Override
				public void mouseReleased(MouseEvent e){
					dispatcher.lift[itemp].lblOpen.setBounds(dispatcher.lift[itemp].lblOpen.getX(), dispatcher.lift[itemp].lblOpen.getY()-1, 16, 17);
				}
			});
			dispatcher.lift[i].lblClose.addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e){
					dispatcher.lift[itemp].lblClose.setBounds(dispatcher.lift[itemp].lblClose.getX(),dispatcher.lift[itemp].lblClose.getY()+1,16,17);
					if(dispatcher.lift[itemp].tellCurrStatus()==LiftStatus.idle){
						dispatcher.lift[itemp].close();
					}
				}
				@Override
				public void mouseReleased(MouseEvent e){
					dispatcher.lift[itemp].lblClose.setBounds(dispatcher.lift[itemp].lblClose.getX(), dispatcher.lift[itemp].lblClose.getY()-1, 16, 17);
				}
			});
			dispatcher.lift[i].lblAlarm.addMouseListener(new MouseAdapter(){
				@Override
				public void mousePressed(MouseEvent e){
					dispatcher.lift[itemp].lblAlarm.setBounds(dispatcher.lift[itemp].lblAlarm.getX(),dispatcher.lift[itemp].lblAlarm.getY()+1,16,17);
					if(dispatcher.lift[itemp].tellCurrStatus()!=LiftStatus.emergency){
						dispatcher.lift[itemp].lblAlarm.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/alarms.png")));
						dispatcher.dealEmergency(itemp);
					}
					else{
						dispatcher.lift[itemp].lblAlarm.setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/alarm.png")));
						dispatcher.dealEmergency(itemp);
					}
				}
				@Override
				public void mouseReleased(MouseEvent e){
					dispatcher.lift[itemp].lblAlarm.setBounds(dispatcher.lift[itemp].lblAlarm.getX(), dispatcher.lift[itemp].lblAlarm.getY()-1, 16, 17);
				}
			});
			//设置内面板楼层按钮属性和鼠标事件
			for(int j=0;j!=maxFloor;j++){
				final int jtemp=j;
				if(j%2==0||j==0)
					dispatcher.lift[i].lblFloor[j].setBounds(26+i*85, 548-j/2*33, 16, 17);
				else
					dispatcher.lift[i].lblFloor[j].setBounds(58+i*85, 548-(j-1)/2*33, 16, 17);
				dispatcher.lift[i].lblFloor[j].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/"+(j+1)+".png")));
				frmLiftdemo.getContentPane().add(dispatcher.lift[i].lblFloor[j],0);
				dispatcher.lift[i].lblFloor[j].addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(MouseEvent e) {
						if(dispatcher.lift[itemp].tellCurrStatus()!=LiftStatus.emergency&&dispatcher.lift[itemp].tellCurrFloor()!=jtemp+1)
							dispatcher.lift[itemp].lblFloor[jtemp].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/"+(jtemp+1)+"s.png")));
						dispatcher.lift[itemp].lblFloor[jtemp].setBounds(dispatcher.lift[itemp].lblFloor[jtemp].getX(), dispatcher.lift[itemp].lblFloor[jtemp].getY()+1, 16, 17);
						dispatcher.dealInRequest(jtemp+1, itemp);
					}
					@Override
					public void mouseReleased(MouseEvent e){
						dispatcher.lift[itemp].lblFloor[jtemp].setBounds(dispatcher.lift[itemp].lblFloor[jtemp].getX(), dispatcher.lift[itemp].lblFloor[jtemp].getY()-1, 16, 17);
					}
				});
			}
		}
		//设置外面板上下按钮属性和鼠标事件
		for(int i=0;i!=maxFloor;i++){
			final int itemp=i;
			if(i<19){
				Lift.lblUp[i]=new JLabel("");
				Lift.lblUp[i].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/up.png")));
				if(i!=0)
					Lift.lblUp[i].setBounds(671, 512-(i-1)*26, 16, 17);
				else
					Lift.lblUp[i].setBounds(687, 538, 16, 17);
				frmLiftdemo.getContentPane().add(Lift.lblUp[i],0);
				Lift.lblUp[i].addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent e){
						Lift.lblUp[itemp].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/ups.png")));
						Lift.lblUp[itemp].setBounds(Lift.lblUp[itemp].getX(), Lift.lblUp[itemp].getY()+1, 16, 17);
						dispatcher.dealOutRequest(itemp+1, LiftStatus.up);
					}
					@Override
					public void mouseReleased(MouseEvent e){
						Lift.lblUp[itemp].setBounds(Lift.lblUp[itemp].getX(), Lift.lblUp[itemp].getY()-1, 16, 17);
					}
				});
			}
			if(i>0){
				Lift.lblDown[i-1]=new JLabel("");
				Lift.lblDown[i-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/down.png")));
				if(i!=19)
					Lift.lblDown[i-1].setBounds(703, 512-(i-1)*26, 16, 17);
				else
					Lift.lblDown[i-1].setBounds(687, 44, 16, 17);
				frmLiftdemo.getContentPane().add(Lift.lblDown[i-1],0);
				Lift.lblDown[i-1].addMouseListener(new MouseAdapter(){
					@Override
					public void mousePressed(MouseEvent e){
						Lift.lblDown[itemp-1].setIcon(new ImageIcon(Window.class.getResource("/com/dark/liftdemo/downs.png")));
						Lift.lblDown[itemp-1].setBounds(Lift.lblDown[itemp-1].getX(), Lift.lblDown[itemp-1].getY()+1, 16, 17);
						dispatcher.dealOutRequest(itemp+1, LiftStatus.down);
					}
					@Override
					public void mouseReleased(MouseEvent e){
						Lift.lblDown[itemp-1].setBounds(Lift.lblDown[itemp-1].getX(), Lift.lblDown[itemp-1].getY()-1, 16, 17);
					}
				});
			}
		}
	}
}
