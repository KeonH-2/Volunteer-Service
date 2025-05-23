package main.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import main.VolunteerProgram;

public class ProgramListPanel extends BasePanel {
    private JTable table;
    private DefaultTableModel tableModel;

    public ProgramListPanel(main.VolunteerServiceGUI mainFrame) {
        super(mainFrame);
    }

    @Override
    protected void initializePanel() {
        setLayout(new BorderLayout());

        String[] columns = {"이름", "날짜", "장소", "카테고리", "정원", "시간"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel filterPanel = new JPanel();
        JTextField locationField = new JTextField(7);
        JTextField dateField = new JTextField(7);
        JTextField categoryField = new JTextField(7);
        JButton filterBtn = new JButton("검색");
        JButton detailBtn = new JButton("상세보기");
        JButton reserveBtn = new JButton("예약신청");
        JButton backBtn = new JButton("메인으로");

        filterPanel.add(new JLabel("장소:"));
        filterPanel.add(locationField);
        filterPanel.add(new JLabel("날짜:"));
        filterPanel.add(dateField);
        filterPanel.add(new JLabel("카테고리:"));
        filterPanel.add(categoryField);
        filterPanel.add(filterBtn);

        add(filterPanel, BorderLayout.NORTH);

        JPanel btnPanel = new JPanel();
        btnPanel.add(detailBtn);
        btnPanel.add(reserveBtn);
        btnPanel.add(backBtn);
        add(btnPanel, BorderLayout.SOUTH);

        filterBtn.addActionListener(e -> {
            String loc = locationField.getText().trim();
            String date = dateField.getText().trim();
            String cat = categoryField.getText().trim();
            List<VolunteerProgram> filtered = programManager.filterPrograms(loc, date, cat);
            refreshTable(filtered);
        });

        detailBtn.addActionListener(e -> showDetail());
        reserveBtn.addActionListener(e -> reserveSelected());
        backBtn.addActionListener(e -> mainFrame.showMainMenuPanel());
    }

    private void refreshTable(List<VolunteerProgram> list) {
        tableModel.setRowCount(0);
        for (VolunteerProgram p : list) {
            tableModel.addRow(new Object[]{
                p.getName(), p.getDate(), p.getLocation(), p.getCategory(),
                p.getMaxParticipants(), p.getHours()
            });
        }
    }

    @Override
    public void onShow() {
        refreshTable(programManager.getPrograms());
    }

    private void showDetail() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showMessage("상세보기할 프로그램을 선택하세요.");
            return;
        }
        String name = (String) tableModel.getValueAt(row, 0);
        VolunteerProgram p = programManager.getProgramByName(name);
        if (p == null) return;
        JOptionPane.showMessageDialog(this,
                "이름: " + p.getName() + "\n날짜: " + p.getDate() + "\n장소: " + p.getLocation() +
                        "\n카테고리: " + p.getCategory() + "\n정원: " + p.getMaxParticipants() +
                        "\n시간: " + p.getHours() + "시간",
                "프로그램 상세", JOptionPane.INFORMATION_MESSAGE);
    }

    private void reserveSelected() {
        int row = table.getSelectedRow();
        if (row < 0) {
            showMessage("예약할 프로그램을 선택하세요.");
            return;
        }
        String name = (String) tableModel.getValueAt(row, 0);
        VolunteerProgram p = programManager.getProgramByName(name);
        if (p == null) return;
        reservationManager.makeReservation(loggedInUser.getId(), programManager, name);
        showMessage("예약 요청이 처리되었습니다.");
    }
}
