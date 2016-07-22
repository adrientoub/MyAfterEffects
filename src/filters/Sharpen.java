package filters;

/**
 * Created by Adrien on 20/07/2016.
 */
public class Sharpen extends ConvolutionMatrix {
    public Sharpen() {
        super(new float[]
                {0, -1, 0,
                -1, 5, -1,
                0, -1, 0});
    }
}
