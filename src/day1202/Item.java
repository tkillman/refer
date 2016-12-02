package day1202;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

//��� Ŭ����
class Block
{
	private int x;
	private int y;
	//������
	public Block(){
	}
	public Block(int x, int y){
		this.x = x;
		this.y = y;
	}
	//�ش� ����Ʈ��ŭ ����
	public void move(int xPlus, int yPlus){
		this.x += xPlus;
		this.y += yPlus;
	}
	//X����Ʈ ��ȯ
	public int getX(){
		return this.x;
	}
	//Y����Ʈ ��ȯ
	public int getY(){
		return this.y;
	}
	//�ڽ� ��ȯ
	public Block getBlock(){
		return this;
	}
	//XY����
	public void setXY(int x, int y){
		this.x = x;
		this.y = y;
	}
}

//������ Ŭ����
public class Item 
{
	JPanel[] panel;		//�ǳ�
	Block[] block;		//��������Ʈ
	Block[][] block_info;		//�� ������ ����Ʈ����
	//���ǹ迭 0~3 ���� 0-0�� 1-90�� 2-180�� 3-270��
	//���� �迭�� �ǳ� ����
	Block currentXY;
	int cnt;				//���ǳڰ���
	int angle;				//�Ѱ�������
	int current_angle;		//���簢����
	int xCnt;				//���ΰ�

	Color color;		//��
	int area;			//����
	
	public Item(int area, int angle, int cnt, int xCnt){
		this.angle = angle;
		this.cnt = cnt;
		this.panel = new JPanel[cnt];				//�ǳڰ��� ����
		this.block = new Block[cnt];				//����Ʈ ����
		this.block_info = new Block[angle][cnt];	//����Ʈ ����, ��������
		this.area = area;									
		this.currentXY = new Block(0,0);			//���簪
		this.xCnt = xCnt;

		for (int i=0; i<cnt; i++){			//�гλ���
			this.panel[i] = new JPanel();
		}
	}
	public void setDefaultRandom(){
		this.current_angle = (int)(Math.random() * this.angle);
		this.block = this.block_info[this.current_angle];
	}
	//�����̳ʿ� ���
	public void setItem(Container c){
		for (int i=0; i<panel.length; i++){
			panel[i].setBackground(this.color);		//����
			panel[i].setSize(area, area);			//����
			panel[i].setLocation(((block[i].getX()) * area)-100, ((block[i].getY()) * area)-100);	//�⺻��ġ �Ⱥ��̴°��� ����
			panel[i].setBorder(new SoftBevelBorder(BevelBorder.RAISED));
			c.add(panel[i]);	//�����̳ʿ� ���
		}
	}
	//������ġ����
	public void setNextLocation(){
		for (int i=0; i<panel.length; i++){
			int x = block[i].getX() + (xCnt-3);
			int y = block[i].getY() + 1;
			panel[i].setLocation(x * area, y * area);
		}
		this.currentXY.setXY((xCnt-3),1);
	}
	//������ġ����
	public void setDefaultLocation(){
		for (int i=0; i<panel.length; i++){
			int x = block[i].getX() + (int)(xCnt/2-2);
			int y = block[i].getY() +2;
			panel[i].setLocation(x * area, y * area);
		}
		this.currentXY.setXY((int)(xCnt/2-2),2);
	}
	//������ ��ġ����
	public void setReadyLocation(){
		for (int i=0; i<panel.length; i++){
			panel[i].setLocation(((block[i].getX()) * area)-100, ((block[i].getY()) * area)-100);
		}
	}
	//������ġ����
	public void setCurrentXY(int x, int y){
		this.currentXY.move(x,y);
	}
	//������ġ��ȯ
	public Block getCurrentXY(){
		return this.currentXY;
	}
	//���� ����Ʈ ����
	public Block[] getBlock(){
		Block[] temp = new Block[cnt];
		for (int i=0; i<block.length; i++){
			int x = block[i].getX() + this.currentXY.getX();
			int y = block[i].getY() + this.currentXY.getY();
			temp[i] = new Block(x,y);
		}
		return temp;
	}
	//���������ϰ����� ����Ʈ���� ��ȯ
	public Block[] getNextBlock(){
		int nextAngle;
		if(this.angle==1)	return getBlock();	//������1�����̸� ����
		else if(this.angle-1 == this.current_angle)	nextAngle=0;	//�������ޱ��̸� 1���ޱ۷�
		else	nextAngle=this.current_angle+1;	//�������� ����
		
		Block[] temp = new Block[cnt];
		for (int i=0; i<block.length; i++){
			int x = block_info[nextAngle][i].getX() + this.currentXY.getX();
			int y = block_info[nextAngle][i].getY() + this.currentXY.getY();
			temp[i] = new Block(x,y);
		}
		return temp;
	}
	//����ޱ۸���
	public int getCurrentAngle(){
		return this.current_angle;
	}
	//������Ʈ
	public void moveRotate(){
		if(this.angle==1)	return;	//������1�����̸� ����
		if(this.current_angle+1 == this.angle){	//�ְ����� ó��������
			this.block = this.block_info[0];
			this.current_angle = 0;
		}else{
			this.current_angle++;
			this.block = this.block_info[this.current_angle];
		}
		this.setMove();
	}
	//������ ����Ʈ ������ �ǳڿ� �����Ͽ� �������� 
	public void setMove(){
		for (int i=0; i<panel.length; i++){
			//�������� x,y���� ����x,y����Ʈ���� ���Ѱ��� ��area���� ���Ѵ�.
			int x = this.block[i].getX() + this.currentXY.getX();
			int y = this.block[i].getY() + this.currentXY.getY();;
			panel[i].setLocation(x * area, y * area);
		}
	}
	//�Ʒ��� ��ĭ ������
	public void moveDown(){
		this.currentXY.move(0,1);		
		this.setMove();
	}
	//���������� ��ĭ ������
	public void moveRight(){
		this.currentXY.move(1,0);
		this.setMove();
	}
	//�������� ��ĭ ������
	public void moveLeft(){
		this.currentXY.move(-1,0);		
		this.setMove();
	}
	//���� �� ����
	public Color getColor(){
		return this.color;
	}
	//���� �� ����
	public void setColor(Color c){
		this.color = c;
		for (int i=0; i<panel.length; i++){
			panel[i].setBackground(this.color);
		}
	}
}





