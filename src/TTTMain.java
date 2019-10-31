import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class TTTMain extends Container implements ActionListener{
	
	private boolean turnxo = false;
	private boolean winnerCrowned = false;
	private JLabel whosTurn;
	private Square[] squares = new Square[9];
	
	public TTTMain() {
		super();
		this.setLayout(new GridLayout(3,3));
		
		for(byte i=0;i<9;i++) {
			squares[i] = new Square(i);
			this.add(squares[i]);
			squares[i].addActionListener(this);
		}
	}

	public void setWhosTurn(JLabel whosTurn) {
		this.whosTurn = whosTurn;
		setTurnText();
	}

	private void setTurnText() {
		if(!winnerCrowned) whosTurn.setText((turnxo?'X':'O')+"'s turn");
		else whosTurn.setText("");
	}
	
	class Square extends JButton{
		boolean xo;
		boolean marked = false;
		byte id;
		
		public Square(byte id) {
			super("-");
			this.id = id;
			this.setActionCommand(""+id);
		}
		public boolean mark(boolean xo) {
			if(marked)
				return false;
			this.xo = xo;
			marked = true;
			this.setText(marking()+"");
			return true;
		}
		public boolean unmark() {
			boolean prev = marked;
			marked = false;
			this.setText(marking()+"");
			return prev!=marked;
		}
		
		public char marking() {
			if(marked) return xo?'x':'o';
			return '-';
		}
		
		public boolean check(boolean m) {
			if(!marked) return false;
			return xo==m;
		}
		public boolean check(Square s) {
			return check(s.xo)&&s.marked;
		}
		
		public boolean checkBoth(Square a, Square b) {
			return check(a)&&check(b);
		}
		
		public String toString() {
			return "{"+id+", "+marking()+"}";
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//System.out.println(e.getActionCommand());
		if(e.getActionCommand().equals("Restart")) {
			for(Square s : squares) {
				s.unmark();
				s.setEnabled(true);
			}
			winnerCrowned = false;
		}
		else if(!winnerCrowned){
			Square sq = (Square)e.getSource();
			boolean isvalid = sq.mark(turnxo);
			if(isvalid) {
				turnxo = !turnxo;
				setTurnText();
				
				if(checkWinner(sq)) {
					winnerCrowned = true;
					for(Square s : squares)
						s.setEnabled(false);
				}
			}
			//continue;
		}
		else {
			
		}
	}
	
	public static final byte[][][] checks = 
	{
		{{1,2},{3,6},{4,8}},//0					012
		{{0,2},{4,7}},		//1					345
		{{0,1},{4,6},{5,8}},//2					678
		{{0,6},{4,5}},		//3
		{{0,8},{1,7},{2,6},{3,5}},//4
		{{2,8},{3,4}},		//5
		{{0,3},{2,4},{7,8}},//6
		{{6,8},{1,4}},		//7
		{{0,4},{2,5},{6,7}}//8
	};
	
	public boolean checkWinner(Square sq){
		
		for(byte[] line : checks[sq.id]) {
			if(sq.checkBoth(squares[line[0]], squares[line[1]])) 
				return true;
		}
		
		return false;
	}
	
	public static void main(String[] args) {
		TTTMain ttt = new TTTMain();
		JFrame frame = new JFrame();
		JLabel whosTurn = new JLabel();
		JButton restartButton = new JButton("Restart");
		
		frame.setSize(400, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().add(ttt,BorderLayout.CENTER);
		
		restartButton.addActionListener(ttt);
		frame.getContentPane().add(restartButton,BorderLayout.SOUTH);
		ttt.setWhosTurn(whosTurn);
		frame.getContentPane().add(whosTurn,BorderLayout.NORTH);
		
		frame.setVisible(true);
	}

}
