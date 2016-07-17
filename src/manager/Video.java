package manager;

import java.io.File;
import java.sql.Time;

/**
 * Created by Damien on 17/07/2016.
 */
public class Video {

    private float _fps;
    private int _nbframes;
    private Time duration;
    private File f;

    public Video(File f) {
        duration = new Time(0, 2, 30);
        // TODO
    }

    public float get_fps() {
        return _fps;
    }

    public void set_fps(float _fps) {
        this._fps = _fps;
    }

    public int get_nbframes() {
        return _nbframes;
    }

    public void set_nbframes(int _nbframes) {
        this._nbframes = _nbframes;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(Time duration) {
        this.duration = duration;
    }

    public String getName() {
        return f.getName();
    }
}
