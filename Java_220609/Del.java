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
	//��� ���� ���� 
	Connection conn = null;
	String url = "jdbc:mysql://127.0.0.1:3306/study?useUnicode=true&characterEncoding=utf8";
	String id = "root";// ID
	String pass = "qwer";// ��й�ȣ	
	
	//��� ����Ÿ ���Կ�
	PreparedStatement pstmt = null;	
		
	//��� ��ȸ��
	Statement stmt = null;
	ResultSet rs = null;
	
	String result="";//�ؽ�Ʈ���̸�� �����ؼ� �ѹ��� ����ϱ����ؼ� 
			
	//������ �߾ӹ�ġ�� ���� ���� 
	private Dimension dimen, dimen1;
	private int xpos, ypos;
	
	
	Label lbTitle = new Label("ȸ������ȭ��");	
	Label lbId = new Label("���̵� :");

	Label lbName = new Label("�̸� :");
	Label lbHp = new Label("���� :");
	Label lbAddr = new Label("�ּ� :");

	Button btnCheck = new Button("ã��");	
	Button btnDel = new Button("�����Ϸ�");
	
	TextField tfId  = new TextField(20);	

	Label lbName2  = new Label("�����̸����..");
	Label lbHp2  = new Label("�����������..");
	Label lbAddr2  = new Label("�����ּҴ��..");
	
	public Del() {
		super("ȸ������ȭ��");
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
		// �ۼַ�Ʈ���  : ���� ��ǥ �Է¹��.
		this.setLayout(null);

		
		Font font20 = new Font("SansSerif", Font.BOLD, 20);
		Font font15 = new Font("SansSerif", Font.BOLD, 15);
		Font font10 = new Font("SansSerif", Font.BOLD, 10);
		
		//Ÿ��Ʋ
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
		
		//ó�� ���۽� ���̺� ��ȸ�ؼ� �ѷ��ֱ�
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
			if(id.equals("")) {dlgMsg("���̵� �Է����ּ���.");return;}
			
			//���̵�� �ش� ������ ã�Ƽ� �Է�â�� �����ͼ� Ȯ���Ļ���
			search(id);
			
		}
		else if(e.getSource() == btnDel)
		{
			dlgMsg("DB���� ����Ÿ�� �����˴ϴ�.");
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
			System.err.println("ȸ�� ���� �Ϸ�!!");
		} catch (SQLException ee) {
			System.err.println("ȸ�� ���� ����!!");
			
		}
		
		lbName2.setText("������ ������.");
		lbHp2.setText("������ ������.");
		lbAddr2.setText("������ ������.");
		tfId.setText("");
	}
	void search(String delId)
	{
		
		//��񿡼� ������ �����ͼ� �ؽ�Ʈ���̸�� �ѷ��ֱ�				
		try {
			//��� ������ ���� conn���� ����
			conn = DriverManager.getConnection(url, id, pass);
			//��񿡿���
			stmt = conn.createStatement();
			//��񿡼� ��ȸ�� ��� ����Ÿ�� �����ͼ� rs�� ����
			String idSelectQuery = "select * from members where id='"+delId+"'";
			rs = stmt.executeQuery(idSelectQuery);
			//��񿡼� ��ȸ�� ��絥��Ÿ�� ����� r�� ���� ���پ� üũ�ؼ� ���������� �ݺ�
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
	
	//���� �˸�â���� �������� �������ؼ� ����.
			void dlgMsg(String msg)
			{
				final Dialog dlg = new Dialog(this, "�˸�", true);
				dlg.setLayout(null);	
				
				Font font15 = new Font("SansSerif", Font.BOLD, 15);
				Font font10 = new Font("SansSerif", Font.BOLD, 10);
				
				Label lbTitle = new Label(msg);
				
				
				Button bt = new Button("Ȯ��");
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
