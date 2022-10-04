import java.util.Arrays;

public class Index {
    public static void main(String[] args) {
        Network net = new Network(4, 1, 3, 4);

        double[] input = new double[] { 0.1, 0.5, 0.2, 0.9 };
        double[] target = new double[] { 0, 0, 1, 0 };

        for (int i = 0; i < 1000; i++) {
            net.train(input, target, 0.3);
        }

        double[] o = net.calculate(input);
        System.out.println(Arrays.toString(o));
    }
}
