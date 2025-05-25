package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import main.VolunteerProgram;

public class ReservationPanel extends BasePanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<String> myProgramNames = new ArrayList<>(); // 표와 동기화되는 프로그램명 리스트

    public ReservationPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
        initializePanel();
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());
        String[] columns = {"프로그램명", "날짜", "상태"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JButton cancelBtn = new JButton("예약취소");
        JButton refreshBtn = new JButton("새로고침");
        JButton backBtn = new JButton("메인으로");

        JPanel btnPanel = new JPanel();
        btnPanel.add(cancelBtn);
        btnPanel.add(refreshBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        cancelBtn.addActionListener(e -> cancelReservation());
        refreshBtn.addActionListener(e -> refreshTable());
        backBtn.addActionListener(e -> mainFrame.showMainMenuPanel());
    }

    @Override
    public void onShow() {
        super.onShow();
        refreshTable();
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        myProgramNames.clear();

        if (loggedInUser == null) {
            showMessage("로그인 후 이용하세요.");
            return;
        }

        List<String> myPrograms = reservationManager.getMyReservations(loggedInUser.getId());
        List<String> myWaiting = reservationManager.getMyWaiting(loggedInUser.getId());

        if (myPrograms != null) {
            for (String prog : myPrograms) {
                VolunteerProgram p = programManager.getProgramByName(prog);
                if (p != null) {
                    tableModel.addRow(new Object[]{p.getName(), p.getDate(), "예약"});
                    myProgramNames.add(p.getName());
                }
            }
        }
        if (myWaiting != null) {
            for (String prog : myWaiting) {
                VolunteerProgram p = programManager.getProgramByName(prog);
                if (p != null) {
                    tableModel.addRow(new Object[]{p.getName(), p.getDate(), "대기"});
                    myProgramNames.add(p.getName());
                }
            }
        }
    }

    private void cancelReservation() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showMessage("취소할 예약을 선택하세요.");
            return;
        }
        if (row >= myProgramNames.size()) {
            showMessage("잘못된 선택입니다.");
            return;
        }
        String programToCancel = myProgramNames.get(row);

        int confirm = JOptionPane.showConfirmDialog(this,
                "[" + programToCancel + "] 예약을 정말 취소하시겠습니까?", "예약 취소 확인", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        boolean ok = reservationManager.cancelReservation(loggedInUser.getId(), programManager, myProgramNames, userManager);

        if (ok) {
            showMessage("예약이 취소되었습니다.");
            refreshTable();
        } else {
            showMessage("취소 실패(이미 확정되었거나 없는 예약)");
        }
    }
}
