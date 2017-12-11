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
	//멤버필드 정의하기
	JTextField inputNum, inputName, inputPhone, inputRegDate;
	JButton saveBtn, deleteBtn, updateBtn;
	//테이블 모델
	DefaultTableModel model;
	//테이블의 참조값을 저장할 필드
	JTable table;
	
	//생성자
	public FriendFrame() {
		initUI();
	}
	//UI 초기화 작업 메소드
	public void initUI() {
		//레이아웃 설정
		setLayout(new BorderLayout());
		//상단 페널 객체 생성
		JPanel topPanel = new JPanel();
		
		//레이블 객체 생성
		JLabel label1 = new JLabel("번호");
		JLabel label2 = new JLabel("이름");
		JLabel label3 = new JLabel("핸드폰번호");
		JLabel label4 = new JLabel("날짜");
		
		//텍스트 필드 객체 생성
		inputNum = new JTextField(10);
		inputName = new JTextField(10);
		inputPhone = new JTextField(10);
		inputRegDate = new JTextField(10);
		
		saveBtn = new JButton("저장");
		deleteBtn = new JButton("삭제");
		updateBtn = new JButton("수정");
		
		//버튼에 ActionListener 등록
		saveBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		//버튼에 action command 등록
		saveBtn.setActionCommand("save");
		deleteBtn.setActionCommand("delete");
		updateBtn.setActionCommand("update");
		
		//번호와 날짜는 입력이나 수정을 할 수 없도록 설정
		inputNum.setEditable(false);
		inputRegDate.setEditable(false);
		
		//페널에 컴포넌트 추가 하기
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
		
		//프레임의 상단에 페널 배치하기
		add(topPanel, BorderLayout.NORTH);
		
		//테이블 칼럼 명을 String[] 에 담는다.
		String[] colNames = {"번호", "이름", "핸드폰", "날짜"};
		//기본 테이블 모델 객체 생성
		model = new DefaultTableModel(colNames,0);
		//JTable 객체 생성
		table = new JTable();
		//테이블에 모델 연결
		table.setModel(model);
		
		//스크롤 가능한 페널 객체
		JScrollPane sPanel = new JScrollPane(table);
		//페널을 프레임의 가운데에 배치
		add(sPanel, BorderLayout.CENTER);
		
		//프레임의 위치와 크기 설정
		setBounds(200, 200, 1000, 500);
		//보이도록 설정
		setVisible(true);
		//프레임을 닫았을 때 프로세스가 완전히 종료되도록 설정
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		//회원정보 출력
		displayFriend();
		
	}
	@Override
	public void actionPerformed(ActionEvent ae) {
		String command = ae.getActionCommand();
		if(command.equals("save")) {
			//입력한 이름과 핸드폰 읽어오기
			String name = inputName.getText();
			String phone = inputPhone.getText();
			//FriendDto 에 담는다.
			FriendDto friendDto = new FriendDto();
			friendDto.setName(name);
			friendDto.setPhone(phone);
			//FriendDao 를 이용해서 저장
			FriendDao friendDao = FriendDao.getInstance();
			boolean isSuccess = friendDao.insert(friendDto);
			if(isSuccess) {
				JOptionPane.showMessageDialog(this, "저장했습니다.");
			}else {
				JOptionPane.showMessageDialog(this, "저장 실패!");
			}
			//저장 후 입력한 이름과 핸드폰 비우기
			inputName.setText("");
			inputPhone.setText("");
		}else if(command.equals("delete")) {
			//예, 아니요, 취소 중에 어떤 버튼을 눌렀는지 정보가
			//int type 으로 리턴된다.
			int result = JOptionPane.showConfirmDialog(this, "삭제하겠습니까?");
			// 예 버튼을 누르지 않았다면
			if(result != JOptionPane.YES_OPTION) {
				return; //메소드 종료
			}
			
			//선택된 row 의 인덱스를 읽어온다.
			int selectedIndex = table.getSelectedRow();
			
			if(selectedIndex == -1) {
				JOptionPane.showMessageDialog(this, "삭제할 row 를 선택하세요");
				return; //메소드 종료
			}
			//삭제할 row 에 있는 획원 번호를 읽어 온다.
			int num = (int)table.getValueAt(selectedIndex, 0);
			//DB 에서 해당 회원정보를 삭제한다.
			FriendDao friendDao = FriendDao.getInstance();
			friendDao.delete(num);
		}else if(command.equals("update")) {
			//선택된 row 의 인덱스를 읽어온다.
			int selectedIndex = table.getSelectedRow();
			if(selectedIndex == -1) {
				JOptionPane.showMessageDialog(this, "수정할 row 를 선택하세요");
				return; //메소드 종료
			}
			//수정할 회원정보를 읽어와서
			int num = (int)table.getValueAt(selectedIndex, 0);
			String name = (String)table.getValueAt(selectedIndex, 1);
			String phone = (String)table.getValueAt(selectedIndex, 2);
			String regDate = (String)table.getValueAt(selectedIndex, 3);
			
			//FriendDto 객체에 담고
			FriendDto friendDto = new FriendDto(num, name, phone, regDate);
			//DB 에 수정 반영한다.
			FriendDao.getInstance().update(friendDto);
			JOptionPane.showMessageDialog(this, "수정하였습니다.");
								
			//저장 후 입력한 이름과 핸드폰 비우기
			inputName.setText("");
			inputPhone.setText("");
		}
		//회원 정보 다시 출력
		displayFriend();
		
	}//actionPerformed()
	
	//DB 에 있는 회원 정보를 JTable 에 출력하는 메소드
	public void displayFriend() {
		//회원 정보를 읽어온다.
		FriendDao friendDao = FriendDao.getInstance();
		List<FriendDto> list = friendDao.getList();
		//테이블의 내용을 지우고
		model.setRowCount(0);
		//다시 출력
		for(FriendDto tmp:list) {
			Object[] rowData = {tmp.getNum(), tmp.getName(), tmp.getPhone(), tmp.getRegDate()};
			model.addRow(rowData);
		}
		
	}
	//메인 메소드
	public static void main(String[] args) {
		new FriendFrame();
	}
	
}
