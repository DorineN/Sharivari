package sample;

import java.util.Date;

/*************************************************************
 *************** Task class *****************
 *************************************************************
 *********** Created by Dorine on 14/05/2016.*****************
 ************************************************************/

public class Task {
    protected int idTask = 0;
    protected String nameTask = "";
    protected String descriptionTask = "";
    protected int priorityTask = 0;
    protected Date estimateStartDateTask = null;
    protected Date estimateEndDateTask = null;
    protected Date realStartDaTeTask = null;
    protected Date realEndDateTask = null;

    public Task(){
        // Empty constructor
    }

    public Task(int idTask, String nameTask, String descriptionTask,  int priorityTask, Date estimateStartDateTask, Date estimateEndDateTask, Date realStartDaTeTask, Date realEndDateTask){
        this.setTaskId(idTask);
        this.setTaskName(nameTask);
        this.setTaskDesc(descriptionTask);
        this.setTaskPriority(priorityTask);
        this.setTaskEsStart(estimateStartDateTask);
        this.setTaskEsEnd(estimateEndDateTask);
        this.setTaskStart(realStartDaTeTask);
        this.setTaskEnd(realEndDateTask);
    }


    // Getters & Setters
    public int getTaskId() {
        return idTask;
    }

    public void setTaskId(int idTask) {
        this.idTask = idTask;
    }

    public String getTaskName() {
        return nameTask;
    }

    public void setTaskName(String nameTask) {
        this.nameTask = nameTask;
    }

    public String getTaskDesc() {return descriptionTask;}

    public void setTaskDesc(String descriptionTask) { this.descriptionTask = descriptionTask;    }

    public int getTaskPriority() {return priorityTask;}

    public void setTaskPriority(int priorityTask) {
        this.priorityTask = priorityTask;
    }

    public Date getTaskEsStart() {
        return estimateStartDateTask;
    }

    public void setTaskEsStart(Date estimateStartDateTask) {
        this.estimateStartDateTask = estimateStartDateTask;
    }

    public Date getTaskEsEnd() {
        return estimateEndDateTask;
    }

    public void setTaskEsEnd(Date estimateEndDateTask) {
        this.estimateEndDateTask = estimateEndDateTask;
    }

    public Date getTaskStart() {
        return realStartDaTeTask;
    }

    public void setTaskStart(Date realStartDaTeTask) {
        this.realStartDaTeTask = realStartDaTeTask;
    }

    public Date getTaskEnd() {
        return realEndDateTask;
    }

    public void setTaskEnd(Date realEndDateTask) {
        this.realEndDateTask = realEndDateTask;
    }

    @Override
    public String toString() {
        return "Task{" +
                "idTask=" + idTask +
                ", nameTask='" + nameTask + '\'' +
                ", descriptionTask='" + descriptionTask + '\'' +
                ", priorityTask='" + priorityTask +
                ", estimateStartDateTask=" + estimateStartDateTask +
                ", estimateEndDateTask=" + estimateEndDateTask +
                ", realStartDaTeTask=" + realStartDaTeTask +
                ", realEndDateTask=" + realEndDateTask +
                '}';
    }

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
        if (priorityTask != task.priorityTask)
            return false;
        if (estimateStartDateTask != null ? !estimateStartDateTask.equals(task.estimateStartDateTask) : task.estimateStartDateTask != null)
            return false;
        if (estimateEndDateTask != null ? !estimateEndDateTask.equals(task.estimateEndDateTask) : task.estimateEndDateTask != null)
            return false;
        if (realStartDaTeTask != null ? !realStartDaTeTask.equals(task.realStartDaTeTask) : task.realStartDaTeTask != null)
            return false;
        if (realEndDateTask != null ? !realEndDateTask.equals(task.realEndDateTask) : task.realEndDateTask != null)
            return false;

        return false;
    }

    @Override
    public int hashCode() {
        int result = idTask;
        result += priorityTask;
        result = 31 * result + (nameTask != null ? nameTask.hashCode() : 0);
        result = 31 * result + (descriptionTask != null ? descriptionTask.hashCode() : 0);
        result = 31 * result + (estimateStartDateTask != null ? estimateStartDateTask.hashCode() : 0);
        result = 31 * result + (estimateEndDateTask != null ? estimateEndDateTask.hashCode() : 0);
        result = 31 * result + (realStartDaTeTask != null ? realStartDaTeTask.hashCode() : 0);
        result = 31 * result + (realEndDateTask != null ? realEndDateTask.hashCode() : 0);
        return result;
    }
}
