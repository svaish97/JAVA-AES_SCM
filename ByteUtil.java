
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


/**
 * @author Anurag Jain (get.anurag@gmail.com)
 **/
public class ByteUtil {


    public static String hexToString(byte[] bytes, int offset, int lenth) {
        int max = offset + lenth;
        StringBuilder t1 = new StringBuilder();
        for (int i = offset; i < max; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                t1.append('0');
            }
            t1.append(hex);
        }
        return t1.toString();
    }

    public static byte[] doXOR(byte[] bytes, int offset, int length, byte[] operator) {
        byte[] result = new byte[length];

        for (int i = 0; i < length; i++) {
            result[i] = bytes[i + offset];
        }

        for (int i = 0; i < length; i++) {
            int index = i % operator.length;
            result[i] = (byte) (result[i] ^ operator[index]);
        }

        return result;
    }


    public static Integer hexToNumber(byte[] bytes, int offset, int length) {
        int result = 0;
        if (length > 4) {
            length = 4; //this is max a int can take
        }
        for (int i = 0; i < length; i++) {
            result |= bytes[offset + i] & 0xFF;
            if (i < (length - 1)) {
                result <<= 8 * i;
            }
        }
        return result;
    }

    public static Integer littleEndianHexToNumber(byte[] bytes, int offset, int length) {
        int result = 0;
        if (length > 4) {
            length = 4; //this is max a int can take
        }
        for (int i = length - 1; i >= 0; i--) {
            result |= bytes[offset + i] & 0xFF;
            if (i != 0) {
                result <<= 8 * i;
            }
        }
        return result;
    }

    public static String byteArrayToString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if (i > 0) {
                sb.append("\n");
            }
            sb.append(String.format("%d - 0x%02X", i, bytes[i]));
        }
        return sb.toString();
    }

    public static byte[] hex2byte(byte[] b, int offset, int len) {
        byte[] d = new byte[len];
        for (int i = 0; i < len * 2; ++i) {
            int shift = (i % 2 == 1) ? 0 : 4;
            int tmp35_34 = (i >> 1);
            byte[] tmp35_30 = d;
            tmp35_30[tmp35_34] = (byte) (tmp35_30[tmp35_34]
                | Character.digit((char) b[(offset + i)], 16) << shift);
        }
        return d;
    }

    public static byte[] hex2byte(String s) {
        if (s.length() % 2 == 0) {
            return hex2byte(s.getBytes(), 0, s.length() >> 1);
        }

        return hex2byte("0" + s);
    }

    public static void reverseByteArray(byte[] array) {
        int i, j;
        byte temp;
        for (i = 0, j = array.length - 1; i < j; i++, j--) {
            temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    public static byte[] getByteArray(int array[]) {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            DataOutputStream dos = new DataOutputStream(bout);
            for (int index = 0; index < array.length; index++) {
                dos.writeByte((array[index] & 0xff));
                dos.writeByte((array[index] >> 8 & 0xff));
                dos.writeByte((array[index] >> 16 & 0xff));
                dos.writeByte((array[index] >> 24 & 0xff));
            }
            dos.close();
        } catch (Exception e) {

        }
        return bout.toByteArray();
    }

    public static String hexor(String op1, String op2) {
        byte[] xor = xor(hex2byte(op1), hex2byte(op2));
        return hexString(xor);
    }

    public static byte[] xor(byte[] op1, byte[] op2) {
        byte[] result = null;
        // Use the smallest array
        if (op2.length > op1.length) {
            result = new byte[op1.length];
        } else {
            result = new byte[op2.length];
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = (byte) (op1[i] ^ op2[i]);
        }
        return result;
    }

    public static String hexString(byte[] b) {
        StringBuilder d = new StringBuilder(b.length * 2);
        for (byte aB : b) {
            char hi = Character.forDigit((aB >> 4) & 0x0F, 16);
            char lo = Character.forDigit(aB & 0x0F, 16);
            d.append(Character.toUpperCase(hi));
            d.append(Character.toUpperCase(lo));
        }
        return d.toString();
    }

    public static String getPrefixedHexLengthtoData(String hex) {
        byte bytes[] = hex2byte(hex);
        int len = bytes.length;
        String hl = Integer.toHexString(len);
        if (hl.length() < 2) {
            hl = (new StringBuilder()).append("0").append(hl).toString();
        }
        return (new StringBuilder()).append(hl).append(hex).toString();
    }

    public static String getPrefixedHex2BytesLengthtoData(String hex) {
        byte bytes[] = hex2byte(hex);
        int len = bytes.length;
        String hl = Integer.toHexString(len);
        if (hl.length() == 1) {
            hl = (new StringBuilder()).append("000").append(hl).toString();
        } else if (hl.length() == 2) {
            hl = (new StringBuilder()).append("00").append(hl).toString();
        } else if (hl.length() == 3) {
            hl = (new StringBuilder()).append("0").append(hl).toString();
        }
        return (new StringBuilder()).append(hl).append(hex).toString();
    }

    public static String getLVHexData(ByteBuffer buffer) {
        int len = buffer.get();
        byte data[] = new byte[len];
        buffer.get(data);
        return hexString(data);
    }

    public static byte[] getLVHexDataBytes(ByteBuffer buffer) {
        int len = buffer.get();
        byte data[] = new byte[len];
        buffer.get(data);
        return data;
    }

    public static byte[] getDataFromBuffer(ByteBuffer buffer) {
        int len = buffer.remaining();
        byte data[] = new byte[len];
        buffer.get(data);
        return data;
    }

    public static String getLVASCIIData(ByteBuffer buffer) {
        int len = buffer.get();
        byte data[] = new byte[len];
        buffer.get(data);
        return getISO8859EncodedString(data);
    }

    public static String getASCIIData(ByteBuffer buffer, int len) {
        byte data[] = new byte[len];
        buffer.get(data);
        return getISO8859EncodedString(data);
    }

    public static String read(ByteBuffer buffer, int len) {
        byte b[] = new byte[len];
        buffer.get(b);
        return hexString(b);
    }

    public static String getLHex(String asc) {
        byte bytes[] = getISO8859EncodedBytes(asc);
        int len = bytes.length;
        String hex = hexString(bytes);
        String hl = Integer.toHexString(len);
        if (hl.length() < 2) {
            hl = (new StringBuilder()).append("0").append(hl).toString();
        }
        return (new StringBuilder()).append(hl).append(hex).toString();
    }

    public static String getHex(String asc) {
        byte bmid[] = getISO8859EncodedBytes(asc);
        return hexString(bmid);
    }

    public static byte[] readBytes(ByteBuffer buffer, int len) {
        byte b[] = new byte[len];
        buffer.get(b);
        return b;
    }


    private static String getISO8859EncodedString(byte data[]) {
        try {
            return new String(data, "ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            return new String(data);
        }
    }

    private static byte[] getISO8859EncodedBytes(String data) {
        try {
            return data.getBytes("ISO_8859_1");
        } catch (UnsupportedEncodingException e) {
            return data.getBytes();
        }
    }

    public static byte[] compressWithDeflater(byte[] data) {
        try {
            Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION);
            deflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            deflater.finish();
            byte[] buffer = new byte[1024];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer); // returns the generated code... index
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            byte[] output = outputStream.toByteArray();
            deflater.end();
            return output;
        } catch (IOException e) {
        }
        return null;
    }


    public static byte[] decompressWithInflater(byte[] data) {
        try {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            byte[] output = outputStream.toByteArray();
            inflater.end();
            return output;
        } catch (IOException e) {
           
        } catch (DataFormatException e) {
            
        }
        return null;
    }
}
