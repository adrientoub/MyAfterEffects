
package controller;

import framework.Application;
import framework.Controller;
import model.PreviewModel;
import view.PreviewView;

public final class ExportController extends Controller<PreviewModel, PreviewView> {
    public ExportController(final Application application) {
        super(application);
    }

    public void clear() {
        this.model().clear();
    }
}
