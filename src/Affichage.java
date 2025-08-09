import javax.swing.*;
import javax.swing.plaf.metal.MetalCheckBoxIcon;
import java.awt.*;
import java.awt.event.*;

public class Affichage implements ActionListener {
    //Constantes
    private final String MSG_ERR = "Vous devez entrer un nombre positif.";

    //Attributs d'instance
    private JFrame fenetre = new JFrame();
    private JPanel panelHaut = new JPanel(new FlowLayout());
    private JPanel panelMilieu = new JPanel(new FlowLayout());

    private JLabel jLBpm = new JLabel("BPM");
    private JTextField jTFBpm = new JTextField("0",3);

    private JLabel jLSilence = new JLabel("Nombre de BPM silencieux");
    private JTextField jTFSilence = new JTextField("0",3);

    private JCheckBox jCBRandom = new JCheckBox("Au hasard");
    private JButton jBDemarrer = new JButton("Démarrer");

    private int bpm = 0;
    private int silence = 0;
    private boolean random = false;

    public Affichage () {
        init();
    }

    public void init() {
        fenetre.setLayout(new BorderLayout());
        fenetre.setBounds(400, 400, 900, 225);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setTitle("Metronome");
        jCBRandom.setSelected(false);

        jLBpm.setFont(new Font("Arial", Font.BOLD, 32));
        jTFBpm.setFont(new Font("Arial", Font.BOLD, 32));
        panelHaut.add(jLBpm);
        panelHaut.add(jTFBpm);


        jLSilence.setFont(new Font("Arial", Font.BOLD, 32));
        jTFSilence.setFont(new Font("Arial", Font.BOLD, 32));
        panelMilieu.add(jLSilence);
        panelMilieu.add(jTFSilence);
        jCBRandom.setFont(new Font("Arial", Font.BOLD, 32));
        jCBRandom.setIcon(new MetalCheckBoxIcon() {
                              @Override
                              protected int getControlSize() {
                                  return 32; // Set desired size
                              }
                          });
        panelMilieu.add(Box.createHorizontalStrut(20));
        panelMilieu.add(jCBRandom);

        jBDemarrer.setFont(new Font("Arial", Font.BOLD, 32));

        fenetre.add(panelHaut, BorderLayout.NORTH);
        fenetre.add(panelMilieu, BorderLayout.CENTER);
        fenetre.add(jBDemarrer, BorderLayout.SOUTH);
        fenetre.setVisible(true);
//ajout des ecouteurs
        jCBRandom.addActionListener(this);
        jBDemarrer.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == jCBRandom) {
            random = !random;
            jTFSilence.setEditable(!random);
        } else {    //bouton demarrer
            demarrer();
        }
    }

    private void demarrer() {

    }
}
