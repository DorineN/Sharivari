package app.model;

import app.Main;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Loïc on 21/05/2016.
 */
public class Task {
    protected int idTask;
    protected String nameTask;
    protected String descriptionTask;
    protected int durationTask;
    protected int idPriority;
    protected Date estimateStartDateTask;
    protected Date realStartDateTask;
    protected Date estimateEndDateTask;
    protected Date realEndDateTask;
    protected int idStatus;
    protected int idProject;
    protected String nameStatus;
    protected String namePriority;
    protected String nameProject = "";
    protected String nameUser;
    private Main mainApp;

    public Task(){
        // Empty constructor
    }

    public Task(int idTask, String nameTask, String descriptionTask, Date estimateStartDateTask, Date realStartDateTask, Date estimateEndDateTask, Date realEndDateTask, int idProject, int idPriority, String nameStatus){
        this.idTask=idTask;
        this.nameTask=nameTask;
        this.descriptionTask=descriptionTask;
        this.estimateStartDateTask = estimateStartDateTask;
        this.realStartDateTask=realStartDateTask;
        this.estimateEndDateTask=estimateEndDateTask;
        this.realEndDateTask=realEndDateTask;
        this.idProject=idProject;
        this.idPriority=idPriority;
        this.nameStatus=nameStatus;

    }

    public Task(int idTask, String nameTask, String descriptionTask, Date estimateStartDateTask, Date realStartDateTask, Date estimateEndDateTask, Date realEndDateTask, int idProject, int idPriority, String nameStatus, String nameProject){
        this.idTask=idTask;
        this.nameTask=nameTask;
        this.descriptionTask=descriptionTask;
        this.estimateStartDateTask = estimateStartDateTask;
        this.realStartDateTask=realStartDateTask;
        this.estimateEndDateTask=estimateEndDateTask;
        this.realEndDateTask=realEndDateTask;
        this.idProject=idProject;
        this.idPriority=idPriority;
        this.nameStatus=nameStatus;
        this.nameProject = nameProject;

    }

    public Task(int idTask, String nameTask, String descriptionTask, int durationTask, int idPriority, Date estimateStartDateTask, Date estimateEndDateTask,
                Date realStartDateTask, Date realEndDateTask, int idStatus, int idProject, String nameStatus, String namePriority){
        this.idTask=idTask;
        this.nameTask=nameTask;
        this.descriptionTask=descriptionTask;
        this.durationTask=durationTask;
        this.estimateStartDateTask = estimateStartDateTask;
        this.realStartDateTask=realStartDateTask;
        this.estimateEndDateTask=estimateEndDateTask;
        this.realEndDateTask=realEndDateTask;
        this.idPriority = idPriority;
        this.idStatus = idStatus;
        this.idProject = idProject;
        this.nameStatus = nameStatus;
        this.namePriority = namePriority;
    }



    public Task(int idTask, String nameTask, String descriptionTask, int durationTask, int idPriority, Date estimateStartDateTask, Date estimateEndDateTask,
                Date realStartDateTask, Date realEndDateTask, int idStatus, int idProject, String nameStatus, String namePriority, String nameUser){
        this.idTask=idTask;
        this.nameTask=nameTask;
        this.descriptionTask=descriptionTask;
        this.durationTask=durationTask;
        this.estimateStartDateTask = estimateStartDateTask;
        this.realStartDateTask=realStartDateTask;
        this.estimateEndDateTask=estimateEndDateTask;
        this.realEndDateTask=realEndDateTask;
        this.idPriority = idPriority;
        this.idStatus = idStatus;
        this.idProject = idProject;
        this.nameStatus = nameStatus;
        this.namePriority = namePriority;



    }

    public void setNamePriority(String namePriority) {
        this.namePriority = namePriority;
    }

    public int getIdProject() {
        return idProject;
    }

    public String getNameProject() {
        return nameProject;
    }

    public void setNameProject(String nameProject) {
        this.nameProject = nameProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public int getIdPriority() {
        return idPriority;
    }

    public void setIdPriority(int idPriority) {
        this.idPriority = idPriority;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }

    public int getIdTask() {
        return idTask;
    }

    public void setIdTask(int idTask) {
        this.idTask = idTask;
    }

    public String getNameTask() {
        return nameTask;
    }

    public void setNameTask(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getDescriptionTask() {
        return descriptionTask;
    }

    public void setDescriptionTask(String descriptionTask) {
        this.descriptionTask = descriptionTask;
    }

    public Date getEstimateStartDateTask() {
        return estimateStartDateTask;
    }

    public void setEstimateStartDateTask(Date estimateStartDateTask) {
        this.estimateStartDateTask = estimateStartDateTask;
    }

    public Date getRealStartDateTask() {
        return realStartDateTask;
    }

    public void setRealStartDateTask(Date realStartDateTask) {
        this.realStartDateTask = realStartDateTask;
    }

    public Date getEstimateEndDateTask() {
        return estimateEndDateTask;
    }

    public void setEstimateEndDateTask(Date estimateEndDateTask) {
        this.estimateEndDateTask = estimateEndDateTask;
    }

    public Date getRealEndDateTask() {
        return realEndDateTask;
    }

    public void setRealEndDateTask(Date realEndDateTask) {
        this.realEndDateTask = realEndDateTask;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setDurationTask(int durationTask) {
        this.durationTask = durationTask;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getDurationTask() {
        return durationTask;
    }

    public String getNamePriority() {
        return namePriority;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }


    public void setMainApp(Main main){
        this.mainApp = main;
        //Use to load all executant on view task (smiley immeuble)
        String concatUser = "";

        ArrayList<User> lUser  = new ArrayList<>();
        lUser = mainApp.taskDAO.findUsersTask(idTask);

        for (int j = 0; j<lUser.size(); j++){
            concatUser = concatUser + lUser.get(j).getUserLogin() + " (" + lUser.get(j).getUserName()+ " " + lUser.get(j).getUserFirstName()+")"+ "\n" ;
        }


        this.nameUser = concatUser;
    }

}