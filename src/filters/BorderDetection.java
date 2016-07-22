package filters;

/**
 * Created by Adrien on 20/07/2016.
 */
public class BorderDetection extends ConvolutionMatrix {
    public BorderDetection() {
        super(new float[]{0, 1, 0,
                1, -4, 1,
                0, 1, 0});
    }
}
