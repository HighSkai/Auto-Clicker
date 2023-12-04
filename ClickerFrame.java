import javax.swing.*;
import javax.swing.plaf.basic.BasicSliderUI;
import java.awt.*;
import java.awt.event.*;

public class ClickerFrame extends JFrame {
    private boolean clicking = false;
    private boolean leftClicking = true;
    private JButton switchButton;
    private JSlider slider;
    private Robot clicker;
    private Timer clickTimer;
    private JButton clickButton;
    private JLabel cpsLabel;
    private JLabel keyBindLabel;
    private long clickCount = 0;
    private long startTime = 0;

    public ClickerFrame() throws AWTException {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        clicker = new Robot();

        setTitle("Sky Clicker"); // Updated branding
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);

        // Custom font for branding
        Font brandingFont = new Font("Arial", Font.ITALIC, 24);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(40, 40, 40));

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        centerPanel.setBackground(new Color(40, 40, 40));

        // Branding label
        JLabel brandingLabel = new JLabel("Sky Clicker");
        brandingLabel.setFont(brandingFont);
        brandingLabel.setForeground(Color.WHITE);
        centerPanel.add(brandingLabel);

        keyBindLabel = new JLabel("Keybind: C");
        keyBindLabel.setForeground(Color.WHITE);
        centerPanel.add(keyBindLabel);

        clickButton = new JButton("Click");
        clickButton.setBackground(new Color(128, 0, 128));
        clickButton.setForeground(Color.WHITE);
        clickButton.setFocusPainted(false);
        clickButton.setBorderPainted(false);
        clickButton.setPreferredSize(new Dimension(100, 40));
        clickButton.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        centerPanel.add(clickButton);

        cpsLabel = new JLabel("CPS: 0");
        cpsLabel.setForeground(Color.WHITE);
        cpsLabel.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        centerPanel.add(cpsLabel);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        slider = new JSlider(JSlider.HORIZONTAL, 0, 50, 10);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(false);
        slider.setPaintTrack(false);
        slider.setPaintLabels(false);
        slider.setBackground(new Color(40, 40, 40));
        slider.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        mainPanel.add(slider, BorderLayout.SOUTH);

        switchButton = new JButton("Left Click");
        switchButton.setBackground(new Color(128, 0, 128));
        switchButton.setForeground(Color.WHITE);
        switchButton.setFocusPainted(false);
        switchButton.setBorderPainted(false);
        switchButton.setPreferredSize(new Dimension(100, 40));
        switchButton.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100), 2));
        centerPanel.add(switchButton);

        // Switch button action
        switchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switchClickType();
            }
        });

        NimbusDarkSliderUI sliderUI = new NimbusDarkSliderUI(slider);
        slider.setUI(sliderUI);

        add(mainPanel);

        clickButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleClicking();
            }
        });

        setupKeyBindings();

        setFocusable(true);
        requestFocus();
        setVisible(true);
    }

    private void setupKeyBindings() {
        int condition = JComponent.WHEN_IN_FOCUSED_WINDOW;
        InputMap inputMap = getRootPane().getInputMap(condition);
        ActionMap actionMap = getRootPane().getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "C_pressed");
        actionMap.put("C_pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleClicking();
            }
        });
    }

    private void toggleClicking() {
        if (clicking) {
            stopClicking();
        } else {
            startClicking();
        }
    }

    private void startClicking() {
        clicking = true;
        int delay = slider.getValue();
        clickCount = 0;
        startTime = System.currentTimeMillis();
        clickTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                click(clicker);
                clickCount++;
                updateCPSLabel();
            }
        });
        clickTimer.start();
        clickButton.setText("Stop");
    }

    private void stopClicking() {
        clicking = false;
        if (clickTimer != null) {
            clickTimer.stop();
        }
        clickButton.setText("Click");
        updateCPSLabel();
    }

    private void switchClickType() {
        leftClicking = !leftClicking;
        if (leftClicking) {
            switchButton.setText("Left Click");
        } else {
            switchButton.setText("Right Click");
        }
    }

    private void updateCPSLabel() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime;
        int cps = (int) (1000.0 * clickCount / elapsedTime);
        cpsLabel.setText("CPS: " + cps);
    }

    private void click(Robot clicker) {
        clicker.mousePress(InputEvent.BUTTON1_DOWN_MASK);
        clicker.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
    }

    class NimbusDarkSliderUI extends BasicSliderUI {
        public NimbusDarkSliderUI(JSlider slider) {
            super(slider);
        }

        @Override
        public Dimension getThumbSize() {
            return new Dimension(12, 20);
        }

        @Override
        public void paintThumb(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(Color.GRAY);
            g2d.fillRoundRect(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height, 10, 10);
        }
    }
}
