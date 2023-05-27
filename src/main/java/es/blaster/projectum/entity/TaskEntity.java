package es.blaster.projectum.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Table(name = "task")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })

public class TaskEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;    
    private int priority;
    private int complexity;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_project")
    private ProjectEntity project;

    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY)
    private final List<IssueEntity> issues;

    public TaskEntity() {
        this.issues = new ArrayList<>();
    }

    public TaskEntity(Long id) {
        this.issues = new ArrayList<>();
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getComplexity() {
        return complexity;
    }

    public void setComplexity(int complexity) {
        this.complexity = complexity;
    }

    public ProjectEntity getProject() {
        return project;
    }

    public void setProject(ProjectEntity project) {
        this.project = project;
    }

//    @PreRemove
//    public void nullify() {
//        this.issues.forEach(c -> c.setTask(null));
//    }

public int getIssues() {
    return issues.size();
}

}
