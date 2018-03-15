/*
 * Feb 27, 2018
 * COMP 585 Project 2
 * SwingWorker
 * Yixin Chen
 * 
 * SwingWorker
 * Driver Class
 *
 */


import javax.swing.SwingUtilities;

class Driver {
    // main
    public static void main(String[] args) {
        // build and show app using the event-dispatching thread
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GUIApp();
            }
        });
    } // end main
}
