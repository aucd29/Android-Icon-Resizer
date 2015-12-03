import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.imgscalr.Scalr;
import org.json.JSONObject;

public class IconResizer {
    private JFrame frmAndroidIconResizer;
    private JTextField filePath;
    private JTextField folderPath;
    private JButton folderButton;
    private Config cfg;
    private PathCfg mPathCfg;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {

                    IconResizer window = new IconResizer();
                    window.frmAndroidIconResizer.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public IconResizer() {
        initialize();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {
        cfg = new Config();
        mPathCfg = new PathCfg();

        frmAndroidIconResizer = new JFrame();
        frmAndroidIconResizer.setTitle("Android Icon Resizer");
        frmAndroidIconResizer.setBounds(100, 100, 329, 300);
        frmAndroidIconResizer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frmAndroidIconResizer.getContentPane().setLayout(null);

        filePath = new JTextField();
        filePath.setEditable(false);
        filePath.setBounds(25, 51, 187, 28);
        frmAndroidIconResizer.getContentPane().add(filePath);
        filePath.setColumns(10);
        filePath.setText(mPathCfg.mSrcPath);

        JButton fileButton = new JButton("찾기");
        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("PNG Icon", "png"));
                fileChooser.setDialogTitle("PNG 형태의 아이콘을 선택하세요");
                int returnVal = fileChooser.showOpenDialog(fileChooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    filePath.setText(fileChooser.getSelectedFile().toString());
                }
            }
        });
        fileButton.setBounds(227, 52, 74, 29);
        frmAndroidIconResizer.getContentPane().add(fileButton);

        folderPath = new JTextField();
        folderPath.setEditable(false);
        folderPath.setColumns(10);
        folderPath.setBounds(25, 117, 187, 28);
        frmAndroidIconResizer.getContentPane().add(folderPath);
        folderPath.setText(mPathCfg.mTargetPath);

        folderButton = new JButton("찾기");
        folderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser folderChooser = new JFileChooser();
                folderChooser
                .setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                folderChooser
                .setDialogTitle("리소스 폴더를 선택하세요");
                int returnVal = folderChooser.showOpenDialog(folderChooser);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    folderPath.setText(folderChooser.getSelectedFile()
                            .toString());
                }
            }
        });

        folderButton.setBounds(227, 118, 74, 29);
        frmAndroidIconResizer.getContentPane().add(folderButton);

        JLabel lblxIconPath = new JLabel("512x512 아이콘 경로");
        lblxIconPath.setBounds(25, 28, 187, 16);
        frmAndroidIconResizer.getContentPane().add(lblxIconPath);

        final JLabel lblMessage = new JLabel("아이콘 생성");
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessage.setBounds(35, 164, 266, 16);
        frmAndroidIconResizer.getContentPane().add(lblMessage);

        JLabel lblOutputFolder = new JLabel("생성될 아이콘 경로");
        lblOutputFolder.setBounds(25, 97, 187, 16);
        frmAndroidIconResizer.getContentPane().add(lblOutputFolder);

        JButton btnGenerate = new JButton("생성");
        btnGenerate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!filePath.getText().isEmpty() && !folderPath.getText().isEmpty()) {
                    mPathCfg.setPath(filePath.getText(), folderPath.getText());

                    File imageFile = new File(filePath.getText());
                    String dirPath = folderPath.getText();
                    if (imageFile.exists()) {
                        try {
                            lblMessage.setText("이미지를 변환 중 입니다.");

                            // Use createResizedCopy method to create resized
                            // BufferedImage
                            BufferedImage myImage = ImageIO.read(imageFile);

                            for (String key : cfg.iconType.keySet()) {
                                int size = cfg.iconType.get(key);

                                BufferedImage resizedIcon = createResizedCopy(myImage, size, size);

                                File fp = new File(dirPath + "/mipmap-" + key + "/" + cfg.iconName);
                                fp.mkdirs();

                                lblMessage.setText("이미지 생성중...");
                                ImageIO.write(resizedIcon, "PNG", fp);
                            }

                            lblMessage.setText("아이콘을 성공적으로 생성하였습니다.");
                        } catch (IOException e1) {
                            lblMessage
                            .setText("아이콘 생성을 실패 하였습니다.");
                            e1.printStackTrace();
                        }
                    } else {
                        lblMessage
                        .setText("선택된 아이콘이 없습니다.");
                    }
                } else {
                    lblMessage
                    .setText("리소스 경로를 설정하세요");
                }
            }
        });
        btnGenerate.setBounds(88, 192, 152, 50);
        frmAndroidIconResizer.getContentPane().add(btnGenerate);

        //        JLabel aboutLabel = new JLabel(
        //                "\u00A92013 Kaushal Subedi, All Rights Reserved");
        //        aboutLabel.setBounds(25, 280, 276, 16);
        //        frmAndroidIconResizer.getContentPane().add(aboutLabel);

        //		JButton btnWebsite = new JButton("Website");
        //		btnWebsite.addActionListener(new ActionListener() {
        //			public void actionPerformed(ActionEvent arg0) {
        //				try {
        //					Desktop.getDesktop().browse(new URI(WEB_URL));
        //				} catch (Exception e) {
        //					e.printStackTrace();
        //				}
        //			}
        //		});
        //		btnWebsite.setBounds(25, 299, 117, 29);
        //		frmAndroidIconResizer.getContentPane().add(btnWebsite);
        //
        //		JButton btnDonate = new JButton("Donate");
        //		btnDonate.addActionListener(new ActionListener() {
        //			public void actionPerformed(ActionEvent e) {
        //				try {
        //					Desktop.getDesktop().browse(new URI(DONATE_LINK));
        //				} catch (Exception x) {
        //					x.printStackTrace();
        //				}
        //			}
        //		});
        //		btnDonate.setBounds(184, 299, 117, 29);
        //		frmAndroidIconResizer.getContentPane().add(btnDonate);
    }

    BufferedImage createResizedCopy(BufferedImage originalImage, int scaledWidth, int scaledHeight) {
        return Scalr.resize(originalImage, Scalr.Method.ULTRA_QUALITY, scaledWidth, scaledHeight, Scalr.OP_ANTIALIAS);
    }

    public class PathCfg extends CfgBase {
        public String mSrcPath;
        public String mTargetPath;

        public PathCfg() {
            super("path.json");

            if (cfg == null) {
                return ;
            }

            try {
                JSONObject json = new JSONObject(cfg);
                mSrcPath    = json.getString("srcPath");
                mTargetPath = json.getString("targetPath");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean setPath(String src, String target) {
            try {
                if (mSrcPath.equals(src) && mTargetPath.equals(target)) {
                    return true;
                }

                mSrcPath    = src;
                mTargetPath = target;

                String data = genData(src, target);

                File fp = new File(".", mFileName);
                if (fp.exists()) {
                    FileOutputStream os = new FileOutputStream(fp);
                    os.write(data.getBytes());
                    os.close();
                }

                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            return false;
        }

        private String genData(String src, String target) {
            try {
                JSONObject json = new JSONObject();
                json.put("srcPath", src);
                json.put("targetPath", target);

                return json.toString();
            } catch (Exception e) {
                e.printStackTrace();

                return "{}";
            }
        }

        @Override
        public String defaultValue() {
            return genData("", "");
        }
    }
}
