
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class SkillSwap extends JFrame {

    private final ArrayList<Skill> skills = new ArrayList<>();
    private final File dataFile = new File("skills.txt");

    private final JTextField nameField = new JTextField();
    private final JTextField teachField = new JTextField();
    private final JTextField learnField = new JTextField();
    private final JTextField contactField = new JTextField();
    private final JTextField searchField = new JTextField();

    private final JPanel cardsPanel = new JPanel();
    private final JLabel countLabel = new JLabel();

    private final Color purple = new Color(91, 72, 220);
    private final Color background = new Color(246, 247, 252);
    private final Color textColor = new Color(45, 48, 70);

    public SkillSwap() {
        setTitle("SkillSwap - Student Skill Exchange");
        setSize(1050, 720);
        setMinimumSize(new Dimension(850, 620));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        boolean savedFileExists = dataFile.exists();
        loadSkills();

        if (!savedFileExists) {
            skills.add(new Skill("Bhavya", "Java", "Python",
                    "bhavya@example.com"));
            skills.add(new Skill("Rithika", "Python", "Java",
                    "rithika@example.com"));
            saveSkills();
        }

        JPanel main = new JPanel(new BorderLayout(0, 22));
        main.setBackground(background);
        main.setBorder(new EmptyBorder(22, 28, 24, 28));
        setContentPane(main);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(purple);
        header.setBorder(new EmptyBorder(22, 25, 22, 25));

        JLabel title = new JLabel("SkillSwap");
        title.setFont(new Font("Arial", Font.BOLD, 30));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel(
                "Share what you know. Discover what you don't.");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(new Color(235, 232, 255));

        JPanel heading = new JPanel();
        heading.setLayout(new BoxLayout(heading, BoxLayout.Y_AXIS));
        heading.setOpaque(false);
        heading.add(title);
        heading.add(Box.createVerticalStrut(7));
        heading.add(subtitle);

        header.add(heading, BorderLayout.CENTER);
        main.add(header, BorderLayout.NORTH);

        // Main content
        JPanel content = new JPanel(new GridLayout(1, 2, 20, 0));
        content.setOpaque(false);

        // Skill submission form
        JPanel formCard = createCard();
        formCard.setLayout(new BoxLayout(formCard, BoxLayout.Y_AXIS));

        formCard.add(createHeading("Share Your Skills"));
        formCard.add(Box.createVerticalStrut(8));
        formCard.add(createLabel(
                "Connect with students and exchange knowledge."));
        formCard.add(Box.createVerticalStrut(22));

        addField(formCard, "Your Name", nameField);
        addField(formCard, "Skill You Can Teach", teachField);
        addField(formCard, "Skill You Want to Learn", learnField);
        addField(formCard, "Contact Details", contactField);

        JButton addButton = createButton("+  Add My Skill");
        addButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addButton.addActionListener(e -> addSkill());

        formCard.add(Box.createVerticalStrut(12));
        formCard.add(addButton);
        formCard.add(Box.createVerticalGlue());

        // Skill listing panel
        JPanel listingCard = createCard();
        listingCard.setLayout(new BorderLayout(0, 15));

        JPanel listingHeader = new JPanel(new BorderLayout(0, 8));
        listingHeader.setOpaque(false);

        listingHeader.add(
                createHeading("Explore Skills"), BorderLayout.NORTH);

        countLabel.setFont(new Font("Arial", Font.BOLD, 12));
        countLabel.setForeground(purple);
        listingHeader.add(countLabel, BorderLayout.SOUTH);

        listingCard.add(listingHeader, BorderLayout.NORTH);

        // Search controls
        JPanel searchPanel = new JPanel(new BorderLayout(8, 8));
        searchPanel.setOpaque(false);

        styleField(searchField);
        searchField.setToolTipText("Search by name or skill");
        searchPanel.add(searchField, BorderLayout.CENTER);

        JButton searchButton = createButton("Search");
        searchButton.addActionListener(e ->
                refreshSkills(searchField.getText().trim()));

        JButton showAllButton = createLightButton("Show All");
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            refreshSkills("");
        });

        JPanel searchButtons = new JPanel(new GridLayout(1, 2, 8, 0));
        searchButtons.setOpaque(false);
        searchButtons.add(searchButton);
        searchButtons.add(showAllButton);

        searchPanel.add(searchButtons, BorderLayout.SOUTH);

        // Skill cards
        cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.Y_AXIS));
        cardsPanel.setBackground(Color.WHITE);

        JScrollPane scrollPane = new JScrollPane(cardsPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        JPanel listingContent = new JPanel(new BorderLayout(0, 14));
        listingContent.setOpaque(false);
        listingContent.add(searchPanel, BorderLayout.NORTH);
        listingContent.add(scrollPane, BorderLayout.CENTER);

        listingCard.add(listingContent, BorderLayout.CENTER);

        // Remove button
        JButton removeButton = createLightButton("Remove Selected Skill");
        removeButton.addActionListener(e -> removeSkill());

        listingCard.add(removeButton, BorderLayout.SOUTH);

        content.add(formCard);
        content.add(listingCard);

        main.add(content, BorderLayout.CENTER);

        refreshSkills("");
    }

    private JPanel createCard() {
        JPanel panel = new JPanel();
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(231, 232, 242)),
                new EmptyBorder(22, 20, 22, 20)));
        return panel;
    }

    private JLabel createHeading(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(textColor);
        return label;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        label.setForeground(new Color(115, 117, 137));
        return label;
    }

    private void addField(JPanel panel, String labelText,
                          JTextField field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(textColor);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        styleField(field);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createVerticalStrut(7));
        panel.add(field);
        panel.add(Box.createVerticalStrut(17));
    }

    private void styleField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(180, 40));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(218, 220, 233)),
                new EmptyBorder(8, 10, 8, 10)));
        field.setBackground(new Color(250, 250, 253));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setBackground(purple);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(11, 14, 11, 14));
        return button;
    }

    private JButton createLightButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setForeground(purple);
        button.setBackground(new Color(239, 237, 255));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(10, 10, 10, 10));
        return button;
    }

    // Add a new skill
    private void addSkill() {
        String name = nameField.getText().trim();
        String teach = teachField.getText().trim();
        String learn = learnField.getText().trim();
        String contact = contactField.getText().trim();

        if (name.isEmpty() || teach.isEmpty()
                || learn.isEmpty() || contact.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill in all fields!",
                    "Missing Information",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        skills.add(new Skill(name, teach, learn, contact));
        saveSkills();

        nameField.setText("");
        teachField.setText("");
        learnField.setText("");
        contactField.setText("");
        searchField.setText("");

        refreshSkills("");
    }

    // Search and display matching skills
    private void refreshSkills(String keyword) {
        cardsPanel.removeAll();
        int found = 0;

        for (Skill skill : skills) {
            String details = (skill.name + " " + skill.teach
                    + " " + skill.learn).toLowerCase();

            if (details.contains(keyword.toLowerCase())) {
                cardsPanel.add(createSkillCard(skill));
                cardsPanel.add(Box.createVerticalStrut(12));
                found++;
            }
        }

        countLabel.setText(found + " skill exchange(s)");

        if (found == 0) {
            JLabel empty = createLabel("No matching skills found.");
            empty.setBorder(new EmptyBorder(20, 5, 20, 5));
            cardsPanel.add(empty);
        }

        cardsPanel.revalidate();
        cardsPanel.repaint();
    }

    // Remove a selected skill using a selection dialog
    private void removeSkill() {
        if (skills.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "There are no skills to remove.");
            return;
        }

        String[] options = new String[skills.size()];

        for (int i = 0; i < skills.size(); i++) {
            Skill skill = skills.get(i);
            options[i] = (i + 1) + ". " + skill.name
                    + " - Teaches: " + skill.teach;
        }

        String choice = (String) JOptionPane.showInputDialog(
                this,
                "Choose a skill to remove:",
                "Remove Skill",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (choice == null) {
            return;
        }

        int index = -1;

        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(choice)) {
                index = i;
                break;
            }
        }

        if (index >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to remove this skill?",
                    "Confirm Removal",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                skills.remove(index);
                saveSkills();
                refreshSkills(searchField.getText().trim());
            }
        }
    }

    // Create an individual skill card
    private JPanel createSkillCard(Skill skill) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(new Color(250, 249, 255));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                        new Color(229, 225, 255)),
                new EmptyBorder(15, 15, 15, 15)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel name = new JLabel(skill.name);
        name.setFont(new Font("Arial", Font.BOLD, 17));
        name.setForeground(purple);

        JLabel teaches = new JLabel("CAN TEACH     " + skill.teach);
        teaches.setFont(new Font("Arial", Font.PLAIN, 13));
        teaches.setForeground(textColor);

        JLabel learns = new JLabel("WANTS TO LEARN     " + skill.learn);
        learns.setFont(new Font("Arial", Font.PLAIN, 13));
        learns.setForeground(textColor);

        JLabel contact = new JLabel("Contact: " + skill.contact);
        contact.setFont(new Font("Arial", Font.PLAIN, 12));
        contact.setForeground(new Color(110, 112, 132));

        card.add(name);
        card.add(Box.createVerticalStrut(12));
        card.add(teaches);
        card.add(Box.createVerticalStrut(8));
        card.add(learns);
        card.add(Box.createVerticalStrut(12));
        card.add(contact);

        return card;
    }

    // Save all skills to a local text file
    private void saveSkills() {
        try (PrintWriter writer = new PrintWriter(
                new FileWriter(dataFile, StandardCharsets.UTF_8))) {

            for (Skill skill : skills) {
                writer.println(skill.name.replace("|", "/") + "|"
                        + skill.teach.replace("|", "/") + "|"
                        + skill.learn.replace("|", "/") + "|"
                        + skill.contact.replace("|", "/"));
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not save skill details.");
        }
    }

    // Load saved skills when the application starts
    private void loadSkills() {
        if (!dataFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(
                new FileReader(dataFile, StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|", -1);

                if (data.length == 4) {
                    skills.add(new Skill(
                            data[0], data[1], data[2], data[3]));
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this,
                    "Could not load saved skill details.");
        }
    }

    // Skill data class
    private static class Skill {
        String name;
        String teach;
        String learn;
        String contact;

        Skill(String name, String teach,
              String learn, String contact) {
            this.name = name;
            this.teach = teach;
            this.learn = learn;
            this.contact = contact;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SkillSwap().setVisible(true);
        });
    }
}
