import java.net.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class ImageThread extends Thread {
    private Socket socket = null;

    public ImageThread(Socket socket) {
        super("ImageThread");
        this.socket = socket;
    }

    public void run() {
        try {
            System.out.println("Processing Image");
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //BufferedImage imBuff = ImageIO.read(in);

            int nbrToRead = in.readInt();
            byte[] byteArray = new byte[nbrToRead];
            int nbrRd = 0;
            int nbrLeftToRead = nbrToRead;
            while(nbrLeftToRead > 0){
                int rd =in.read(byteArray, nbrRd, nbrLeftToRead);
                if(rd < 0)
                    break;
                nbrRd += rd; // accumulate bytes read
                nbrLeftToRead -= rd;
            }
            //Converting the image
            ByteArrayInputStream byteArrayI = new ByteArrayInputStream(byteArray);
            BufferedImage image = ImageIO.read(byteArrayI);

            System.out.println("Image Read");
            BufferedImage result = HistogramEQ.computeHistogramEQ(image);
            System.out.println("Image Processed");
            ImageIO.write(result, "JPG", out);
            System.out.println("Image Sent");
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
