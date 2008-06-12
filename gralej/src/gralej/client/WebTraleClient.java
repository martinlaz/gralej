/*
 * WebTraleClient.java
 *
 * Created on 4. Dezember 2007, 17:55
 */
package gralej.client;

import gralej.prefs.GralePreferences;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * 
 * @author Martin
 */
public class WebTraleClient extends JPanel {

    private URL webtraleContextURL;
    BlockingQueue<Short> byteBlockingQueue = new LinkedBlockingQueue<Short>();
    
    public class InvalidServerException extends RuntimeException {
        public InvalidServerException(URL url) {
            super(url.toString() + " is not a WebTrale server");
        }
    }

    /** Creates new form WebTraleClient */
    public WebTraleClient(URL url) {
        webtraleContextURL = url;
        String appInfo = null;
        try {
            InputStream is = getStream("__appinfo.txt");
            if (is != null)
                appInfo = readAll(is);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        if (appInfo == null || !appInfo.startsWith("application.name=WebTrale"))
            throw new InvalidServerException(url);
        initComponents();
        loadWords();
    }

    private void loadWords() {
        try {
            InputStream is = getStream("words-raw");
            if (is == null)
                return;
            String s = readAll(is);
            String[] ss = s.split("\r?\n");
            DefaultListModel model = new DefaultListModel();
            for (String word : ss) {
                model.addElement(word);
            }
            lstLexicon.setModel(model);
        } catch (IOException ex) {
            showError(ex);
        }
    }

    public InputStream getInputStream() {
        return new InputStream() {
            public int read() throws IOException {
                try {
                    return byteBlockingQueue.take();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    return -1;
                }
            }

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                int reqSize = len - off;
                if (reqSize <= 0)
                    return 0;

                int n = byteBlockingQueue.size();
                if (n > reqSize)
                    n = reqSize;
                else if (n == 0)
                    n = 1;

                for (int i = 0; i < n - 1; ++i)
                    b[off++] = (byte) read();

                int lastByte = read();
                if (lastByte == -1) {
                    if (--n == 0)
                        return -1;
                } else
                    b[off] = (byte) lastByte;

                return n;
            }
        };
    }

