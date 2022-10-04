import java.util.Arrays;

public class Network {

    private double[][] output; // [currentLayer][currentNeuron]
    private double[][][] weights; // [currentLayer][currentNeuron][prevNeuron]
    private double[][] bias; // [currentLayer][currentNeuron]

    private double[][] errorSignal;
    private double[][] outputDerivative;

    public final int[] networkLayerSizes;
    public final int inputSize;
    public final int outputSize;
    public final int networkSize;

    public Network(int... networkLayerSizes) {
        this.networkSize = networkLayerSizes.length;
        this.networkLayerSizes = networkLayerSizes;
        this.inputSize = networkLayerSizes[0];
        this.outputSize = networkLayerSizes[networkLayerSizes.length - 1];

        this.bias = new double[networkSize][];
        this.weights = new double[networkSize][][];
        this.output = new double[networkSize][];

        this.errorSignal = new double[networkSize][];
        this.outputDerivative = new double[networkSize][];

        for (int i = 0; i < networkSize; i++) {
            this.output[i] = new double[networkLayerSizes[i]];

            this.errorSignal[i] = new double[networkLayerSizes[i]];
            this.outputDerivative[i] = new double[networkLayerSizes[i]];

            this.bias[i] = NetworkTools.createRandomArray(networkLayerSizes[i], 0.3, 0.7);

            if (i > 0) {
                weights[i] = NetworkTools.createRandomArray(networkLayerSizes[i], networkLayerSizes[i - 1], -0.3, 0.5);
            }
        }

    }

    public double[] calculate(double... input) {

        if (input.length != this.inputSize) {
            System.out.println("something got really messed up");
            return null;
        }

        this.output[0] = input;

        for (int layer = 1; layer < networkSize; layer++) {
            for (int neuron = 0; neuron < networkLayerSizes[layer]; neuron++) {

                double sum = bias[layer][neuron];

                for (int prevNeuron = 0; prevNeuron < networkLayerSizes[layer - 1]; prevNeuron++) {
                    sum += output[layer - 1][prevNeuron] * weights[layer][neuron][prevNeuron];
                }

                output[layer][neuron] = sigmoid(sum);
                outputDerivative[layer][neuron] = output[layer][neuron] * (1 - output[layer][neuron]);

            }
        }

        return output[networkSize - 1];

    }

    public void train(double[] input, double[] target, double eta) {
        if (input.length != inputSize || target.length != outputSize) {
            return;
        }

        calculate(input);
        backPropError(target);
        updateWeights(eta);

    }

    public void backPropError(double[] target) {

        for (int neuron = 0; neuron < networkLayerSizes[networkSize - 1]; neuron++) {
            errorSignal[networkSize - 1][neuron] = (output[networkSize - 1][neuron] - target[neuron])
                    * outputDerivative[networkSize - 1][neuron];
        }

        for (int layer = networkSize - 2; layer > 0; layer--) {
            for (int neuron = 0; neuron < networkLayerSizes[layer]; neuron++) {
                double sum = 0;
                for (int nextNeuron = 0; nextNeuron < networkLayerSizes[layer + 1]; nextNeuron++) {
                    sum += weights[layer + 1][nextNeuron][neuron] * errorSignal[layer + 1][nextNeuron];
                }
                this.errorSignal[layer][neuron] = sum * outputDerivative[layer][neuron];
            }
        }

    }

    public void updateWeights(double eta) {
        for (int layer = 1; layer < networkSize; layer++) {
            for (int neuron = 0; neuron < networkLayerSizes[layer]; neuron++) {
                for (int prevNeuron = 0; prevNeuron < networkLayerSizes[layer - 1]; prevNeuron++) {
                    double delta = -eta * output[layer - 1][prevNeuron] * errorSignal[layer][neuron];
                    weights[layer][neuron][prevNeuron] += delta;
                }
                double delta = -eta * errorSignal[layer][neuron];
                bias[layer][neuron] += delta;
            }
        }
    }

    private double sigmoid(double x) {
        return 1d / (1 + Math.exp(-x));
    }

}