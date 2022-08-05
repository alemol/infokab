package mx.geoint.VideoCutter;

import com.xuggle.xuggler.Global;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.MediaListenerAdapter;
import com.xuggle.mediatool.MediaToolAdapter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.mediatool.event.IAudioSamplesEvent;
import com.xuggle.mediatool.event.IReadPacketEvent;
import com.xuggle.mediatool.event.IVideoPictureEvent;
import com.xuggle.mediatool.event.IWriteHeaderEvent;
import com.xuggle.mediatool.event.IWritePacketEvent;
import com.xuggle.mediatool.event.IWriteTrailerEvent;
import org.apache.commons.io.FilenameUtils;

public class VideoCutter extends MediaListenerAdapter {

    private String TMP_DIR;

    //Tengo que usar varios escritores, uno para cada parte de la película a cortar.
    private IMediaWriter[] writers;

    //public Cutter(double[] starts, double[] ends, String videoPathin, String videoPathout)
    public boolean Cutter(String videoPathin, double s, double e, String videoPathout) {
        double starts[] = new double[1];
        starts[0] = s;
        double ends[] = new double[1];
        ends[0] = e;
        String basePath = FilenameUtils.getPath(videoPathin) + "multimedia/";
        existDirectory(basePath);

        try {
            //writers = new IMediaWriter[starts.length];
            writers = new IMediaWriter[starts.length];
            IMediaReader reader = ToolFactory.makeReader(videoPathin);
            reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

            //TMP_DIR = videoPathout + "_algo";
            //File tmpdir = new File(TMP_DIR);
            //tmpdir.mkdir(); //se crea la carpeta temporal para guardar la salida
            //paso de segundos a nanosegundos
            for (int i = 0; i < starts.length; i++) {
                starts[i] *= Global.DEFAULT_PTS_PER_SECOND;
                ends[i] *= Global.DEFAULT_PTS_PER_SECOND;
                writers[i] = ToolFactory.makeWriter(basePath + videoPathout, reader); //comprende il nome del file
            }

            //creazione di un tool che mi taglia il video nei punti scelti
            videoCheck checkPos = new videoCheck(); //videocheck MediaToolAdapter
            reader.addListener(checkPos);
            //IMediaWriter writer = ToolFactory.makeWriter(videoPathout+".mp4", reader); //comprende il nome del file

            boolean updatedS = false;
            boolean updatedE = false;

            int rp = 0; //Posición relativa, cambia según el flujo de partes de la película
            //E' importante che le parti siano disgiunte
            //reader.
            while (reader.readPacket() == null) {
                if (!updatedS && (checkPos.timeInMilisec >= starts[rp])) {
                    System.out.print("\n" + rp);
                    updatedS = true; //da un certo punto inizio a convertire
                    updatedE = false;
                    checkPos.addListener(writers[rp]);
                }

                if (!updatedE && (checkPos.timeInMilisec >= ends[rp])) {
                    System.out.print("-" + rp);
                    updatedE = true; //arrivato ad un certo punto smetto di convertire
                    checkPos.removeListener(writers[rp]);
                    writers[0].close();
                    rp++; //passo alla prossima parte del filmato
                    System.out.println(rp);
                    //if(rp == starts.length)
                    if (rp <= starts.length) { //se sono arrivato alla fine
                        System.out.print("\nCLOSE\n");
                        //writer.close(); //smetto di convertire
                    } else
                        updatedS = false;
                }
            }
        } catch (Exception err) {
            System.out.println("Ocurrió un error: ");
            err.printStackTrace();
            //System.exit(-1);
            return false;
        }


        //String OUT_FILE = videoPathout+".mp4";
        //Ottenuti i file separati li riunisco in un unico file
        //return concatenateVideoFromWriters(videoPathin, OUT_FILE);
        return true;
    }

