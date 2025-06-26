package github;

import java.awt.*;
import javax.swing.*;
import java.text.*;
import java.util.*;

public class ToDo {

    private JFrame frame;
    private JTextField taskField, dateField;
    private DefaultListModel<String> taskListModel;
    private JList<String> taskList;
    private java.util.List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ToDo().createUI());
    }

    private void createUI() {
        frame = new JFrame("To-Do App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        frame.setLayout(new BorderLayout());

        // Input panel
        JPanel inputPanel = new JPanel();
        taskField = new JTextField(10);
        dateField = new JTextField(10);
        JButton addButton = new JButton("Add");
        inputPanel.add(new JLabel("Task:"));
        inputPanel.add(taskField);
        inputPanel.add(new JLabel("Due Date (dd-MM-yyyy):"));
        inputPanel.add(dateField);
        inputPanel.add(addButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Task list
        taskListModel = new DefaultListModel<>();
        taskList = new JList<>(taskListModel);
        frame.add(new JScrollPane(taskList), BorderLayout.CENTER);

        // Control panel
        JPanel controlPanel = new JPanel();
        JButton completeButton = new JButton("Mark Done");
        JButton deleteButton = new JButton("Delete");
        JButton sortButton = new JButton("Sort by Date");
        controlPanel.add(completeButton);
        controlPanel.add(deleteButton);
        controlPanel.add(sortButton);

        frame.add(controlPanel, BorderLayout.SOUTH);

        // Action Listeners
        addButton.addActionListener(e -> addTask());
        completeButton.addActionListener(e -> markAsDone());
        deleteButton.addActionListener(e -> deleteTask());
        sortButton.addActionListener(e -> sortTasks());
        frame.setVisible(true);
    }
    private void addTask() {
        String title = taskField.getText().trim();
        String dateStr = dateField.getText().trim();
        if (title.isEmpty() || dateStr.isEmpty()) {
            showMessage("Enter task and date.");
            return;
        }
        try {
            Date date = new SimpleDateFormat("dd-MM-yyyy").parse(dateStr);
            Task task = new Task(title, date);
            tasks.add(task);
            updateTaskList();
            taskField.setText("");
            dateField.setText("");
        } catch (Exception ex) {
            showMessage("Invalid date format.");
        }
    }
    private void updateTaskList() {
        taskListModel.clear();
        for (Task task : tasks) {
            taskListModel.addElement(task.toString());
        }
    }

    private void markAsDone() {
        int index = taskList.getSelectedIndex();
        if (index >= 0) {
            tasks.get(index).setDone(true);
            updateTaskList();
        }
    }
    private void deleteTask() {
        int index = taskList.getSelectedIndex();
        if (index >= 0) {
            tasks.remove(index);
            updateTaskList();
        }
    }
    private void sortTasks() {
        tasks.sort(Comparator.comparing(Task::getDueDate));
        updateTaskList();
    }

    private void showMessage(String msg) {
        JOptionPane.showMessageDialog(frame, msg);
    }

    class Task {
        private String title;
        private Date dueDate;
        private boolean done;

        public Task(String title, Date dueDate) {
            this.title = title;
            this.dueDate = dueDate;
        }

        public void setDone(boolean done) {
            this.done = done;
        }

        public Date getDueDate() {
            return dueDate;
        }

        @Override
        public String toString() {
            return (done ? "[Done] " : "[Pending] ") + title + " - " +
                    new SimpleDateFormat("dd-MM-yyyy").format(dueDate);
        }
    }
}
