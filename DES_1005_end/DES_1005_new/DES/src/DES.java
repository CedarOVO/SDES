import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.lang.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DES {
    JFrame frame;

    public DES() {

        frame = new JFrame("DES-加密解密");
        frame.setSize(600, 450);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLayout(null); // 设置为null布局
        //frame.getContentPane().setBackground(new Color(207, 241, 225));
        //frame.setIconImage(new ImageIcon("../photo/bg.jpg").getImage());



        JLabel titleLabel = new JLabel("S-DES");
        titleLabel.setBounds(250,40,150,50);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24)); // 设置字体样式和大小
        frame.add(titleLabel);

        JLabel plaintextLabel = new JLabel("明文：");
        plaintextLabel.setBounds(60, 120, 100, 30);
        frame.add(plaintextLabel);

        JTextField plaintextField = new JTextField();
        plaintextField.setBounds(150, 120, 250, 30);
        frame.add(plaintextField);

        JCheckBox asciiCheck = new JCheckBox("输入ASCII字符");
        asciiCheck.setBounds(430, 120, 110, 30);
        asciiCheck.setBackground(new Color(239, 204, 203));
        frame.add(asciiCheck);

        JLabel keyLabel = new JLabel("密钥（10bit）：");
        keyLabel.setBounds(60, 190, 100, 30);
        frame.add(keyLabel);

        JTextField keyField = new JTextField();
        keyField.setBounds(150, 190, 250, 30);
        frame.add(keyField);

        JButton generateButton = new JButton("随机生成密钥");
        generateButton.setBounds(430, 190, 110, 30);
        generateButton.setBackground(new Color(239, 204, 203));
        frame.add(generateButton);
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String randomKey = generateRandomKey();
                keyField.setText(randomKey);
            }
        });

        JLabel ciphertextLabel = new JLabel("密文：");
        ciphertextLabel.setBounds(60, 260, 100, 30);
        frame.add(ciphertextLabel);

        JTextField ciphertextField = new JTextField();
        ciphertextField.setBounds(150, 260, 250, 30);
        frame.add(ciphertextField);

        JButton encryptButton = new JButton("加密");
        encryptButton.setBounds(60, 330, 110, 30);
        encryptButton.setBackground(new Color(239, 204, 203));
        frame.add(encryptButton);
        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String plaintext = plaintextField.getText();
                String key = keyField.getText();

                // 检查密文和密钥长度
                if (asciiCheck.isSelected()) {
//                    if (!isASCII(plaintext)) {
//                        JOptionPane.showMessageDialog(frame, "您输入的明文包含非ASCII字符，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
                    plaintext = stringToBinary(plaintext);
                }else if (!plaintext.matches("[01]+")) { // 检查是否为二进制数
                    JOptionPane.showMessageDialog(frame, "您输入的明文不是有效的二进制数，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (key.length() != 10) {
                    JOptionPane.showMessageDialog(frame, "您输入的密钥不是10位，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String ciphertext = encrypt(plaintext, key);
                if (asciiCheck.isSelected())
                    ciphertext = binaryToString(ciphertext);
                ciphertextField.setText(ciphertext);
            }
        });

        JButton decryptButton = new JButton("解密");
        decryptButton.setBounds(245, 330, 110, 30);
        decryptButton.setBackground(new Color(239, 204, 203));
        frame.add(decryptButton);
        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ciphertext = ciphertextField.getText();
                String key = keyField.getText();

                // 检查密文和密钥长度
                if (asciiCheck.isSelected()) {
//                    if (!isASCII(ciphertext)) {
//                        JOptionPane.showMessageDialog(frame, "您输入的密文包含非ASCII字符，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
//                        return;
//                    }
                    ciphertext = stringToBinary(ciphertext);
                }else if (!ciphertext.matches("[01]+")) { // 检查是否为二进制数
                    JOptionPane.showMessageDialog(frame, "您输入的密文不是有效的二进制数，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (key.length() != 10) {
                    JOptionPane.showMessageDialog(frame, "您输入的密钥不是10位，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String plaintext = decrypt(ciphertext, key);
                if (asciiCheck.isSelected())
                    plaintext = binaryToString(plaintext);
                plaintextField.setText(plaintext);
            }
        });

        JButton bruteForceButton = new JButton("暴力破解密钥");
        bruteForceButton.setBounds(430, 330, 110, 30);
        bruteForceButton.setBackground(new Color(239, 204, 203));
        frame.add(bruteForceButton);
        bruteForceButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DateTimeFormatter formatter_start = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTime_start = LocalDateTime.now().format(formatter_start);
                System.out.println("请求暴力破解的时间: " + currentTime_start);
                String plaintext = plaintextField.getText();
                String ciphertext = ciphertextField.getText();
                if (asciiCheck.isSelected()){
                    plaintext = stringToBinary(plaintext);
                    ciphertext = stringToBinary(ciphertext);
                }else if (!plaintext.matches("[01]+") ||!ciphertext.matches("[01]+")) { // 检查是否为二进制数
                    JOptionPane.showMessageDialog(frame, "您输入的明文或密文不是有效的二进制数，请重新输入", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String foundKey = bruteForceEncrypt(plaintext, ciphertext);
                if (!foundKey.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "找到的密钥是: " + foundKey, "成功", JOptionPane.INFORMATION_MESSAGE);
                    keyField.setText(foundKey);
                } else {
                    JOptionPane.showMessageDialog(frame, "未找到有效的密钥！", "失败", JOptionPane.WARNING_MESSAGE);
                    keyField.setText("");
                }
            }
        });

        JButton cleanButton = new JButton("清空");
        cleanButton.setBounds(430, 260, 110, 30);
        cleanButton.setBackground(new Color(239, 204, 203));
        frame.add(cleanButton);
        cleanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ciphertextField.setText("");
                keyField.setText("");
                plaintextField.setText("");
            }
        });

        //设置背景图
        System.out.println("当前工作目录: " + System.getProperty("user.dir"));
        //注意：背景图片路径要根据当前工作目录的情况进行修改！！！
        //ImageIcon background = new ImageIcon("../photo/bg.jpg");
        ImageIcon background = new ImageIcon("DES_1005_new/DES/photo/bg.jpg");

        Image image = background.getImage();
        Image smallImage = image.getScaledInstance(600, 450, Image.SCALE_FAST);
        ImageIcon backbrounds = new ImageIcon(smallImage);

        JLabel jlabel = new JLabel(backbrounds);
        jlabel.setBounds(0,0, frame.getWidth(),frame.getHeight() );
        frame.getContentPane().add(jlabel);
    }

    public void show() {
        frame.setVisible(true);
    }

    // 随机生成10位密钥
    private String generateRandomKey() {
        Random rand = new Random();
        StringBuilder key = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            key.append(rand.nextInt(2));
        }
        return key.toString();
    }

    // 置换表
    private static final int[] P10 = {3, 5, 2, 7, 4, 10, 1, 9, 8, 6};
    private static final int[] P8 = {6, 3, 7, 4, 8, 5, 10, 9};
    private static final int[] IP = {2, 6, 3, 1, 4, 8, 5, 7};
    private static final int[] IP_INV = {4, 1, 3, 5, 7, 2, 8, 6};
    private static final int[] EPBox = {4, 1, 2, 3, 2, 3, 4, 1};
    private static final int[][] SBOX_1 = {
            {1, 0, 3, 2},
            {3, 2, 1, 0},
            {0, 2, 1, 3},
            {3, 1, 0, 2}
    };
    private static final int[][] SBOX_2 = {
            {0, 1, 2, 3},
            {2, 3, 1, 0},
            {3, 0, 1, 2},
            {2, 1, 0, 3}
    };
    private static final int[] SPBox = {2, 4, 3, 1};

    private String stringToBinary(String s) {
        StringBuilder binary = new StringBuilder();
        for (char c : s.toCharArray()) {
            String bin = String.format("%08d", Integer.parseInt(Integer.toBinaryString(c)));
            binary.append(bin);
        }
        return binary.toString(); // 返回完整的二进制字符串
    }

    private String binaryToString(String binary) {
        StringBuilder result = new StringBuilder();

        // 每8位为一个字符
        for (int i = 0; i + 8 <= binary.length(); i += 8) {
            String byteString = binary.substring(i, i + 8);

            // 转换为字符
            char character = (char) Integer.parseInt(byteString, 2);
            result.append(character);
        }

        // 返回完整的结果
        return result.toString();
    }

    private boolean isASCII(String s) {
        for (char c : s.toCharArray()) {
            // 检查字符是否在 ASCII 范围内
            if (c < 0 || c > 127) {
                return false;
            }
        }
        return true;
    }


    // 左移操作
    private String leftShift(String input) {
        return input.substring(1) + input.charAt(0);
    }

    // 密钥生成
    private String[] generateKeys(String key) {
        String[] keys = new String[2];

        // P10置换
        String permutedKey = permute(key, P10);

        // 分成左右两部分
        String left = permutedKey.substring(0, 5);
        String right = permutedKey.substring(5);

        // 生成K1
        left = leftShift(left);
        right = leftShift(right);
        keys[0] = permute(left + right, P8);

        // 生成K2
        left = leftShift(left);
        right = leftShift(right);
        keys[1] = permute(left + right, P8);

        return keys;
    }

    // 初始置换
    private String initialPermutation(String input) {
        return permute(input, IP);
    }

    // 逆初始置换
    private String inverseInitialPermutation(String input) {
        return permute(input, IP_INV);
    }

    // 扩展置换
    private String expansion(String right) {
        return permute(right, EPBox);
    }

    // S盒转换
    private String sBoxSubstitution(String input) {
        String left = input.substring(0, 4);
        String right = input.substring(4, 8);

        int row1 = Integer.parseInt("" + left.charAt(0) + left.charAt(3), 2);
        int col1 = Integer.parseInt("" + left.charAt(1) + left.charAt(2), 2);
        int row2 = Integer.parseInt("" + right.charAt(0) + right.charAt(3), 2);
        int col2 = Integer.parseInt("" + right.charAt(1) + right.charAt(2), 2);

        int sBox1Value = SBOX_1[row1][col1];
        int sBox2Value = SBOX_2[row2][col2];
        return String.format("%02d%02d", Integer.parseInt(Integer.toBinaryString(sBox1Value)), Integer.parseInt(Integer.toBinaryString(sBox2Value)));
    }

    private String SPBoxPermutation(String input) {
        return permute(input, SPBox);
    }

    // 轮函数 F 的实现
    private String fFunction(String right, String subkey) {
        String expanded = expansion(right);
        String xored = xor(expanded, subkey);
        String substituted = sBoxSubstitution(xored);
        return SPBoxPermutation(substituted);
    }

    // 辅助方法：异或操作
    private String xor(String a, String b) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < a.length(); i++) {
            result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
        }
        return result.toString();
    }

    // 辅助方法：置换
    private String permute(String input, int[] table) {
        StringBuilder result = new StringBuilder();
        for (int index : table) {
            result.append(input.charAt(index - 1)); // 由于table是1-based索引
        }
        return result.toString();
    }

    private String encrypt(String plaintext, String key) {
        StringBuilder ciphertext = new StringBuilder();

        // 按照每8位划分块进行加密
        for (int i = 0; i < plaintext.length(); i += 8) {
            String block = plaintext.substring(i, Math.min(i + 8, plaintext.length()));

            // 补齐到8位
            while (block.length() < 8) {
                block += "0"; // 可以补充0或者其他字符
            }

            String[] keys = generateKeys(key);
            String permutedText = initialPermutation(block);
            String left = permutedText.substring(0, 4);
            String right = permutedText.substring(4);

            // 第一轮
            String temp_right_1 = fFunction(right, keys[0]);
            left = xor(left, temp_right_1);

            // 交换左右部分
            String temp = left;
            left = right;
            right = temp;

            // 第二轮
            String temp_right_2 = fFunction(right, keys[1]);
            left = xor(left, temp_right_2);

            // 合并并进行逆初始置换
            String combined = left + right;
            ciphertext.append(inverseInitialPermutation(combined));
        }

        return ciphertext.toString();
    }

    private String decrypt(String ciphertext, String key) {
        StringBuilder plaintext = new StringBuilder();

        // 每8位解密一个块
        for (int i = 0; i < ciphertext.length(); i += 8) {
            String block = ciphertext.substring(i, Math.min(i + 8, ciphertext.length()));

            String[] keys = generateKeys(key);
            String permutedText = initialPermutation(block);
            String left = permutedText.substring(0, 4);
            String right = permutedText.substring(4);

            // 第一轮
            String temp_right_1 = fFunction(right, keys[1]);
            left = xor(left, temp_right_1);

            // 交换左右部分
            String temp = left;
            left = right;
            right = temp;

            // 第二轮
            String temp_right_2 = fFunction(right, keys[0]);
            left = xor(left, temp_right_2);

            // 合并并进行逆初始置换
            String combined = left + right;
            plaintext.append(inverseInitialPermutation(combined));
        }

        return plaintext.toString();
    }

    private String bruteForceEncrypt(String plaintext, String targetCiphertext) {
        StringBuilder matchedKeys = new StringBuilder();

        int counter = 0;

        // 对所有可能的1024个密钥进行尝试
        for (int i = 0; i < 1024; i++) { // 2^10 = 1024
            String key = String.format("%10s", Integer.toBinaryString(i)).replace(' ', '0'); // 二进制补全至10位

            String ciphertext = encrypt(plaintext, key);

            if (ciphertext.equals(targetCiphertext)) {
                counter ++;
                DateTimeFormatter formatter_end = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String currentTime_end = LocalDateTime.now().format(formatter_end);
                System.out.println("暴力破解完成的时间（找到第" + counter + "个密钥）: " + currentTime_end);
                if (matchedKeys.length() == 0) {
                    matchedKeys.append(key); // 找到密钥
                } else {
                    matchedKeys.append(",").append(key); // 找到多个密钥
                }
            }
        }
        return matchedKeys.toString();
    }

    public static void main(String[] args) {
        DES des = new DES();
        des.show();
    }

}