    public HashMap<String, Boolean> Cutter2(double[] starts, double[] ends, String videoPathin, String[] videoPathout)
    {

        String basePath = FilenameUtils.getPath(videoPathin) + "multimedia/";
        existDirectory(basePath);

        writers = new IMediaWriter[starts.length];
        IMediaReader reader = ToolFactory.makeReader(videoPathin);
        reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        //passo da secondi a nanosecondi
        for(int i = 0; i < starts.length; i++)
        {
            starts[i]*=Global.DEFAULT_PTS_PER_SECOND;
            ends[i]*=Global.DEFAULT_PTS_PER_SECOND;
            writers[i] =  ToolFactory.makeWriter(basePath + videoPathout[i], reader); //comprende il nome del file
        }

        //creazione di un tool che mi taglia il video nei punti scelti
        videoCheck checkPos = new videoCheck(); //videocheck estende MediaToolAdapter
        reader.addListener(checkPos);
        //IMediaWriter writer = ToolFactory.makeWriter(videoPathout+".flv", reader); //comprende il nome del file

        boolean updatedS = false;
        boolean updatedE = false;

        int rp = 0; //Relative Position, cambia in base allo scorrere delle parti del filmato
        //E' importante che le parti siano disgiunte
        //reader.
        HashMap<String, Boolean>  CreatedList = new HashMap<String, Boolean>();
        while(reader.readPacket() == null)
        {
            try{
                if(!updatedS && (checkPos.timeInMilisec >= starts[rp]))
                {
                    System.out.print("\n" + rp);
                    updatedS = true; //da un certo punto inizio a convertire
                    updatedE = false;
                    checkPos.addListener(writers[rp]);
                }

                if(!updatedE && (checkPos.timeInMilisec >= ends[rp] ))
                {
                    System.out.print("-" + rp);
                    updatedE = true; //arrivato ad un certo punto smetto di convertire
                    checkPos.removeListener(writers[rp]);
                    //writers[rp].close();
                    CreatedList.put(basePath + videoPathout[rp],true);
                    rp++; //passo alla prossima parte del filmato
                    if(rp == starts.length)
                    { //se sono arrivato alla fine
                        System.out.print("\nCLOSE\n");
                        //writer.close(); //smetto di convertire
                    }
                    else
                        updatedS = false;
                }
            } catch (Exception err) {
                System.out.println("Ocurrió un error: ");
                err.printStackTrace();
                //System.exit(-1);
                CreatedList.put(basePath + videoPathout[rp],false);
            }


        }

        //String OUT_FILE = videoPathout+".flv";
        //Ottenuti i file separati li riunisco in un unico file
        //concatenateVideoFromWriters(OUT_FILE);
        return CreatedList;

    }

    public HashMap<String, Boolean> CutterList(double[] starts, double[] ends, String videoPathin, String[] videoPathout) {
        String basePath = FilenameUtils.getPath(videoPathin) + "multimedia/";
        existDirectory(basePath);
        HashMap<String, Boolean> CreatedList = new HashMap<String, Boolean>();

        writers = new IMediaWriter[starts.length];
        IMediaReader reader = ToolFactory.makeReader(videoPathin);
        reader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

        /*TMP_DIR = videoPathout + "_tmp";
        File tmpdir = new File(TMP_DIR);
        tmpdir.mkdir(); //creo una cartella temporanea per salvere i frammenti*/
        //passo da secondi a nanosecondi
        for (int i = 0; i < starts.length; i++) {
            starts[i] *= Global.DEFAULT_PTS_PER_SECOND;
            ends[i] *= Global.DEFAULT_PTS_PER_SECOND;
            writers[i] = ToolFactory.makeWriter(basePath + videoPathout[i], reader); //comprende il nome del file
        }

        //creazione di un tool che mi taglia il video nei punti scelti
        videoCheck checkPos = new videoCheck(); //videocheck estende MediaToolAdapter
        reader.addListener(checkPos);
        //IMediaWriter writer = ToolFactory.makeWriter(videoPathout+".flv", reader); //comprende il nome del file

        boolean updatedS = false;
        boolean updatedE = false;

        int rp = 0; //Relative Position, cambia in base allo scorrere delle parti del filmato
        //E' importante che le parti siano disgiunte
        //reader.

        while (reader.readPacket() == null) {
            try {
                if (!updatedS && (checkPos.timeInMilisec >= starts[rp])) {
                    System.out.print("\n" + rp);
                    updatedS = true; //da un certo punto inizio a convertire
                    updatedE = false;
                    checkPos.addListener(writers[rp]);
                }

                if (!updatedE && (checkPos.timeInMilisec >= ends[rp])) {
                    System.out.print("-" + rp);
                    updatedE = true; //arrivato ad un certo punto smetto di convertire
                    checkPos.removeListener(writers[rp]);
                    //writers[rp].close();
                    CreatedList.put(TMP_DIR + "/p" + rp + ".flv", true);
                    rp++; //passo alla prossima parte del filmato
                    if (rp == starts.length) { //se sono arrivato alla fine
                        System.out.print("\nCLOSE\n");
                        for (int i = 0; i <= writers.length; i++) {
                            writers[i].close();
                        }
                        //writers[rp].close(); //smetto di convertire
                    } else
                        updatedS = false;
                }
            } catch (Exception err) {
                System.out.println("Ocurrió un error: ");
                err.printStackTrace();
                //System.exit(-1);
                CreatedList.put(TMP_DIR + "/p" + rp + ".flv", false);
            }
        }

        //String OUT_FILE = videoPathout+".flv";
        //Ottenuti i file separati li riunisco in un unico file
        //concatenateVideoFromWriters(OUT_FILE);
        return CreatedList;

    }

