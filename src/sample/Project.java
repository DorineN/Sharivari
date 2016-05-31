package sample;

import java.util.Date;

/*************************************************************
 *************** Project class *****************
 *************************************************************
 *********** Created by Dorine on 24/04/2016.*****************
 ************************************************************/

public class Project {
    protected int idProject = 0;
    protected String nameProject = "";
    protected String descriptionProject = "";
    protected Date startDateProject = null;
    protected Date realEndDaTeEndProject = null;
    protected Date estimateEndDateProject = null;

    public Project(){
        // Empty constructor
    }

    public Project(int idProject, String nameProject, String descriptionProject, Date startDateProject, Date realEndDaTeEndProject, Date estimateEndDateProject){
        this.setProjectId(idProject);
        this.setProjectName(nameProject);
        this.setProjectDesc(descriptionProject);
        this.setProjectStart(startDateProject);
        this.setProjectDeadline(realEndDaTeEndProject);
        this.setProjectEnd(estimateEndDateProject);
    }


    // Getters & Setters
    public int getProjectId() {
        return idProject;
    }

    public void setProjectId(int idProject) {
        this.idProject = idProject;
    }

    public String getProjectName() {
        return nameProject;
    }

    public void setProjectName(String nameProject) {
        this.nameProject = nameProject;
    }

    public String getProjectDesc() {
        return descriptionProject;
    }

    public void setProjectDesc(String descriptionProject) {
        this.descriptionProject = descriptionProject;
    }

    public Date getProjectStart() {
        return startDateProject;
    }

    public void setProjectStart(Date startDateProject) {
        this.startDateProject = startDateProject;
    }

    public Date getProjectDeadline() {
        return realEndDaTeEndProject;
    }

    public void setProjectDeadline(Date realEndDaTeEndProject) {
        this.realEndDaTeEndProject = realEndDaTeEndProject;
    }

    public Date getProjectEnd() {
        return estimateEndDateProject;
    }

    public void setProjectEnd(Date estimateEndDateProject) {
        this.estimateEndDateProject = estimateEndDateProject;
    }

    @Override
    public String toString() {
        return "Project{" +
                "idProject=" + idProject +
                ", nameProject='" + nameProject + '\'' +
                ", descriptionProject='" + descriptionProject + '\'' +
                ", startDateProject=" + startDateProject +
                ", realEndDaTeEndProject=" + realEndDaTeEndProject +
                ", estimateEndDateProject=" + estimateEndDateProject +
                '}';
    }

    // Inherited methods
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Project project = (Project) o;

        if (idProject != project.idProject)
            return false;
        if (nameProject != null ? !nameProject.equals(project.nameProject) : project.nameProject != null)
            return false;
        if (descriptionProject != null ? !descriptionProject.equals(project.descriptionProject) : project.descriptionProject != null)
            return false;
        if (startDateProject != null ? !startDateProject.equals(project.startDateProject) : project.startDateProject != null)
            return false;
        if (realEndDaTeEndProject != null ? !realEndDaTeEndProject.equals(project.realEndDaTeEndProject) : project.realEndDaTeEndProject != null)
            return false;
        if (estimateEndDateProject != null ? !estimateEndDateProject.equals(project.estimateEndDateProject) : project.estimateEndDateProject != null)
            return false;

        return false;
    }

    @Override
    public int hashCode() {
        int result = idProject;
        result = 31 * result + (nameProject != null ? nameProject.hashCode() : 0);
        result = 31 * result + (descriptionProject != null ? descriptionProject.hashCode() : 0);
        result = 31 * result + (startDateProject != null ? startDateProject.hashCode() : 0);
        result = 31 * result + (realEndDaTeEndProject != null ? realEndDaTeEndProject.hashCode() : 0);
        result = 31 * result + (estimateEndDateProject != null ? estimateEndDateProject.hashCode() : 0);
        return result;
    }
}