    public static WebTraleClient inFrame(URL url) {
        if (!url.toString().endsWith("/")) {
            // url should always end in '/'
            try {
                url = new URL(url.toString() + "/");
            } catch (MalformedURLException e) {
                // will not happen
            }
        }

        Authenticator.install();
        final WebTraleClient wtClient = new WebTraleClient(url);

        JFrame f = new JFrame();
        f.setTitle(url.getHost() + " - WebTrale");
        f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent we) {
                wtClient.byteBlockingQueue.offer((short) -1);
            }
        });
        f.setLocationByPlatform(true);
        f.setContentPane(wtClient);
        f.pack();
        f.setVisible(true);
        return wtClient;
    }

    public static void main(String[] args) throws Exception {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String surl = args[0].trim();
        if (!surl.endsWith("/"))
            surl += "/";
        final URL url = new URL(surl);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                inFrame(url);
            }
        });
    }

    private InputStream getStream(String spec) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(webtraleContextURL,
                spec).openConnection();
        con.setRequestProperty("User-Agent", "Gralej/" + GralePreferences.getInstance().get("_gralej.version"));
        try {
            return con.getInputStream();
        } catch (IOException ex) {
            InputStream errs = con.getErrorStream();
            if (errs == null)
                throw ex;
            String msg = readAll(errs);
            if (!msg.startsWith("WebTrale:"))
                throw ex;
            JOptionPane.showMessageDialog(this, msg);
            errs.close();
            return null;
        }
    }

    void copyStream(InputStream is) throws IOException {
        int n;
        byte[] b = new byte[0x1000];
        while ((n = is.read(b)) != -1) {
            for (int i = 0; i < n; ++i)
                byteBlockingQueue.offer((short) b[i]);
        }
        is.close();
    }

    static String readAll(InputStream is) throws IOException {
        return readAll(is, "ISO-8859-1");
    }

    static String readAll(InputStream is, String charsetName)
            throws IOException {
        char[] b = new char[1024];
        int n;
        StringBuilder sb = new StringBuilder();
        Reader in = new BufferedReader(new InputStreamReader(is, charsetName));
        while ((n = in.read(b)) != -1) {
            sb.append(b, 0, n);
        }
        is.close();
        return sb.toString();
    }

    private void rec() {
        String s = txtSentence.getText();
        txtSentence.selectAll();
        request("rec-raw?q=", s);
    }

    private void request(final String prefix, final String suffix) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    InputStream is = getStream(prefix
                            + URLEncoder.encode(suffix, "UTF-8"));
                    if (is == null)
                        return;
                    // String content = readAll(is);
                    // System.out.println(content);
                    copyStream(is);
                } catch (IOException ex) {
                    showError(ex);
                }
            }
        }).start();
    }

    private void showError(Exception e) {
        JOptionPane.showMessageDialog(this, e.getMessage(), e.getClass()
                .getName(), JOptionPane.ERROR_MESSAGE);
    }

    private void lex() {
        Object o = lstLexicon.getSelectedValue();
        if (o == null) {
            if (lstLexicon.getModel().getSize() == 0)
                loadWords();
            return;
        }
        String s = o.toString();
        request("lex-raw?q=", s);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtSentence = new javax.swing.JTextField();
        btnParse = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstLexicon = new javax.swing.JList();
        btnShow = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Sentence",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51,
                        255)));

        txtSentence.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSentenceActionPerformed(evt);
            }
        });

        btnParse.setText("Parse");
        btnParse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(
                jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout
                .setHorizontalGroup(jPanel1Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel1Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel1Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(
                                                                txtSentence,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                198,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                btnParse,
                                                                javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addContainerGap()));
        jPanel1Layout
                .setVerticalGroup(jPanel1Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                jPanel1Layout
                                        .createSequentialGroup()
                                        .addComponent(
                                                txtSentence,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(btnParse)
                                        .addContainerGap(6, Short.MAX_VALUE)));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null,
                "Lexicon",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 51,
                        255)));

        lstLexicon
                .setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        lstLexicon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstLexiconMouseClicked(evt);
            }
        });
        lstLexicon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lstLexiconKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(lstLexicon);

        btnShow.setText("Show");
        btnShow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnShowActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(
                jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout
                .setHorizontalGroup(jPanel2Layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                jPanel2Layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                jPanel2Layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(
                                                                jScrollPane1,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                198,
                                                                Short.MAX_VALUE)
                                                        .addComponent(btnShow))
                                        .addContainerGap()));
        jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(
                javax.swing.GroupLayout.Alignment.LEADING).addGroup(
                javax.swing.GroupLayout.Alignment.TRAILING,
                jPanel2Layout.createSequentialGroup().addComponent(
                        jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE,
                        151, Short.MAX_VALUE).addPreferredGap(
                        javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnShow).addContainerGap()));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout
                .setHorizontalGroup(layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                javax.swing.GroupLayout.Alignment.TRAILING,
                                layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addGroup(
                                                layout
                                                        .createParallelGroup(
                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(
                                                                jPanel2,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE)
                                                        .addComponent(
                                                                jPanel1,
                                                                javax.swing.GroupLayout.Alignment.LEADING,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                Short.MAX_VALUE))
                                        .addContainerGap()));
        layout
                .setVerticalGroup(layout
                        .createParallelGroup(
                                javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(
                                layout
                                        .createSequentialGroup()
                                        .addContainerGap()
                                        .addComponent(
                                                jPanel1,
                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                82,
                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(
                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(
                                                jPanel2,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                Short.MAX_VALUE)
                                        .addContainerGap()));
    }// </editor-fold>//GEN-END:initComponents

    private void btnParseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnParseActionPerformed
        rec();
    }// GEN-LAST:event_btnParseActionPerformed

    private void txtSentenceActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_txtSentenceActionPerformed
        rec();
    }// GEN-LAST:event_txtSentenceActionPerformed

    private void lstLexiconMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_lstLexiconMouseClicked
        if (evt.getClickCount() == 2)
            lex();
    }// GEN-LAST:event_lstLexiconMouseClicked

    private void lstLexiconKeyReleased(java.awt.event.KeyEvent evt) {// GEN-FIRST:event_lstLexiconKeyReleased
        if (evt.getKeyCode() == KeyEvent.VK_ENTER)
            lex();
    }// GEN-LAST:event_lstLexiconKeyReleased

    private void btnShowActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnShowActionPerformed
        lex();
    }// GEN-LAST:event_btnShowActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnParse;
    private javax.swing.JButton btnShow;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstLexicon;
    private javax.swing.JTextField txtSentence;
    // End of variables declaration//GEN-END:variables

}