    public boolean concatenateVideoFromWriters(String source, String OUT_FILE) {
        //Si el fragmento es único, solo tengo que moverlo y renombrarlo
        if (writers.length == 1) {
            String basePath = FilenameUtils.getPath(source) + "/multimedia/";
            new File(writers[0].getUrl()).renameTo(new File(basePath + OUT_FILE));
        }
        /*else
        {
            //concateno i primi due e proseguo dal terzo
            new MyConcatenateAudioAndVideo().concatenate(writers[0].getUrl(),writers[1].getUrl(),TMP_DIR+"/d1.mp4");
            int i;
            for(i = 2; i < writers.length; i++)
                new MyConcatenateAudioAndVideo().concatenate(TMP_DIR+"/d"+(i-1)+".mp4",writers[i].getUrl(),TMP_DIR+"/d"+i+".mp4");
            //sposto il file
            new File(TMP_DIR+"/d"+(i-1)+".mp4").renameTo(new File(OUT_FILE));
        }*/
        //elimino tutto
        deleteAllFromTmpFolder();

        return true;
    }

    public void deleteAllFromTmpFolder() {
        System.out.print("deleting tmpfile and folder" + TMP_DIR + " \n");
        File td = new File(TMP_DIR);
        String[] fileslist = td.list();
        for (String fpath : fileslist) {
            System.out.print(fpath + " \n");
            new File(TMP_DIR + "/" + fpath).delete(); //elimino i file nella cartella
        }
        td.delete(); //elimino la cartella
    }

    class videoCheck extends MediaToolAdapter {
        //Devono essere millisecondi
        public Long timeInMilisec = (long) 0;
        public boolean convert = true;

        @Override
        public void onVideoPicture(IVideoPictureEvent event) {
            timeInMilisec = event.getTimeStamp();  //mi ritorna il preciso istante in MICROsecondi
            //adesso chiamo la superclasse che continua con la manipolazione

            if (convert)
                super.onVideoPicture(event);
        }

        @Override
        public void onAudioSamples(IAudioSamplesEvent event) {
            if (convert)
                super.onAudioSamples(event);
        }

        @Override
        public void onWritePacket(IWritePacketEvent event) {
            if (convert)
                super.onWritePacket(event);
        }

        @Override
        public void onWriteTrailer(IWriteTrailerEvent event) {
            if (convert)
                super.onWriteTrailer(event);
        }

        @Override
        public void onReadPacket(IReadPacketEvent event) {
            if (convert)
                super.onReadPacket(event);
        }

        @Override
        public void onWriteHeader(IWriteHeaderEvent event) {
            if (convert)
                super.onWriteHeader(event);
        }
    }

    private String existDirectory(String pathDirectory) {
        String currentDirectory = pathDirectory;

        if (!Files.exists(Path.of(pathDirectory))) {
            File newSubDirectory = new File(pathDirectory);
            newSubDirectory.mkdirs();
        }

        return currentDirectory;
    }
}