package app.util;

/**
 * Created by Loïc on 28/05/2016.
 */

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import app.Main;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarFile;


public class PluginsLoader {

    public String[] files;
    private Main mainApp;
    private ArrayList classControllerPlugins;
    private ArrayList classViewPlugins;
    private ArrayList classModelPlugins;


    /**
     * Constucteur initialisant le tableau de fichier à charger.
     */
    public PluginsLoader(){

        this.classControllerPlugins = new ArrayList();
        this.classViewPlugins = new ArrayList();
        this.classModelPlugins = new ArrayList();

    }

    /**
     * Défini l'ensemble des fichiers à charger
     * @param files
     */
    public void setFiles(String[] files ){
        this.files = files;
    }

    /**
     * Fonction de chargement de tout les plugins de type TypeControllerPlugins
     * @return Une collection de TypeControllerPlugins contenant les instances des plugins
     * @throws Exception si file = null ou file.length = 0
     */
    public TypeControllerPlugins[] loadAllControllerPlugins() throws Exception {

        this.initializeLoader();

        TypeControllerPlugins[] tmpPlugins = new TypeControllerPlugins[this.classControllerPlugins.size()];


        //  System.out.println("controller :");
        for(int index = 0 ; index < tmpPlugins.length; index ++ ){

            //On créer une nouvelle instance de l'objet contenu dans la liste grâce à newInstance()
            //et on le cast en TypeControllerPlugins. Vu que la classe implémente TypeControllerPlugins, le cast est toujours correct
            tmpPlugins[index] = (TypeControllerPlugins)((Class)this.classControllerPlugins.get(index)).newInstance() ;
            // Give the controller access to the main app.

            tmpPlugins[index].setMainApp(mainApp);
        }

        return tmpPlugins;
    }

    /**
     * Fonction de chargement de tout les plugins de type TypeModelPlugins
     * @return Une collection de TypeModelPlugins contenant les instances des plugins
     * @throws Exception si file = null ou file.length = 0
     */
    public TypeModelPlugins[] loadAllModelPlugins() throws Exception {

        this.initializeLoader();

        TypeModelPlugins[] tmpPlugins = new TypeModelPlugins[this.classModelPlugins.size()];

        // System.out.println("Model :");

        for(int index = 0 ; index < tmpPlugins.length; index ++ ){

            //On créer une nouvelle instance de l'objet contenu dans la liste grâce à newInstance()
            //et on le cast en TypeControllerPlugins. Vu que la classe implémente TypeControllerPlugins, le cast est toujours correct
            tmpPlugins[index] = (TypeModelPlugins)((Class)this.classModelPlugins.get(index)).newInstance() ;
            //System.out.println(tmpPlugins[index].getLibelle());

            tmpPlugins[index].setMainApp(mainApp);

        }

        return tmpPlugins;

    }

    public TypeViewPlugins[] loadAllViewPlugins() throws Exception {

        this.initializeLoader();

        TypeViewPlugins[] tmpPlugins = new TypeViewPlugins[this.classViewPlugins.size()];

        // System.out.println("View :");

        for(int index = 0 ; index < tmpPlugins.length; index ++ ){

            //On créer une nouvelle instance de l'objet contenu dans la liste grâce à newInstance()
            //et on le cast en TypeControllerPlugins. Vu que la classe implémente TypeControllerPlugins, le cast est toujours correct
            tmpPlugins[index] = (TypeViewPlugins)((Class)this.classViewPlugins.get(index)).newInstance() ;

            tmpPlugins[index].setMainApp(mainApp);

            Button myButton = tmpPlugins[index].buttonMenu();

            mainApp.addButton(myButton, index);


        }

        return tmpPlugins;

    }

    private void initializeLoader() throws Exception{
        //On vérifie que la liste des plugins à charger à été initialisé
        if(this.files == null || this.files.length == 0 ){
            // System.out.println("0 plug found");
        }

        //Pour eviter le double chargement des plugins
        if(this.classModelPlugins.size() != 0 || this.classControllerPlugins.size() != 0  || this.classViewPlugins.size() != 0 ){
            return ;
        }

        File[] f = new File[this.files.length];

//		Pour charger le .jar en memoire
        URLClassLoader loader;
        //Pour la comparaison de chaines
        String tmp = "";
        //Pour le contenu de l'archive jar
        Enumeration enumeration;
        //Pour déterminé quels sont les interfaces implémentées
        Class tmpClass = null;

        for(int index = 0 ; index < f.length ; index ++ ){

            f[index] = new File("plugin\\"+this.files[index]);

           /* if(!f[index].exists() ) {
                System.out.println("break pour "+index);
                break;
            }*/

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

                    tmp = tmp.substring(0,tmp.length()-6);
                    tmp = tmp.replaceAll("/",".");

                    tmpClass = Class.forName(tmp ,true, loader);

                    //  System.out.println(tmpClass.getInterfaces().length);

                    for(int i = 0 ; i < tmpClass.getInterfaces().length; i ++ ){


                        //Une classe ne doit pas appartenir à deux catégories de plugins différents.
                        //Si tel est le cas on ne la place que dans la catégorie de la première interface correct
                        // trouvée
                        if(tmpClass.getInterfaces()[i].getName().toString().equals("sample.util.TypeControllerPlugins") ) {
                            this.classControllerPlugins.add(tmpClass);
                        }
                        else if( tmpClass.getInterfaces()[i].getName().toString().equals("sample.util.TypeModelPlugins") ) {
                            this.classModelPlugins.add(tmpClass);
                        } else if( tmpClass.getInterfaces()[i].getName().toString().equals("sample.util.TypeViewPlugins") ) {
                            this.classViewPlugins.add(tmpClass);
                        }

                    }

                }
            }


        }

    }

    public void setMainApp(Main mainApp) {
        this.mainApp = mainApp;
    }

    public void checkAndLoadPlug(){

        File dir;
        dir = new File ("plugin/");

        if(!dir.exists()){
            dir.mkdir();
        }
        //first check for secu : if plug not added correctly suppress, if status = delete the plug
        this.files=dir.list();

        if(this.files.length>0) {
            for (int i = 0; i < this.files.length; i++) {
                String res = mainApp.pluginDAO.checkStatu(this.files[i]);
                if(res.equals("")){
                    File f = new File("plugin\\"+files[i]);
                    f.delete();
                }
            }
        }

        //Second check : ready to use
        dir = new File ("plugin/");
        this.files=dir.list();
    }
}