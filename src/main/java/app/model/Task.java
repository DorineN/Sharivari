package app.model;

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

    public Task(){
        // Empty constructor
    }

    public Task(int idTask, String nameTask, String descriptionTask, Date estimateStartDateTask, Date realStartDateTask, Date estimateEndDateTask, Date realEndDateTask, int idProject, int idPriority){
        this.idTask=idTask;
        this.nameTask=nameTask;
        this.descriptionTask=descriptionTask;
        this.estimateStartDateTask = estimateStartDateTask;
        this.realStartDateTask=realStartDateTask;
        this.estimateEndDateTask=estimateEndDateTask;
        this.realEndDateTask=realEndDateTask;
        this.idProject=idProject;
        this.idPriority=idPriority;

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

    public int getDurationTask() {
        return durationTask;
    }

    public void setDurationTask(int durationTask) {
        this.durationTask = durationTask;
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

    public int getIdPriority() {
        return idPriority;
    }

    public void setIdPriority(int idPriority) {
        this.idPriority = idPriority;
    }

    public int getIdStatus() {
        return idStatus;
    }

    public void setIdStatus(int idStatus) {
        this.idStatus = idStatus;
    }

    public int getIdProject() {
        return idProject;
    }

    public void setIdProject(int idProject) {
        this.idProject = idProject;
    }

    public String getNamePriority() {
        return namePriority;
    }

    public void setNamePriority(String namePriority) {
        this.namePriority = namePriority;
    }

    public String getNameStatus() {
        return nameStatus;
    }

    public void setNameStatus(String nameStatus) {
        this.nameStatus = nameStatus;
    }


    /*
    // Inherited methods
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Task task = (Task) o;
        if (idTask != task.idTask)
            return false;
        if (nameTask != null ? !nameTask.equals(task.nameTask) : task.nameTask != null)
            return false;
        if (descriptionTask != null ? !descriptionTask.equals(task.descriptionTask) : task.descriptionTask != null)
            return false;
        if (estimateStartDateTask != null ? !estimateStartDateTask.equals(task.estimateStartDateTask) : task.estimateStartDateTask != null)
            return false;
        if (realStartDateTask != null ? !realStartDateTask.equals(task.realStartDateTask) : task.realStartDateTask != null)
            return false;
        if (estimateEndDateTask != null ? !estimateEndDateTask.equals(task.estimateEndDateTask) : task.estimateEndDateTask != null)
            return false;
        if (realEndDateTask != null ? !realEndDateTask.equals(task.realEndDateTask) : task.realEndDateTask != null)
            return false;
    }
    @Override
    public int hashCode() {
        int result = idTask;
        result = 31 * result + (nameTask != null ? nameTask.hashCode() : 0);
        result = 31 * result + (descriptionTask != null ? descriptionTask.hashCode() : 0);
        result = 31 * result + (estimateStartDateTask != null ? estimateStartDateTask.hashCode() : 0);
        result = 31 * result + (realStartDateTask != null ? realStartDateTask.hashCode() : 0);
        result = 31 * result + (estimateEndDateTask != null ? estimateEndDateTask.hashCode() : 0);
        result = 31 * result + (realEndDateTask != null ? realEndDateTask.hashCode() : 0);
        return result;
    }*/
}