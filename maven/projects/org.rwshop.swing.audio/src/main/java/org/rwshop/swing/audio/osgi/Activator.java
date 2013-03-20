package org.rwshop.swing.audio.osgi;

import javax.swing.UIManager;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.rwshop.swing.audio.wav.WavLoaderFrame;

public class Activator implements BundleActivator {

    public void start(BundleContext context) throws Exception {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try{
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    /*int fftLen = 512;
                    String file = "/home/matt/Desktop/Knockin On Heavens Door.wav";
                    WavMemoryBuffer wav = new WavMemoryBuffer(file, 0, -1, fftLen);
                    WavPlayerFrame player = new WavPlayerFrame();
                    player.init(wav);
                    player.setVisible(true);
                    WavSpectrogramFrame spect = new WavSpectrogramFrame();
                    spect.init(wav);
                    spect.setVisible(true);*/
                    new WavLoaderFrame().setVisible(true);
                }catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    public void stop(BundleContext context) throws Exception {
        // TODO add deactivation code here
    }

}
