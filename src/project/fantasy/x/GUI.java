package project.fantasy.x;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GUI extends JFrame {
    private Hero player;
    private Boss enemy;
    private JLabel playerHealthLabel, enemyHealthLabel, skillUsesLabel;
    private JButton attackButton, defendButton, skillButton;
    private JLabel playerImageLabel, enemyImageLabel, backgroundLabel;

    public GUI() {
        player = new Hero("Hero");
        enemy = new Boss("Dragon");

        // Load images
        ImageIcon imgHero = new ImageIcon(getClass().getResource("Hero.png"));
        ImageIcon imgBoss = new ImageIcon(getClass().getResource("Boss.png"));
        
        Image imgHeroScaled = imgHero.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH); // Adjust dimensions as needed
        Image imgBossScaled = imgBoss.getImage().getScaledInstance(450, 450, Image.SCALE_SMOOTH);
        playerImageLabel = new JLabel(new ImageIcon(imgHeroScaled));
        enemyImageLabel = new JLabel(new ImageIcon(imgBossScaled));

        
        ImageIcon backgroundImg = new ImageIcon(getClass().getResource("Background.jpg"));

        // Resize background image to fit only the game screen area, not the full frame
        Image backgroundImage = backgroundImg.getImage().getScaledInstance(800, 600, Image.SCALE_SMOOTH);
        backgroundLabel = new JLabel(new ImageIcon(backgroundImage));
        backgroundLabel.setLayout(new GridBagLayout()); // Use GridBagLayout for character positioning 

        // Set up the frame
        setTitle("Simple RPG Game");
        setSize(1000, 600); // Set a fixed size for the game window
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLayout(new BorderLayout());

        // Center area (game screen) with background
        JPanel gamePanel = new JPanel(new BorderLayout());
        gamePanel.add(backgroundLabel, BorderLayout.CENTER);
        add(gamePanel, BorderLayout.CENTER);

        // GridBagConstraints for placing player and enemy in the background
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Player panel on the left
        playerHealthLabel = createHealthLabel("Hero HP: " + player.health);
        playerImageLabel = new JLabel(new ImageIcon(imgHero.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        backgroundLabel.add(playerHealthLabel, gbc);

        gbc.gridy = 1;
        backgroundLabel.add(playerImageLabel, gbc);

        // Enemy panel on the right
        enemyHealthLabel = createHealthLabel("Boss HP: " + enemy.health);
        enemyImageLabel = new JLabel(new ImageIcon(imgBoss.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH)));

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTHEAST;
        backgroundLabel.add(enemyHealthLabel, gbc);

        gbc.gridy = 1;
        backgroundLabel.add(enemyImageLabel, gbc);

        // Right-side panel for buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        attackButton = new JButton("Attack");
        defendButton = new JButton("Defend");
        skillButton = new JButton("Use Skill");
        skillUsesLabel = new JLabel("Skill Uses Left: " + player.skillUses);
        skillUsesLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(attackButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Spacing
        buttonPanel.add(defendButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Spacing
        buttonPanel.add(skillButton);
        buttonPanel.add(Box.createVerticalStrut(10)); // Spacing
        buttonPanel.add(skillUsesLabel);

        add(buttonPanel, BorderLayout.EAST); // Add button panel to the right side of the frame

        // Button listeners
        attackButton.addActionListener(e -> playerAttack());
        defendButton.addActionListener(e -> playerDefend());
        skillButton.addActionListener(e -> playerUseSkill());

        setVisible(true);
        updateStatus();
    }

    private JLabel createHealthLabel(String text) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(Color.RED);
        label.setForeground(Color.WHITE);
        label.setPreferredSize(new Dimension(100, 25));
        label.setFont(new Font("Arial", Font.BOLD, 12));
        return label;
    }

    private void playerAttack() {
        int damage = player.attack();
        enemy.takeDamage(damage);
        JOptionPane.showMessageDialog(this, "Hero attacked Boss for " + damage + " damage!");
        if (checkGameEnd()) return;
        enemyTurn();
    }

    private void playerDefend() {
        player.defend();
        JOptionPane.showMessageDialog(this, "Hero is defending and will take less damage next turn!");
        enemyTurn();
    }

    private void playerUseSkill() {
        if (player.skillUses > 0) {
            int damage = player.useSkill();
            enemy.takeDamage(damage);
            JOptionPane.showMessageDialog(this, "Hero used a skill and attacked Boss for " + damage + " damage!");
            updateStatus();
        } else {
            JOptionPane.showMessageDialog(this, "No skill uses left!");
        }
        if (checkGameEnd()) return;
        enemyTurn();
    }

    private void enemyTurn() {
        if (!enemy.isAlive()) return;

        int damage = enemy.attack();
        player.takeDamage(damage);
        JOptionPane.showMessageDialog(this, "Boss attacked Hero for " + damage + " damage!");
        updateStatus();
        checkGameEnd();
    }

    private void updateStatus() {
        playerHealthLabel.setText("Hero HP: " + player.health);
        enemyHealthLabel.setText("Boss HP: " + enemy.health);
        skillUsesLabel.setText("Skill Uses Left: " + player.skillUses);
    }

    private boolean checkGameEnd() {
        if (!player.isAlive()) {
            JOptionPane.showMessageDialog(this, "Hero was defeated by the Boss. Game Over!");
            disableButtons();
            return true;
        }
        if (!enemy.isAlive()) {
            JOptionPane.showMessageDialog(this, "Hero defeated the Boss! Victory!");
            disableButtons();
            return true;
        }
        return false;
    }

    private void disableButtons() {
        attackButton.setEnabled(false);
        defendButton.setEnabled(false);
        skillButton.setEnabled(false);
    }

    public static void main(String[] args) {
        new GUI();
    }
}
