import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Affichage implements ActionListener {
    //Constantes
    private final String MSG_ERR = "Vous devez entrer un nombre positif.";

    //Attributs d'instance
    private JPanel panelHaut = new JPanel(new FlowLayout());
    private JPanel panelMilieu = new JPanel(new FlowLayout());
    private JFrame fenetre = new JFrame();
    private JLabel jLBpm = new JLabel("BPM");
    private JTextField jTFBpm = new JTextField(5);
    private JLabel jLSilence = new JLabel("Nombre de BPM silencieux");
    private JTextField jTFSilence = new JTextField(5);//8 colonnes
    private JCheckBox jCBRandom = new JCheckBox("Au hasard");
    private JButton jBDemarrer = new JButton("Démarrer");


    public Affichage () {
        init();
    }

    public void init() {
        fenetre.setLayout(new BorderLayout());
        fenetre.setBounds(400, 400, 900, 150);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setTitle("Metronome");
        jCBRandom.setSelected(false);

        jLBpm.setFont(new Font("Arial", Font.BOLD, 20));
        panelHaut.add(jLBpm);
        panelHaut.add(jTFBpm);


        jLSilence.setFont(new Font("Arial", Font.BOLD, 20));
        panelMilieu.add(jLSilence);
        panelMilieu.add(jTFSilence);
        jCBRandom.setFont(new Font("Arial", Font.BOLD, 20));
        panelMilieu.add(Box.createHorizontalStrut(20));
        panelMilieu.add(jCBRandom);

        jBDemarrer.setFont(new Font("Arial", Font.BOLD, 20));

        fenetre.add(panelHaut, BorderLayout.NORTH);
        fenetre.add(panelMilieu, BorderLayout.CENTER);
        fenetre.add(jBDemarrer, BorderLayout.SOUTH);
        fenetre.setVisible(true);
//ajout des ecouteurs
        jCBRandom.addActionListener(this);
        jBDemarrer.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {

    }
}
