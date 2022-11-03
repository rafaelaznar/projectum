package net.ausiasmarch.andamio.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.OneToMany;

@Table(name = "issue")
@Entity
public class IssueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime open_datetime;
    private String observations;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_developer")
    private DeveloperEntity developer;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_task")
    private TaskEntity task;

    @OneToMany(mappedBy = "issue", fetch = FetchType.LAZY)
    private final List<ResolutionEntity> resolutions;

    private int value;

    public IssueEntity() {
        this.resolutions = new ArrayList<>();
    }

    public IssueEntity(Long id, LocalDateTime open_datetime, String observations, DeveloperEntity developer, TaskEntity task, int value) {
        this.id = id;
        this.open_datetime = open_datetime;
        this.observations = observations;
        this.developer = developer;
        this.task = task;
        this.value = value;
        this.resolutions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getOpen_datetime() {
        return open_datetime;
    }

    public void setOpen_datetime(LocalDateTime open_datetime) {
        this.open_datetime = open_datetime;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public DeveloperEntity getDeveloper() {
        return developer;
    }

    public void setDeveloper(DeveloperEntity developer) {
        this.developer = developer;
    }

    public TaskEntity getTask() {
        return task;
    }

    public int getValue() {
        return value;
    }

    public int getResolutions() {
        return this.resolutions.size();
    }

    public void setTask(TaskEntity task) {
        this.task = task;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
