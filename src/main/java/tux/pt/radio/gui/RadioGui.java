package tux.pt.radio.gui;

import com.mpatric.mp3agic.*;
import tux.pt.radio.helper.AudioHelper;
import tux.pt.radio.helper.ID3v2Helper;
import tux.pt.radio.helper.TimerHelper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;


public class RadioGui extends JFrame {

    public RadioGui() {
        initComponents();
    }

    private String url, host, name;

    private static Random random;

    private static final TimerHelper timer = new TimerHelper();
    private static JComboBox<String> jComboBox1;
    private static JProgressBar jProgressBar1;
    private static InputStream inputstream = null;
    private static OutputStream outputstream = null;
    private static final byte[] bytes = new byte[2048];
    private static int length;

    private void initComponents() {
        JCheckBox enabledCheckBox = new JCheckBox();
        JCheckBox recordCheckBox = new JCheckBox();

        JPanel jPanel1 = new JPanel();
        JButton jButton1 = new JButton();
        jComboBox1 = new JComboBox<>();
        jProgressBar1 = new JProgressBar();

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("RadioNL");
        setBackground(new Color(255, 153, 51));
        setName("frame");
        setPreferredSize(new Dimension(507, 315));
        setResizable(false);
        setSize(new Dimension(507, 315));
        getContentPane().setLayout(null);

        jPanel1.setBackground(new Color(51, 51, 51));
        jPanel1.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, new Color(255, 153, 51)));
        jPanel1.setForeground(new Color(255, 255, 255));

        enabledCheckBox.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 12));
        enabledCheckBox.setForeground(new Color(255, 255, 255));
        enabledCheckBox.setText("Enabled");
        enabledCheckBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(255, 153, 0)));
        enabledCheckBox.addItemListener(this::enabledCheckBoxActionPerformed);

        recordCheckBox.setFont(new Font("Yu Gothic UI Semilight", 0, 12));
        recordCheckBox.setForeground(new Color(255, 255, 255));
        recordCheckBox.setText("Record");
        recordCheckBox.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 153, 0)));
        recordCheckBox.addItemListener(this::recordCheckBoxActionPerformed);
        recordCheckBox.setEnabled(true);

        jComboBox1.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 12));
        jComboBox1.setModel(new DefaultComboBoxModel<>(new String[]{"Haarlem105", "Barok", "NPORadio", "LansingerlandFM"}));
        jComboBox1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(255, 153, 0)));

        jButton1.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 12));
        jButton1.setText("Credits");
        jButton1.setActionCommand("jButton");
        jButton1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(255, 153, 0)));
        jButton1.addActionListener(this::jButton1ActionPerformed);

        jProgressBar1.setFont(new Font("Yu Gothic UI Semilight", Font.PLAIN, 10));
        jProgressBar1.setForeground(new Color(255, 153, 0));
        jProgressBar1.setToolTipText("Connected");
        jProgressBar1.setValue(0);
        jProgressBar1.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, new Color(255, 153, 0)));

        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jProgressBar1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(enabledCheckBox)
                                                        .addComponent(recordCheckBox))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(jButton1, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                                .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, 119, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(0, 0, 0))))).addContainerGap()));
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(enabledCheckBox)
                                        .addComponent(jComboBox1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(jButton1).addComponent(recordCheckBox))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 220, Short.MAX_VALUE)
                                .addComponent(jProgressBar1, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))
        );

        getContentPane().add(jPanel1);
        jPanel1.setBounds(0, 0, 507, 315);

        pack();
    }


    private void enabledCheckBoxActionPerformed(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                switch (jComboBox1.getSelectedIndex()) {
                    case 0:
                        url = "http://stream.haarlem105.nl:8000/haarlem105high.mp3";
                        host = "haarlem";
                        break;

                    case 1:
                        url = "https://streams.greenhost.nl:8006/barok";
                        host = "barok";
                        break;

                    case 2:
                        url = "https://icecast.omroep.nl/radio5-bb-mp3";
                        host = "nporadio";
                        break;

                    case 3:
                        url = "http://145-53-208-11.fixed.kpn.net:8300/audiostream";
                        host = "lansingerland";
                }
                AudioHelper.setStream(new URL(url).openStream());
                if (timer.passed(500)) {
                    AudioHelper.start();
                    jProgressBar1.setValue(100);
                    timer.reset();
                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("failed.");
                jProgressBar1.setValue(0);

            }
        } else {
            if (AudioHelper.isRunning() || evt.getStateChange() == ItemEvent.DESELECTED) {
                System.out.println("Disrupted connection from: " + url);
                AudioHelper.stop();
                jProgressBar1.setValue(0);
            }
        }
    }

    private void recordCheckBoxActionPerformed(ItemEvent evt) {
        DateFormat DATE_FORMAT = null;
        if (evt.getStateChange() == ItemEvent.SELECTED && AudioHelper.isRunning()) {

            try {
                if (!Files.exists(Paths.get("radioNL/"))) {
                    Files.createDirectories(Paths.get("radioNL/"));
                }
                if (!Files.exists(Paths.get("radioNL/haarlem/"))) {
                    Files.createDirectories(Paths.get("radioNL/haarlem/"));
                }
                if (!Files.exists(Paths.get("radioNL/barok/"))) {
                    Files.createDirectories(Paths.get("radioNL/barok/"));
                }
                if (!Files.exists(Paths.get("radioNL/nporadio/"))) {
                    Files.createDirectories(Paths.get("radioNL/nporadio/"));
                }
                if (!Files.exists(Paths.get("radioNL/lansingerland/"))) {
                    Files.createDirectories(Paths.get("radioNL/lansingerland/"));
                }

            } catch (Exception e) {
                System.out.println("Creating file error: " + e);
            }

            try {
                Thread thread = new Thread(() -> {
                    try {
                        URL download = new URL(url);
                        URLConnection connection = download.openConnection();
                        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1) AppleWebKit/534.30 (KHTML, like Gecko) Chrome/12.0.742.122 Safari/534.30 ChromePlus/1.6.3.1");

                        name = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + ".mp3";

                        inputstream = connection.getInputStream();
                        outputstream = new FileOutputStream("radioNL/" + host + "/" + name);

                        while ((length = inputstream.read(bytes)) != -1) {
                            outputstream.write(bytes, 0, length);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (inputstream != null) {
                        try {
                            inputstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                thread.start();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void jButton1ActionPerformed(ActionEvent evt) {
    }
}


