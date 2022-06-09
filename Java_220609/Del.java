package study0609;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


class Del extends Frame implements ActionListener {
	//디비 접속 정보 
	Connection conn = null;
	String url = "jdbc:mysql://127.0.0.1:3306/study?useUnicode=true&characterEncoding=utf8";
	String id = "root";// ID
	String pass = "qwer";// 비밀번호	
	
	//디비 데이타 삽입용
	PreparedStatement pstmt = null;	
		
	//디비 조회용
	Statement stmt = null;
	ResultSet rs = null;
	
	String result="";//텍스트에이리어에 누적해서 한번에 출력하기위해서 
			
	//윈도우 중앙배치를 위한 변수 
	private Dimension dimen, dimen1;
	private int xpos, ypos;
	
	
	Label lbTitle = new Label("회원삭제화면");	
	Label lbId = new Label("아이디 :");

	Label lbName = new Label("이름 :");
	Label lbHp = new Label("전번 :");
	Label lbAddr = new Label("주소 :");

	Button btnCheck = new Button("찾기");	
	Button btnDel = new Button("삭제완료");
	
	TextField tfId  = new TextField(20);	

	Label lbName2  = new Label("삭제이름대기..");
	Label lbHp2  = new Label("삭제전번대기..");
	Label lbAddr2  = new Label("삭제주소대기..");
	
	public Del() {
		super("회원삭제화면");
		this.init();
		this.start();		
		this.setSize(450, 450);	
		dimen = Toolkit.getDefaultToolkit().getScreenSize();
		dimen1 = this.getSize();
		xpos = (int) (dimen.getWidth() / 2 - dimen1.getWidth() / 2);
		ypos = (int) (dimen.getHeight() / 2 - dimen1.getHeight() / 2);
		this.setLocation(xpos, ypos);
		this.setVisible(true);	
		
	}

	public void init() {
		// 앱솔루트방식  : 직접 좌표 입력방식.
		this.setLayout(null);

		
		Font font20 = new Font("SansSerif", Font.BOLD, 20);
		Font font15 = new Font("SansSerif", Font.BOLD, 15);
		Font font10 = new Font("SansSerif", Font.BOLD, 10);
		
		//타이틀
		this.add(lbTitle);
		lbTitle.setBounds(120, 50, 300, 30);		
		lbTitle.setFont(font20);
		
		this.add(lbId);
		lbId.setBounds(50, 100, 100, 30);
		lbId.setFont(font15);
		
		this.add(tfId);
		tfId.setBounds(170, 100, 150, 30);
		tfId.setFont(font15);	
		
		this.add(btnCheck);
		btnCheck.setBounds(330, 100, 100, 30);
		btnCheck.setFont(font15);	

		this.add(lbName);
		lbName.setBounds(50, 150, 100, 30);
		lbName.setFont(font15);
		
		this.add(lbName2);
		lbName2.setBounds(170, 150, 150, 30);
		lbName2.setFont(font15);
	
		this.add(lbHp);
		lbHp.setBounds(50, 200, 100, 30);
		lbHp.setFont(font15);
		
		this.add(lbHp2);
		lbHp2.setBounds(170, 200, 150, 30);
		lbHp2.setFont(font15);

		this.add(lbAddr);
		lbAddr.setBounds(50, 250, 100, 30);
		lbAddr.setFont(font15);
		
		this.add(lbAddr2);
		lbAddr2.setBounds(170, 250, 150, 30);
		lbAddr2.setFont(font15);
		

		this.add(btnDel);
		btnDel.setBounds(120, 300, 150, 30);
		btnDel.setFont(font15);
		
		//처음 시작시 테이블 조회해서 뿌려주기
		//loadData();
		
	}

	public void start() {
		
		 btnCheck.addActionListener(this); 
		 btnDel.addActionListener(this); 
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCheck)
		{
			String id = tfId.getText();
			if(id.equals("")) {dlgMsg("아이디를 입력해주세요.");return;}
			
			//아이디로 해당 정보를 찾아서 입력창에 가져와서 확인후삭제
			search(id);
			
		}
		else if(e.getSource() == btnDel)
		{
			dlgMsg("DB에서 데이타가 삭제됩니다.");
			delMember();
		}
		
	}
	
	void delMember()
	{
		String delId = tfId.getText();
		
		String query = "delete from member where id = ?";
		
		try {
			PreparedStatement pstmt = conn.prepareStatement(query);
			pstmt.setString(1, delId);
			pstmt.executeUpdate();
			pstmt.close();
			System.err.println("회원 삭제 완료!!");
		} catch (SQLException ee) {
			System.err.println("회원 삭제 실패!!");
			
		}
		
		lbName2.setText("내용이 삭제됨.");
		lbHp2.setText("내용이 삭제됨.");
		lbAddr2.setText("내용이 삭제됨.");
		tfId.setText("");
	}
	void search(String delId)
	{
		
		//디비에서 정보를 가져와서 텍스트에이리어에 뿌려주기				
		try {
			//디비 접속을 위한 conn변수 생성
			conn = DriverManager.getConnection(url, id, pass);
			//디비에연결
			stmt = conn.createStatement();
			//디비에서 조회한 모든 데이타를 가져와서 rs에 저장
			String idSelectQuery = "select * from members where id='"+delId+"'";
			rs = stmt.executeQuery(idSelectQuery);
			//디비에서 조회한 모든데이타가 저장된 r의 값을 한줄씩 체크해서 없을때까지 반복
			while (rs.next()) {
				
				lbName2.setText(rs.getString("name"));
				lbHp2.setText(rs.getString("phone"));
				lbAddr2.setText(rs.getString("addr"));						
					
			}

			rs.close();
		} catch (SQLException error) {
			System.err.println("error = " + error.toString());			
		}	
	}
	
	//뭔가 알림창으로 공통으로 쓰기위해서 만듬.
			void dlgMsg(String msg)
			{
				final Dialog dlg = new Dialog(this, "알림", true);
				dlg.setLayout(null);	
				
				Font font15 = new Font("SansSerif", Font.BOLD, 15);
				Font font10 = new Font("SansSerif", Font.BOLD, 10);
				
				Label lbTitle = new Label(msg);
				
				
				Button bt = new Button("확인");
				Panel pp = new Panel(new FlowLayout());
				pp.add(bt);
				bt.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dlg.setVisible(false);
					}
				});
				
				dlg.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						dlg.setVisible(false);
					}
				});
				
				dlg.add(lbTitle);
				lbTitle.setBounds(30, 50, 250, 30);		
				lbTitle.setFont(font15);
				
				
				
				dlg.add(bt);
				bt.setBounds(100, 100, 100, 30);		
				bt.setFont(font15);
				
				
				dlg.setLocation(xpos, 400);
				dlg.add("South", pp);
				dlg.setSize(300, 200);
				dlg.setVisible(true);
			}
		
}

