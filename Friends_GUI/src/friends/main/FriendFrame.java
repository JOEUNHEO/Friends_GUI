package friends.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import friends.dao.FriendDao;
import friends.dto.FriendDto;

public class FriendFrame extends JFrame implements ActionListener{
	//����ʵ� �����ϱ�
	JTextField inputNum, inputName, inputPhone, inputRegDate;
	JButton saveBtn, deleteBtn, updateBtn;
	//���̺� ��
	DefaultTableModel model;
	//���̺��� �������� ������ �ʵ�
	JTable table;
	
	//������
	public FriendFrame() {
		initUI();
	}
	//UI �ʱ�ȭ �۾� �޼ҵ�
	public void initUI() {
		//���̾ƿ� ����
		setLayout(new BorderLayout());
		//��� ��� ��ü ����
		JPanel topPanel = new JPanel();
		
		//���̺� ��ü ����
		JLabel label1 = new JLabel("��ȣ");
		JLabel label2 = new JLabel("�̸�");
		JLabel label3 = new JLabel("�ڵ�����ȣ");
		JLabel label4 = new JLabel("��¥");
		
		//�ؽ�Ʈ �ʵ� ��ü ����
		inputNum = new JTextField(10);
		inputName = new JTextField(10);
		inputPhone = new JTextField(10);
		inputRegDate = new JTextField(10);
		
		saveBtn = new JButton("����");
		deleteBtn = new JButton("����");
		updateBtn = new JButton("����");
		
		//��ư�� ActionListener ���
		saveBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		//��ư�� action command ���
		saveBtn.setActionCommand("save");
		deleteBtn.setActionCommand("delete");
		updateBtn.setActionCommand("update");
		
		//��ȣ�� ��¥�� �Է��̳� ������ �� �� ������ ����
		inputNum.setEditable(false);
		inputRegDate.setEditable(false);
		
		//��ο� ������Ʈ �߰� �ϱ�
		topPanel.add(label1);
		topPanel.add(inputNum);
		topPanel.add(label2);
		topPanel.add(inputName);
		topPanel.add(label3);
		topPanel.add(inputPhone);
		topPanel.add(label4);
		topPanel.add(inputRegDate);
		topPanel.add(saveBtn);
		topPanel.add(deleteBtn);
		topPanel.add(updateBtn);
		
		//�������� ��ܿ� ��� ��ġ�ϱ�
		add(topPanel, BorderLayout.NORTH);
		
		//���̺� Į�� ���� String[] �� ��´�.
		String[] colNames = {"��ȣ", "�̸�", "�ڵ���", "��¥"};
		//�⺻ ���̺� �� ��ü ����
		model = new DefaultTableModel(colNames,0);
		//JTable ��ü ����
		table = new JTable();
		//���̺� �� ����
		table.setModel(model);
		
		//��ũ�� ������ ��� ��ü
		JScrollPane sPanel = new JScrollPane(table);
		//����� �������� ����� ��ġ
		add(sPanel, BorderLayout.CENTER);
		
		//�������� ��ġ�� ũ�� ����
		setBounds(200, 200, 1000, 500);
		//���̵��� ����
		setVisible(true);
		//�������� �ݾ��� �� ���μ����� ������ ����ǵ��� ����
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//ȸ������ ���
		displayFriend();
		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		if(command.equals("save")) {
			//�Է��� �̸��� �ڵ��� �о����
			String name = inputName.getText();
			String phone = inputPhone.getText();
			//FriendDto �� ��´�.
			FriendDto friendDto = new FriendDto();
			friendDto.setName(name);
			friendDto.setPhone(phone);
			//FriendDao �� �̿��ؼ� ����
			FriendDao friendDao = FriendDao.getInstance();
			boolean isSuccess = friendDao.insert(friendDto);
			if(isSuccess) {
				JOptionPane.showMessageDialog(this, "�����߽��ϴ�.");
			}else {
				JOptionPane.showMessageDialog(this, "���� ����!");
			}
			//���� �� �Է��� �̸��� �ڵ��� ����
			inputName.setText("");
			inputPhone.setText("");
		}else if(command.equals("delete")) {
			//��, �ƴϿ�, ��� �߿� � ��ư�� �������� ������
			//int type ���� ���ϵȴ�.
			int result = JOptionPane.showConfirmDialog(this, "�����ϰڽ��ϱ�?");
			// �� ��ư�� ������ �ʾҴٸ�
			if(result != JOptionPane.YES_OPTION) {
				return; //�޼ҵ� ����
			}
			
			//���õ� row �� �ε����� �о�´�.
			int selectedIndex = table.getSelectedRow();
			
			if(selectedIndex == -1) {
				JOptionPane.showMessageDialog(this, "������ row �� �����ϼ���");
				return; //�޼ҵ� ����
			}
			//������ row �� �ִ� ȹ�� ��ȣ�� �о� �´�.
			int num = (int)table.getValueAt(selectedIndex, 0);
			//DB ���� �ش� ȸ�������� �����Ѵ�.
			FriendDao friendDao = FriendDao.getInstance();
			friendDao.delete(num);
		}else if(command.equals("update")) {
			//���õ� row �� �ε����� �о�´�.
			int selectedIndex = table.getSelectedRow();
			if(selectedIndex == -1) {
				JOptionPane.showMessageDialog(this, "������ row �� �����ϼ���");
				return; //�޼ҵ� ����
			}
			//������ ȸ�������� �о�ͼ�
			int num = (int)table.getValueAt(selectedIndex, 0);
			String name = (String)table.getValueAt(selectedIndex, 1);
			String phone = (String)table.getValueAt(selectedIndex, 2);
			String regDate = (String)table.getValueAt(selectedIndex, 3);
			
			//FriendDto ��ü�� ���
			FriendDto friendDto = new FriendDto(num, name, phone, regDate);
			//DB �� ���� �ݿ��Ѵ�.
			FriendDao.getInstance().update(friendDto);
			JOptionPane.showMessageDialog(this, "�����Ͽ����ϴ�.");
								
			//���� �� �Է��� �̸��� �ڵ��� ����
			inputName.setText("");
			inputPhone.setText("");
		}
		//ȸ�� ���� �ٽ� ���
		displayFriend();
		
	}//actionPerformed()
	
	//DB �� �ִ� ȸ�� ������ JTable �� ����ϴ� �޼ҵ�
	public void displayFriend() {
		//ȸ�� ������ �о�´�.
		FriendDao friendDao = FriendDao.getInstance();
		List<FriendDto> list = friendDao.getList();
		//���̺��� ������ �����
		model.setRowCount(0);
		//�ٽ� ���
		for(FriendDto tmp:list) {
			Object[] rowData = {tmp.getNum(), tmp.getName(), tmp.getPhone(), tmp.getRegDate()};
			model.addRow(rowData);
		}
		
	}
	//���� �޼ҵ�
	public static void main(String[] args) {
		new FriendFrame();
	}
	
}
