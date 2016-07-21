package process;

import model.ExportModel;

/**
 * Created by Adrien on 21/07/2016.
 */
public class ProcessThreaded extends Thread {
    private ExportModel exportModel;
    private String path;

    public ProcessThreaded(ExportModel exportModel, String path) {
        this.exportModel = exportModel;
        this.path = path;
    }

    @Override
    public void run() {
        exportModel.process(path);
    }
}