//�簢�� 
class Rect extends Item
{
	public Rect(int area, Container con, int xCnt){
		super(area, 1, 4, xCnt);	//��������, ��������, �ǳڰ���

		block_info[0][0] = new Block(0,0);
		block_info[0][1] = new Block(0,1);
		block_info[0][2] = new Block(1,0);
		block_info[0][3] = new Block(1,1);

		this.setDefaultRandom();	//��������
		this.setItem(con);			//�����̳ʿ� ���
	}
}

//�����
class OneThree extends Item
{
	public OneThree(int area, Container con, int xCnt){
		super(area, 4, 4, xCnt);	//��������, ��������, �ǳڰ���

		block_info[0][0] = new Block(0,0);
		block_info[0][1] = new Block(0,1);
		block_info[0][2] = new Block(1,1);
		block_info[0][3] = new Block(2,1);

		block_info[1][0] = new Block(0,2);
		block_info[1][1] = new Block(1,2);
		block_info[1][2] = new Block(1,1);
		block_info[1][3] = new Block(1,0);

		block_info[2][0] = new Block(2,1);
		block_info[2][1] = new Block(2,0);
		block_info[2][2] = new Block(1,0);
		block_info[2][3] = new Block(0,0);

		block_info[3][0] = new Block(1,0);
		block_info[3][1] = new Block(0,0);
		block_info[3][2] = new Block(0,1);
		block_info[3][3] = new Block(0,2);

		this.setDefaultRandom();	//��������
		this.setItem(con);			//�����̳ʿ� ���
	}
}

