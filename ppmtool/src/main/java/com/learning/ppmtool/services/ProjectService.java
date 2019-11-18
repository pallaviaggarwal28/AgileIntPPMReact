package com.learning.ppmtool.services;

import com.learning.ppmtool.domain.Backlog;
import com.learning.ppmtool.domain.Project;
import com.learning.ppmtool.exceptions.ProjectIdException;
import com.learning.ppmtool.repositories.BacklogRepository;
import com.learning.ppmtool.repositories.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private BacklogRepository backlogRepository;

    public Project saveOrUpdateProject(Project project) {
        String projectIdentifier = project.getProjectIdentifier().toUpperCase();
        try
        {
            project.setProjectIdentifier(projectIdentifier);
            if(project.getId()==null) {
                Backlog backlog = new Backlog();
                project.setBacklog(backlog);
                backlog.setProject(project);
                backlog.setProjectIdentifier(projectIdentifier);
            }

            else
                project.setBacklog(backlogRepository.findByProjectIdentifier(projectIdentifier));
            return projectRepository.save(project);
        }
        catch(Exception e) {
            throw new ProjectIdException("Project ID '"+ projectIdentifier +"' already exists");
        }
    }

    public Project findProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
        if(null==project) {
            throw new ProjectIdException("Project ID '"+projectId.toUpperCase() +"' does not exist");
        }
        return project;
    }

    public Iterable<Project> findAllProjects() {
        return projectRepository.findAll();
    }

    public void deleteProjectByIdentifier(String projectId) {
        Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

        if(null==project)
            throw  new  ProjectIdException("Cannot Project with ID '"+projectId.toUpperCase()+"'. This project does not exist");
        projectRepository.delete(project);
    }
}
