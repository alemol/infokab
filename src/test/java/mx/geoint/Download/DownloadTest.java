package mx.geoint.Download;

import mx.geoint.Model.SearchDoc;
import mx.geoint.Model.Search.SearchResponse;
import mx.geoint.Apis.Searcher.Searcher;
import org.apache.lucene.queryparser.classic.ParseException;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DownloadTest {



    @Test
    void generateZipTest() throws IOException {
        System.out.println("getZIP!!");

        String[] paths = {
                "/home/david/centroGEO/infoKab/repos/infokab/Files/Project/244939c7-6f54-466b-97be-8de93ef70e1d/eligio_prueba_1661552274740/multimedia/a1_oracion_ts73_ts77_72340_74300_eligio_prueba_1661552274740.wav",
                "/home/david/centroGEO/infoKab/repos/infokab/Files/Project/244939c7-6f54-466b-97be-8de93ef70e1d/eligio_prueba_1661552274740/multimedia/a5_oracion_ts1_ts5_34240_39000_eligio_prueba_1661552274740.wav",
                "/home/david/centroGEO/infoKab/repos/infokab/Files/Project/244939c7-6f54-466b-97be-8de93ef70e1d/eligio_prueba_1661552274740/multimedia/a6_oracion_ts9_ts13_39790_42750_eligio_prueba_1661552274740.wav",
                "/home/david/centroGEO/infoKab/repos/infokab/Files/Project/244939c7-6f54-466b-97be-8de93ef70e1d/eligio_prueba_1661552274740/multimedia/a7_oracion_ts17_ts21_43500_46500_eligio_prueba_1661552274740.wav"
        };

        List<String> filePaths = Arrays.asList(paths);
        String zipPath = "/home/david/centroGEO/infoKab/repos/infokab/Files/Project/244939c7-6f54-466b-97be-8de93ef70e1d/eligio_prueba_1661552274740/output.zip";

        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filePath : filePaths) {
                File fileToZip = new File(filePath);
                zipOut.putNextEntry(new ZipEntry(fileToZip.getName()));
                Files.copy(fileToZip.toPath(), zipOut);
            }
        }
        catch (Exception e){
            System.out.print("Error al generar el zip: "+e);
            File myObj = new File(zipPath);
            if (myObj.delete()) {
                System.out.println("Deleted the file: " + myObj.getName());
            } else {
                System.out.println("Failed to delete the file.");
            }
        }

    }


    @Test
    void generateZipFromSearch() throws IOException, ParseException {
        Searcher busqueda = new Searcher();

        SearchResponse results = busqueda.findMultiple("yaan");

        System.out.println(results.getTotalHits());
        //System.out.print(results.getDocuments());
        ArrayList<SearchDoc> documentsResult = results.getDocuments();
        //System.out.print(documentsResult);

        List<String> filePaths = new ArrayList<String>();

        for (int counter = 0; counter < documentsResult.size(); counter++) {
            System.out.println("**********************************************");
            System.out.println(documentsResult.get(counter).getOriginalPath()); //multimedia original
            System.out.println(documentsResult.get(counter).getFileName());     //npmbre del json
            System.out.println(documentsResult.get(counter).getBasePath());     //ruta multimedia
            System.out.println(documentsResult.get(counter).getText());         //texto
            System.out.println(documentsResult.get(counter).getMultimediaName()); //nombre de corte multimedia (sin extensión)
            System.out.println(documentsResult.get(counter).getTypePath());        //exteension del archivo multimedia

            String multimediaFile = documentsResult.get(counter).getBasePath()+documentsResult.get(counter).getMultimediaName()+"."+documentsResult.get(counter).getTypePath();
            System.out.println(multimediaFile);
            filePaths.add(multimediaFile);
        }
        System.out.println(filePaths);
    }

}
