package day1202;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import java.util.*;

//��Ʈ���� ����~
public class Tetris extends JFrame implements Runnable, KeyListener
{
	private int width;			//����
	private int height;			//����
	private	int xCnt;			//���ι迭ũ��
	private	int yCnt;			//���ι迭ũ��
	private int area;			//���μ��α���
	private int time;			//������
	private boolean[][] grid;	//
	private JPanel[][] background;	//����ǳ�
	private Container fc;		//�����̳�
	private Item nextItem;		//�������ð�
	private Item currentItem;	//������ ������
	private ArrayList<Item> itemList;	//�����۸���Ʈ
	private ArrayList<Color> colorList;	//�÷�����Ʈ
	private Random rnd;
	private JPanel top, next, center;			//��� �����ºκ�
	private boolean isKey = true;		//Ű����Ȱ��ȭ����
	private final Color bgColor = Color.white;	//����÷�
//	public static boolean isRight = false;		//�����ʿ���
	Thread t;

	public Tetris(String str){
		//========== �⺻���� ���� ���� ===========
		this.setTitle(str);
		this.xCnt = 14;
		this.yCnt = 25;
		this.time = 500;
		this.area = 20;
		this.width = this.xCnt * this.area;
		this.height = this.yCnt * this.area;
		this.itemList = new ArrayList<Item>();
		this.background = new JPanel[this.xCnt][this.yCnt];
		this.grid = new boolean[this.xCnt][this.yCnt];
		this.rnd = new Random(System.currentTimeMillis());

		this.fc = this.getContentPane();

		this.center = new JPanel();
		this.center.setSize(this.width, this.height);
		this.center.setLayout(null);
		this.center.setBackground(new Color(224,255,216));
		this.fc.add(this.center, "Center");

		this.addKeyListener(this);
		this.setBounds(200,200,this.width+8,this.height+13);

		//========== �⺻���� ���� �� ===========

		//������ �߰��ϱ�
		itemList.add(new Rect(this.area, this.center, this.xCnt));
		itemList.add(new OneThree(this.area, this.center, this.xCnt));
		itemList.add(new ThreeOne(this.area, this.center, this.xCnt));
		itemList.add(new LineBlock(this.area, this.center, this.xCnt));
		itemList.add(new Triangle(this.area, this.center, this.xCnt));
		itemList.add(new RightBlock(this.area, this.center, this.xCnt));
		itemList.add(new LeftBlock(this.area, this.center, this.xCnt));

		//�� �߰�
		this.colorList = new ArrayList<Color>();
		this.colorList.add(Color.red);
		this.colorList.add(Color.blue);
		this.colorList.add(Color.green);
		this.colorList.add(Color.orange);
		this.colorList.add(Color.pink);
		this.colorList.add(new Color(170,40,150));	//����

		//��� ���� ����======

		this.top = new JPanel();
		this.next = new JPanel();
		this.top.setBounds(0,0,this.xCnt*this.area, this.area*4);
		this.top.setBackground(new Color(244,211,99));
		this.next.setBounds((this.xCnt-4)*this.area,0,this.area*4, this.area*4);
		this.next.setBackground(new Color(245,180,250));

		this.center.add(this.top);
		this.top.setLayout(null);
		this.top.add(this.next);

		//��� ���� ��======

		//��׶��� �г� ���� ���� ==========
		for (int i=0; i<background.length; i++){
			for (int p=0; p<background[i].length; p++){
				this.background[i][p] = new JPanel();
				this.background[i][p].setBounds(i * this.area, p * this.area, this.area, this.area);
				this.background[i][p].setBackground(this.bgColor);
				this.center.add(background[i][p]);
			}
		}
		//��׶��� �г� ���� ���� ==========

		//������ ���ۼ���
		this.currentItem = itemList.get(rnd.nextInt(itemList.size()));
		this.currentItem.setColor(this.colorList.get(this.rnd.nextInt(this.colorList.size())));
		this.currentItem.setDefaultLocation();
		setNextItem();
	
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
//		this.setResizable(false);

		t = new Thread(this);
		t.start();
	}
	//�ؽ�Ʈ ������ ����
	public void setNextItem(){
		Item temp;
		do{
			temp = itemList.get(rnd.nextInt(itemList.size()));
		}
		while (temp==this.currentItem);		//��������۰� �ߺ�X
		this.nextItem = temp;
		this.nextItem.setColor(this.colorList.get(this.rnd.nextInt(this.colorList.size())));
		this.nextItem.setNextLocation();	//��ġ����
	}
	//������ ���� ������ ����
	public void setNewItem(){
		this.currentItem = this.nextItem;
		this.currentItem.setDefaultLocation();
		setNextItem();
	}
	//��׶��� �� ä���
	public void setBack(int x, int y, Color c){
		this.background[x][y].setBackground(c);
		this.background[x][y].setBorder(new SoftBevelBorder(BevelBorder.RAISED));
		this.grid[x][y] = true;
//		System.out.println("x="+x+", y="+y);
	}
	//��׶��� �� ����
	public void setEmptyBack(int x, int y){
		this.background[x][y].setBorder(null);
		this.background[x][y].setBackground(this.bgColor);
		this.grid[x][y] = false;
	}
	//������ ��� ��׶���� ����
	public void setCopyBlock(){
		Block[] tempBlock = this.currentItem.getBlock();
		for (int i=0; i<tempBlock.length; i++){
			setBack(tempBlock[i].getX(), tempBlock[i].getY(), this.currentItem.getColor());
		}
		this.currentItem.setReadyLocation();	//�����ġ�� ���ư���
	}
	//�پ��ֱ� üũ
	public void checkLine(){
		for (int i=0; i<grid[0].length; i++){	// i = Y�� = ROW
//			System.out.println(
			boolean isLine = true;
			for (int p=0; p<grid.length; p++){	// p = X�� = Column
//				System.out.print(p+","+i+" : " + grid[p][i]);
				if(!grid[p][i]){	//�ϳ��� ������ ������ break;
					isLine = false;
					break;
				}
			}
			if(isLine){	//�پ���
				deleteLine(i);
				System.out.println(i + "�� ����");
			}
		}
	}
	//�پ��ְ� ������ ��ĭ�� ������
	public void deleteLine(int line){
		boolean temp[] = new boolean[xCnt];
		JPanel	tempPanel[] = new JPanel[xCnt];

		
		for (int i=line; i>0; i--){		// i = �� = Y
			for (int p=0; p<grid.length; p++){	// p = �� = X
				if(i==line){	//������ ���������� ����
					tempPanel[p] = background[p][i];
					tempPanel[p].setLocation(p*this.area,0);
				}
				//����� ��ĭ�� ������
				grid[p][i] = grid[p][i-1];
				background[p][i] = background[p][i-1];
				background[p][i].setLocation(p*this.area, i*this.area);
			}
		}
		//������ ������ �ø���
		for (int i=0; i<grid.length; i++){
			background[i][0] = tempPanel[i];
			setEmptyBack(i,0);
		}
	}
	//����Ʈ������� �ӽ�
	public void printInfo(){
		Block temp = this.currentItem.getCurrentXY();
		System.out.println("x : " + temp.getX() + ", y : " + temp.getY());
	}
	//������ ȸ��üũ -> ȸ��
	public void goRotate(){
		Block[] tempBlock = this.currentItem.getNextBlock();
		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX();
			int y = tempBlock[i].getY();
			if(x<0 || x>=this.xCnt || y+1>=this.yCnt || this.grid[x][y]){
				return;
			}
		}
		this.currentItem.moveRotate();
	}
	//�����۴ٿ�üũ -> �̵�
	public boolean goDown(){
		Block[] tempBlock = this.currentItem.getBlock();
		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX();
			int y = tempBlock[i].getY() + 1;
			if(y+1 >= this.yCnt || this.grid[x][y]){
				if(!this.isKey)	gameEnd();	//���ӳ�
				setCopyBlock();	//��׶���� ����
				checkLine();	//�پ��ֱ� üũ
				setNewItem();	//���������� ����
				return false;
			}
		}
		this.currentItem.moveDown();
		return true;
	}
	//�����ۿ������̵�üũ -> �̵�
	public void goRight(){
		Block[] tempBlock = this.currentItem.getBlock();

		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX()+1;
			int y = tempBlock[i].getY();
			if(x >= this.xCnt || this.grid[x][y]){
				return;
			}
		}
		this.currentItem.moveRight();
	}
	//�����ۿ����̵�üũ -> �̵�
	public void goLeft(){
		Block[] tempBlock = this.currentItem.getBlock();

		for (int i=0; i<tempBlock.length; i++){
			int x = tempBlock[i].getX()-1;
			int y = tempBlock[i].getY();
			if(x < 0 || this.grid[x][y]){
				return;
			}
		}
		this.currentItem.moveLeft();
	}
	//�������ֱ� üũ -> ���ֱ�
	//Ű����׼Ǹ�����
	public void keyPressed(KeyEvent e){
		if(!this.isKey)	return;
		switch (e.getKeyCode()){
			case KeyEvent.VK_DOWN:
				goDown();
				break;
			case KeyEvent.VK_LEFT:
				goLeft();
				break;
			case KeyEvent.VK_RIGHT:
				goRight();
				break;
			case KeyEvent.VK_UP:
				goRotate();
				break;
			case KeyEvent.VK_SPACE:
				while(goDown()){}
				break;
		}
	}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){}
	//��������üũ	
	public void gameEnd(){
		JOptionPane.showMessageDialog(null, "������ ����Ǿ����ϴ�.", "��������", JOptionPane.ERROR_MESSAGE);
		t.stop();
	}


	//���������
	public void run(){
		try
		{
			while(true){
				Thread.sleep(this.time);
				//�ǳ������̸� Ű������ ����X
				if(this.currentItem.getCurrentXY().getY() < 3)	this.isKey = false;
				else	this.isKey = true;
				goDown();		//�����۹������̵�
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) 
	{
		new Tetris("Tetris by 1��");
	}
}
