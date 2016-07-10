import uk.co.caprica.vlcj.discovery.NativeDiscovery;
import view.MainWindow;

/**
 * Created by Adrien on 10/07/2016.
 */
public class Main {
    public static void main(String[] args) {
        new NativeDiscovery().discover();
        MainWindow.getMainWindow().display();
    }
}
