package br.com.devinno.listedapi.dataTransferObject.response;

import br.com.devinno.listedapi.model.Access;
import br.com.devinno.listedapi.model.Project;
import br.com.devinno.listedapi.model.UserListed;

import java.time.LocalDate;

public class ResponseProjectDTO {

    private Long id;

    private String name;

    private String description;

    private int weekSprint;

    private boolean concluded;

    private LocalDate dateStart;

    private LocalDate dateEnd;

    private String role;

    public ResponseProjectDTO() {

    }

    public ResponseProjectDTO(Project project, UserListed user) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.weekSprint = project.getWeekSprint();
        this.concluded = project.isConcluded();
        this.dateStart = project.getDateStart();
        this.dateEnd = project.getDateEnd();
        this.role = null;

        if (project.getAccess() != null) {
            for (Access item : project.getAccess()) {
                if (item.getUser() == user)
                    this.role = item.getCategory().getName();
            }
        }
    }

    public ResponseProjectDTO(Project project, String role) {
        this.id = project.getId();
        this.name = project.getName();
        this.description = project.getDescription();
        this.weekSprint = project.getWeekSprint();
        this.concluded = project.isConcluded();
        this.dateStart = project.getDateStart();
        this.dateEnd = project.getDateEnd();
        this.role = role;
    }

    public ResponseProjectDTO(Long id, String name, String description, int weekSprint, boolean concluded, String creator,
                              LocalDate dateStart, LocalDate dateEnd) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weekSprint = weekSprint;
        this.concluded = concluded;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWeekSprint() {
        return weekSprint;
    }

    public void setWeekSprint(int weekSprint) {
        this.weekSprint = weekSprint;
    }

    public boolean isConcluded() {
        return concluded;
    }

    public void setConcluded(boolean concluded) {
        this.concluded = concluded;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
