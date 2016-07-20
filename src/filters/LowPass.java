package filters;

/**
 * Created by Adrien on 20/07/2016.
 */
public class LowPass extends ConvolutionMatrix {
    public LowPass() {
        super(new float[]{1 / 9f, 1 / 9f, 1 / 9f,
                1 / 9f, 1 / 9f, 1 / 9f,
                1 / 9f, 1 / 9f, 1 / 9f});
    }
}