//����� �ݴ�
class ThreeOne extends Item
{
	public ThreeOne(int area, Container con, int xCnt){
		super(area, 4, 4, xCnt);	//��������, ��������, �ǳڰ���

		block_info[0][0] = new Block(0,1);
		block_info[0][1] = new Block(0,0);
		block_info[0][2] = new Block(1,0);
		block_info[0][3] = new Block(2,0);

		block_info[1][0] = new Block(1,2);
		block_info[1][1] = new Block(0,2);
		block_info[1][2] = new Block(0,1);
		block_info[1][3] = new Block(0,0);

		block_info[2][0] = new Block(2,0);
		block_info[2][1] = new Block(2,1);
		block_info[2][2] = new Block(1,1);
		block_info[2][3] = new Block(0,1);

		block_info[3][0] = new Block(0,0);
		block_info[3][1] = new Block(1,0);
		block_info[3][2] = new Block(1,1);
		block_info[3][3] = new Block(1,2);

		this.setDefaultRandom();	//��������
		this.setItem(con);			//�����̳ʿ� ���
	}
}

//����
class LineBlock extends Item
{
	public LineBlock(int area, Container con, int xCnt){
		super(area, 2, 4, xCnt);	//��������, ��������, �ǳڰ���

		block_info[0][0] = new Block(0,-1);
		block_info[0][1] = new Block(0,0);
		block_info[0][2] = new Block(0,1);
		block_info[0][3] = new Block(0,2);

		block_info[1][0] = new Block(-1,0);
		block_info[1][1] = new Block(0,0);
		block_info[1][2] = new Block(1,0);
		block_info[1][3] = new Block(2,0);

		this.setDefaultRandom();	//��������
		this.setItem(con);			//�����̳ʿ� ���
	}
}

//�� ����~ ����
class Triangle extends Item
{
	public Triangle(int area, Container con, int xCnt){
		super(area, 4, 4, xCnt);	//��������, ��������, �ǳڰ���

		block_info[0][0] = new Block(1,0);
		block_info[0][1] = new Block(0,1);
		block_info[0][2] = new Block(1,1);
		block_info[0][3] = new Block(2,1);

		block_info[1][0] = new Block(0,0);
		block_info[1][1] = new Block(0,1);
		block_info[1][2] = new Block(0,2);
		block_info[1][3] = new Block(1,1);

		block_info[2][0] = new Block(0,0);
		block_info[2][1] = new Block(1,0);
		block_info[2][2] = new Block(2,0);
		block_info[2][3] = new Block(1,1);

		block_info[3][0] = new Block(0,1);
		block_info[3][1] = new Block(1,0);
		block_info[3][2] = new Block(1,1);
		block_info[3][3] = new Block(1,2);

		this.setDefaultRandom();	//��������
		this.setItem(con);			//�����̳ʿ� ���
	}
}

//_|- ����? ����
class RightBlock extends Item
{
	public RightBlock(int area, Container con, int xCnt){
		super(area, 2, 4, xCnt);	//��������, ��������, �ǳڰ���

		block_info[0][0] = new Block(0,0);
		block_info[0][1] = new Block(0,1);
		block_info[0][2] = new Block(1,1);
		block_info[0][3] = new Block(1,2);

		block_info[1][0] = new Block(1,0);
		block_info[1][1] = new Block(0,0);
		block_info[1][2] = new Block(0,1);
		block_info[1][3] = new Block(-1,1);

		this.setDefaultRandom();	//��������
		this.setItem(con);			//�����̳ʿ� ���
	}
}

//-|_ ���� ������
class LeftBlock extends Item
{
	public LeftBlock(int area, Container con, int xCnt){
		super(area, 2, 4, xCnt);	//��������, ��������, �ǳڰ���

		block_info[0][0] = new Block(0,0);
		block_info[0][1] = new Block(1,0);
		block_info[0][2] = new Block(1,1);
		block_info[0][3] = new Block(2,1);

		block_info[1][0] = new Block(0,1);
		block_info[1][1] = new Block(0,0);
		block_info[1][2] = new Block(1,0);
		block_info[1][3] = new Block(1,-1);

		this.setDefaultRandom();	//��������
		this.setItem(con);			//�����̳ʿ� ���
	}
}