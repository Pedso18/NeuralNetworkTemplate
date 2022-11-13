import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.Random;

public class Index {

    private static int[] networkLayerSizes = { 2, 4, 2 };

    public static void main(String[] args) {
        Network net = new Network(networkLayerSizes);

        int amountOfInputs = 100;
        DataPoint[] inputs = generateInputs(amountOfInputs);

        for (int w = 0; w < 50; w++) {
            for (int a = 0; a < amountOfInputs; a++) {
                for (int i = 0; i < 1000; i++) {
                    net.train(inputs[a].input, inputs[a].target, 0.3);
                }
            }
        }

        generateInputs(amountOfInputs);

        for (int i = 0; i < amountOfInputs; i++) {

            double[] o = net.calculate(inputs[i].input);

            o[0] = round(o[0], 3);
            o[1] = round(o[1], 3);

            System.out.println("Expected: " + Arrays.toString(inputs[i].target));
            System.out.println("Resulted: " + Arrays.toString(o));
            System.out.println("----------------------");

        }

        saveNetwork(net);

    }

    public static DataPoint[] generateInputs(int howMany) {

        Random rand = new Random();

        DataPoint[] newInputs = new DataPoint[howMany];

        for (int i = 0; i < howMany; i++) {

            double[] currInput = new double[2];
            double[] currTarget = new double[2];

            currInput[0] = rand.nextDouble();
            currInput[1] = rand.nextDouble();

            if (currInput[0] + currInput[1] >= 1) {
                currTarget[0] = 0;
                currTarget[1] = 1;
            } else {
                currTarget[0] = 1;
                currTarget[1] = 0;
            }

            newInputs[i] = new DataPoint(currInput, currTarget);

        }

        return newInputs;

    }

    private static double round(double value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (double) Math.round(value * scale) / scale;
    }

    public static void saveNetwork(Network net) {
        try {
            FileOutputStream f = new FileOutputStream(new File("savedNetwork.txt"));
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(net);

            o.close();
            f.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static Network readNetwork() {
        try {
            FileInputStream fi = new FileInputStream(new File("myObjects.txt"));
            ObjectInputStream oi;
            oi = new ObjectInputStream(fi);

            // Read objects
            Network net = (Network) oi.readObject();

            oi.close();
            fi.close();

            return net;

        } catch (IOException | ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

}
