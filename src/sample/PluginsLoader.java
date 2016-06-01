package sample;

/**
 * Created by Loïc on 28/05/2016.
 **/
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;


public class PluginsLoader {

    private String[] files = null;

    private ArrayList<Class> classStringPlugins;
    private ArrayList<Class> classIntPlugins;


    /**
     * Constucteur initialisant le tableau de fichier à charger.
     */
    public PluginsLoader(){
        this.classIntPlugins = new ArrayList<>();
        this.classStringPlugins = new ArrayList<>();

        File dir = new File ("plugin/");
        System.out.println(dir.getName());
        this.files = dir.list();
        if (this.files != null) {
            System.out.println("liste des plugins : ");
                for (String file : this.files) {
                    System.out.println(file);
                }
        }
    }

    public void setFiles(String[] files ){
        this.files = files;
    }

    /**
     * Fonction de chargement de tout les plugins de type StringPlugins
     * @return Une collection de StringPlugins contenant les instances des plugins
     * @throws Exception si file = null ou file.length = 0
     */
    public StringPlugins[] loadAllStringPlugins() throws Exception {
        StringPlugins[] tmpPlugins = null;
        if (this.files.length != 0) {
            this.initializeLoader();

                tmpPlugins = new StringPlugins[this.classStringPlugins.size()];

            System.out.println("LENGTH : " + tmpPlugins.length);

            for (int index = 0; index < tmpPlugins.length; index++) {
                System.out.println(" index : " + index);
                //On créer une nouvelle instance de l'objet contenu dans la liste grâce à newInstance()
                //et on le cast en StringPlugins. Vu que la classe implémente StringPlugins, le cast est toujours correct
                tmpPlugins[index] = (StringPlugins) (this.classStringPlugins.get(index)).newInstance();
                System.out.println(tmpPlugins[index].getLibelle());
            }
        }

        return tmpPlugins;
    }

    private void initializeLoader() throws Exception{
        //On vérifie que la liste des plugins à charger à été initialisé
        if(this.files == null || this.files.length == 0 ){
            throw new Exception("Pas de fichier spécifié");
        }

        //Pour eviter le double chargement des plugins
        if(!this.classIntPlugins.isEmpty() || !this.classStringPlugins.isEmpty()){
            return ;
        }

        File[] f = new File[this.files.length];
		// Pour charger le .jar en memoire
        URLClassLoader loader;
        //Pour la comparaison de chaines
        String tmp;
        //Pour le contenu de l'archive jar
        Enumeration enumeration;
        //Pour déterminé quels sont les interfaces implémentées
        Class tmpClass;

        for(int index = 0 ; index < f.length ; index ++ ){

            f[index] = new File(this.files[index]);

            URL u = f[index].toURL();
            //On créer un nouveau URLClassLoader pour charger le jar qui se trouve ne dehors du CLASSPATH
            loader = new URLClassLoader(new URL[] {u});

            //On charge le jar en mémoire
            JarFile jar = new JarFile(f[index].getAbsolutePath());

            //On récupére le contenu du jar
            enumeration = jar.entries();

            while(enumeration.hasMoreElements()){
                tmp = enumeration.nextElement().toString();

                //On vérifie que le fichier courant est un .class (et pas un fichier d'informations du jar )
                if(tmp.length() > 6 && tmp.substring(tmp.length()-6).compareTo(".class") == 0) {
                    tmp = tmp.substring(0,tmp.length() - 6);
                    tmp = tmp.replaceAll("/", ".");

                    tmpClass = Class.forName(tmp ,true, loader);
                    System.out.println("interface : " + tmpClass.getInterfaces().length);

                    for(int i = 0 ; i < tmpClass.getInterfaces().length; i ++){
                        System.out.println(tmpClass.getInterfaces()[i].getName());

                        //Une classe ne doit pas appartenir à deux catégories de plugins différents.
                        //Si tel est le cas on ne la place que dans la catégorie de la première interface correct
                        // trouvée
                        if("sample.StringPlugins".equals(tmpClass.getInterfaces()[i].getName()))
                            this.classStringPlugins.add(tmpClass);
                        else if("sample.IntPlugins".equals(tmpClass.getInterfaces()[i].getName()))
                            this.classIntPlugins.add(tmpClass);
                    }
                }
            }
        }
    }
}