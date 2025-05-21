package main.gui;

import javax.swing.*;
import java.awt.*;

// 모든 패널이 상속받을 공통 패널
public abstract class BasePanel extends JPanel {
	protected VolunteerServiceGUI mainFrame;
	
    public BasePanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setOpaque(true);
        setBackground(Color.WHITE); // 공통 배경색
        setFont(new Font("맑은 고딕", Font.PLAIN, 14)); // 공통 폰트
    }

    // 화면이 보여질 때 자동 호출 (필요시 오버라이드)
    public void onShow() {}

    // 화면 전환(이름으로)
    protected void goTo(String panelName) {
        mainFrame.showPanel(panelName);
    }

    // 공통 메시지 박스
    protected void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    // 공통 입력 다이얼로그
    protected String inputDialog(String msg) {
        return JOptionPane.showInputDialog(this, msg);
    }

    // 공통 확인 다이얼로그
    protected boolean confirmDialog(String msg) {
        return JOptionPane.showConfirmDialog(this, msg, "확인", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    // 공통 폼 초기화(필요시 오버라이드)
    public void resetForm() {}
    
    protected abstract void initializePanel();
}
