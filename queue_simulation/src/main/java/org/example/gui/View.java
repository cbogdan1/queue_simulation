package org.example.gui;

import javax.naming.ldap.Control;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class View extends JFrame {
    private JPanel mainPanel;
    private JPanel  secPanel;

    private JButton startSimulation;


    private JPanel inputPanel;
    private JLabel timeLimitLabel;
    private JLabel maxProcessingTimeLabel;
    private JLabel minProcessingTimeLabel;
    private JLabel maxArrivalTimeLabel;
    private JLabel minArrivalTimeLabel;
    private JLabel numberOfServersLabel;
    private JLabel numberOfClientsLabel;



    private JTextField timeLimitField;
    private JTextField maxProcessingTimeField;
    private JTextField minProcessingTimeField;
    private JTextField maxArrivalTimeField;
    private JTextField minArrivalTimeField;
    private JTextField numberOfServersField;
    private JTextField numberOfClientsField;

    private JPanel textPanel;
    private JTextPane textField;
    private JScrollPane textScroll;

    private JPanel dataPanel;

    private JTable dataTable;
    private JScrollPane dataScrollPane;
    private String[] colData;


    private Controller controller=new Controller(this);
    public View(){
        prepareGUI();
    }
    public void prepareGUI(){
        this.setSize(1280, 900);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.mainPanel = new JPanel(new GridLayout(3, 1));
        prepareInputData();
        prepareData();
        this.setContentPane(this.mainPanel);
    }
    public void prepareInputData(){
        this.inputPanel = new JPanel(new GridLayout(7, 2));

        this.startSimulation=new JButton("Start Simulation");
        this.startSimulation.setActionCommand("Start");
        this.startSimulation.addActionListener(this.controller);

        this.timeLimitLabel=new JLabel("Time Limit:");
        this.maxProcessingTimeLabel=new JLabel("Max proccessing time:");
        this.minProcessingTimeLabel=new JLabel("Min proccessing time:");
        this.maxArrivalTimeLabel=new JLabel("Max Arrival time:");
        this.minArrivalTimeLabel=new JLabel("Min Arrival time:");
        this.numberOfServersLabel=new JLabel("Nr of servers(queues):");
        this.numberOfClientsLabel=new JLabel("Nr of tasks(clients):");

        this.timeLimitField=new JTextField();
        this.maxProcessingTimeField=new JTextField();
        this.minProcessingTimeField=new JTextField();
        this.maxArrivalTimeField=new JTextField();
        this.minArrivalTimeField=new JTextField();
        this.numberOfServersField=new JTextField();
        this.numberOfClientsField=new JTextField();


        this.inputPanel.add(timeLimitLabel);
        this.inputPanel.add(timeLimitField);
        this.inputPanel.add(maxProcessingTimeLabel);
        this.inputPanel.add(maxProcessingTimeField);
        this.inputPanel.add(minProcessingTimeLabel);
        this.inputPanel.add(minProcessingTimeField);
        this.inputPanel.add(maxArrivalTimeLabel);
        this.inputPanel.add(maxArrivalTimeField);
        this.inputPanel.add(minArrivalTimeLabel);
        this.inputPanel.add(minArrivalTimeField);
        this.inputPanel.add(numberOfServersLabel);
        this.inputPanel.add(numberOfServersField);
        this.inputPanel.add(numberOfClientsLabel);
        this.inputPanel.add(numberOfClientsField);


        this.secPanel = new JPanel(new GridLayout(3, 1));

        this.secPanel.add(startSimulation);
        this.mainPanel.add(inputPanel);
        this.mainPanel.add(secPanel);
    }

    public void prepareData() {
        this.dataPanel = new JPanel(new GridLayout(1, 4));
        this.textPanel = new JPanel(new GridLayout(1, 1));

        this.textField= new JTextPane();
        this.textScroll=new JScrollPane(textField);

        this.textPanel.add(textScroll);
        this.mainPanel.add(textPanel);

        this.colData = new String[]{"Time","Waiting Clients","Quees"};
        DefaultTableModel dataModel = new DefaultTableModel(this.colData, 0);
        this.dataTable = new JTable(dataModel);
        this.dataScrollPane = new JScrollPane(this.dataTable);
        this.dataPanel.add(this.dataScrollPane);
        this.secPanel.add(dataPanel);
    }
    public JTextField getTimeLimitField() {return this.timeLimitField;}
    public JTextField getMaxProcessingTimeField() {return this.maxProcessingTimeField;}
    public JTextField getMinProcessingTimeField() {return this.minProcessingTimeField;}
    public JTextField getMaxArrivalTimeField() {return this.maxArrivalTimeField;}
    public JTextField getMinArrivalTimeField() {return this.minArrivalTimeField;}
    public JTextField getNumberOfServersField() {return this.numberOfServersField;}
    public JTextField getNumberOfClientsField() {return this.numberOfClientsField;}

    public JTable getDataTable() {return this.dataTable;}
    public void appendToTextField(String text) {
        this.textField.setText(this.textField.getText() + text + "\n");
    }
}
