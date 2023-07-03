package mx.geoint;

public class pathSystem {
    public static final String TIER_MAIN = "transcripcion literal";
    public static final String TIER_TRANSLATE = "traduccion libre";
    public static final String TIER_DEFAULT = "default-lt";

    public static final String INDEX_LANGUAJE_MAYA = "maya";
    public static final String INDEX_LANGUAJE_SPANISH = "español";
    public static final String INDEX_LANGUAJE_GLOSA = "glosado";
    public static final String INDEX_LANGUAJE_GLOSA_WORDS = "glosado_words";
    public static final String TIER_GlOSA = "glosa";
    public static final String TIER_GlOSA_INDEX = "glosa_index";
    public static final String TIER_GlOSA_INDEX_WORDS = "glosa_index_words";
    public static final String DIRECTORY_PUBLIC_MULTIMEDIA = "./Files/";
    public static final String DIRECTORY_INDEX_LUCENE = "./Files/index/";
    public static final String DIRECTORY_INDEX_GENERAL = "./Files/Indices/";

    public static final String DIRECTORY_PROJECTS = "./Files/Project/";
    public static final String DIRECTORY_LOG = "./Files/logs/";

    public static final Number SUCCESS_UPLOAD = 00;
    public static final Number NOT_UPLOAD_EAF_FILE = 01;
    public static final Number NOT_UPLOAD_MULTIMEDIA_FILE = 02;
    public static final Number ERROR_DATABASE_SAVE_PROJECT = 03;
    public static final Number NOT_UPLOAD_AUTORIZATION_FILE = 04;

    public static final String DIRECTORY_DOWNLOADS = "./Files/Descargas/";

    public static final String DIRECTORY_CSV = "./Files/CSV/";
}
