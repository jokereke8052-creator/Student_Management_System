
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName()
                );
            } catch (Exception e) {
                System.out.println(
                    "Could not apply system appearance."
                );
            }

            CourseDashboard dashboard = new CourseDashboard();
            dashboard.setVisible(true);
        });
    }
}
