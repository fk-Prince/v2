package SwingComponents;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;

public class AnimateMessage {
    private static Timer timer;
    private static JPanel currentMessagePanel;

    public static void showMessage(String message, boolean flag, MigLayout migLayout, JPanel parent, double X, double Y, boolean animation) {
        if (currentMessagePanel != null) {
            if (timer != null && timer.isRunning()) {
                timer.stop();
            }
            parent.remove(currentMessagePanel);
            parent.revalidate();
            parent.repaint();
            currentMessagePanel = null;
        }

        MessageDialog panel = new MessageDialog(message, flag);
        parent.add(panel, "pos " + X + "% " + Y + "%, w 50%");
        currentMessagePanel = panel;

        parent.revalidate();
        parent.repaint();
        parent.setComponentZOrder(panel, 0);


        if (animation) {
            double[] Y_POSITION = {Y};
            timer = new Timer(20, e -> {
                Y_POSITION[0] += 0.2;

                migLayout.setComponentConstraints(panel, "pos " + X + "% " + Y_POSITION[0] + "%, w 50%");

                if (Y_POSITION[0] >= 2) {
                    timer.stop();
                    Y_POSITION[0] = Y;
                    currentMessagePanel = null;
                }
                parent.revalidate();
                parent.repaint();
            });

            timer.start();
        } else {
            timer = new Timer(2000, e -> {
                currentMessagePanel = null;
                parent.revalidate();
                parent.repaint();
            });
            timer.setDelay(0);
            timer.setRepeats(false);
            timer.start();
        }
    }


}
