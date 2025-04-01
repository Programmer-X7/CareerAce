package com.bcet.resume_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bcet.resume_service.dto.ResumeCardDto;
import com.bcet.resume_service.exceptions.NotAllowedException;
import com.bcet.resume_service.model.Certification;
import com.bcet.resume_service.model.Education;
import com.bcet.resume_service.model.Experience;
import com.bcet.resume_service.model.Language;
import com.bcet.resume_service.model.Project;
import com.bcet.resume_service.model.Resume;
import com.bcet.resume_service.model.Skill;
import com.bcet.resume_service.repository.ResumeRepository;

import jakarta.ws.rs.NotFoundException;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private static final Logger logger = LoggerFactory.getLogger(ResumeServiceImpl.class);

    public ResumeServiceImpl(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    @Override
    public void createResume(Resume resume) {
        try {
            resumeRepository.save(resume);
        } catch (Exception e) {
            throw new RuntimeException("Error creating resume: " + e.getMessage());
        }
    }

    @Override
    public List<ResumeCardDto> getAllResumesByUserId(String userId) {
        Optional<List<Resume>> optionalResumes = resumeRepository.findByUserId(userId);
        if (!optionalResumes.isPresent()) {
            return new ArrayList<>();
        }

        return optionalResumes.get().stream()
                .map(resume -> new ResumeCardDto(
                        resume.getResumeId(),
                        resume.getTemplateId(),
                        resume.getTitle(),
                        resume.getThemeColor()))
                .toList();
    }

    @Override
    public Resume getResumeById(String resumeId) {
        try {
            return resumeRepository.findById(resumeId)
                    .orElseThrow(() -> new NotFoundException("No Resume found with id: " + resumeId));
        } catch (Exception e) {
            throw new RuntimeException("Error fetching resume: " + e.getMessage());
        }
    }

    @Override
    public Resume updateResume(String resumeId, Resume updatedResume) {
        try {
            // Fetch existing resume
            Optional<Resume> existingResumeOptional = resumeRepository.findById(resumeId);

            if (existingResumeOptional.isEmpty()) {
                throw new NotFoundException("No Resume found with id: " + resumeId);
            }

            Resume existingResume = existingResumeOptional.get();

            // Update only changed fields
            if (updatedResume.getTitle() != null) {
                existingResume.setTitle(updatedResume.getTitle());
            }
            if (updatedResume.getThemeColor() != null) {
                existingResume.setThemeColor(updatedResume.getThemeColor());
            }
            if (updatedResume.getFirstName() != null) {
                existingResume.setFirstName(updatedResume.getFirstName());
            }
            if (updatedResume.getLastName() != null) {
                existingResume.setLastName(updatedResume.getLastName());
            }
            if (updatedResume.getJobTitle() != null) {
                existingResume.setJobTitle(updatedResume.getJobTitle());
            }
            if (updatedResume.getAddress() != null) {
                existingResume.setAddress(updatedResume.getAddress());
            }
            if (updatedResume.getEmail() != null) {
                existingResume.setEmail(updatedResume.getEmail());
            }
            if (updatedResume.getPhone() != null) {
                existingResume.setPhone(updatedResume.getPhone());
            }
            if (updatedResume.getLinkedIn() != null) {
                existingResume.setLinkedIn(updatedResume.getLinkedIn());
            }
            if (updatedResume.getGithub() != null) {
                existingResume.setGithub(updatedResume.getGithub());
            }
            if (updatedResume.getPortfolio() != null) {
                existingResume.setPortfolio(updatedResume.getPortfolio());
            }
            if (updatedResume.getImageUrl() != null) {
                existingResume.setImageUrl(updatedResume.getImageUrl());
            }
            if (updatedResume.getSummary() != null) {
                existingResume.setSummary(updatedResume.getSummary());
            }

            // Handle the experience update (adding, updating, or removing experiences)
            if (updatedResume.getExperience() != null) {

                List<Experience> existingExperiences = existingResume.getExperience();
                List<Experience> updatedExperiences = updatedResume.getExperience();

                // Use a map for quick lookup of existing experiences by ID
                Map<String, Experience> existingExperienceMap = existingExperiences.stream()
                        .collect(Collectors.toMap(Experience::getId, Function.identity()));

                // Prepare to track experiences to remove
                List<Experience> experiencesToRemove = new ArrayList<>(existingExperiences);

                for (Experience updatedExperience : updatedExperiences) {
                    if (updatedExperience.getId() == null) {
                        updatedExperience.setId(UUID.randomUUID().toString());
                    }

                    Experience existingExperience = existingExperienceMap.get(updatedExperience.getId());
                    if (existingExperience != null) {
                        // Update existing experience
                        existingExperience.setTitle(updatedExperience.getTitle());
                        existingExperience.setCompanyName(updatedExperience.getCompanyName());
                        existingExperience.setCity(updatedExperience.getCity());
                        existingExperience.setState(updatedExperience.getState());
                        existingExperience.setStartDate(updatedExperience.getStartDate());
                        existingExperience.setEndDate(updatedExperience.getEndDate());
                        existingExperience.setCurrentlyWorking(updatedExperience.isCurrentlyWorking());
                        existingExperience.setWorkSummary(updatedExperience.getWorkSummary());
                        experiencesToRemove.remove(existingExperience); // Not an orphan
                    } else {
                        // Add new experience
                        updatedExperience.setResume(existingResume); // Set the relationship
                        existingResume.getExperience().add(updatedExperience);
                    }
                }

                // Remove orphaned experiences
                experiencesToRemove.forEach(existingResume.getExperience()::remove);
            }

            // Handle the projects update (adding, updating, or removing projects)
            if (updatedResume.getProjects() != null) {
                List<Project> existingProjects = existingResume.getProjects();
                List<Project> updatedProjects = updatedResume.getProjects();

                Map<String, Project> existingProjectMap = existingProjects.stream()
                        .collect(Collectors.toMap(Project::getId, Function.identity()));

                List<Project> projectsToRemove = new ArrayList<>(existingProjects);

                for (Project updatedProject : updatedProjects) {
                    if (updatedProject.getId() == null) {
                        updatedProject.setId(UUID.randomUUID().toString());
                    }

                    Project existingProject = existingProjectMap.get(updatedProject.getId());
                    if (existingProject != null) {
                        existingProject.setTitle(updatedProject.getTitle());
                        existingProject.setGithubUrl(updatedProject.getGithubUrl());
                        existingProject.setPreviewUrl(updatedProject.getPreviewUrl());
                        existingProject.setTechnologies(updatedProject.getTechnologies());
                        existingProject.setStartDate(updatedProject.getStartDate());
                        existingProject.setEndDate(updatedProject.getEndDate());
                        existingProject.setDescription(updatedProject.getDescription());
                        projectsToRemove.remove(existingProject);
                    } else {
                        updatedProject.setResume(existingResume);
                        existingResume.getProjects().add(updatedProject);
                    }
                }

                projectsToRemove.forEach(existingResume.getProjects()::remove);
            }

            // Handle the skills update (adding, updating, or removing skills)
            if (updatedResume.getSkills() != null) {
                List<Skill> existingSkills = existingResume.getSkills();
                List<Skill> updatedSkills = updatedResume.getSkills();

                Map<String, Skill> existingSkillMap = existingSkills.stream()
                        .collect(Collectors.toMap(Skill::getId, Function.identity()));

                List<Skill> skillsToRemove = new ArrayList<>(existingSkills);

                for (Skill updatedSkill : updatedSkills) {
                    if (updatedSkill.getId() == null) {
                        updatedSkill.setId(UUID.randomUUID().toString());
                    }

                    Skill existingSkill = existingSkillMap.get(updatedSkill.getId());
                    if (existingSkill != null) {
                        existingSkill.setName(updatedSkill.getName());
                        existingSkill.setRating(updatedSkill.getRating());
                        skillsToRemove.remove(existingSkill);
                    } else {
                        updatedSkill.setResume(existingResume);
                        existingResume.getSkills().add(updatedSkill);
                    }
                }

                skillsToRemove.forEach(existingResume.getSkills()::remove);
            }

            // Handle the education update (adding, updating, or removing education)
            if (updatedResume.getEducation() != null) {
                List<Education> existingEducation = existingResume.getEducation();
                List<Education> updatedEducation = updatedResume.getEducation();

                Map<String, Education> existingEducationMap = existingEducation.stream()
                        .collect(Collectors.toMap(Education::getId, Function.identity()));

                List<Education> educationToRemove = new ArrayList<>(existingEducation);

                for (Education updatedEdu : updatedEducation) {
                    if (updatedEdu.getId() == null) {
                        updatedEdu.setId(UUID.randomUUID().toString());
                    }

                    Education existingEdu = existingEducationMap.get(updatedEdu.getId());
                    if (existingEdu != null) {
                        existingEdu.setUniversityName(updatedEdu.getUniversityName());
                        existingEdu.setDegree(updatedEdu.getDegree());
                        existingEdu.setMajor(updatedEdu.getMajor());
                        existingEdu.setStartDate(updatedEdu.getStartDate());
                        existingEdu.setEndDate(updatedEdu.getEndDate());
                        existingEdu.setDescription(updatedEdu.getDescription());
                        educationToRemove.remove(existingEdu); // Not an orphan
                    } else {
                        updatedEdu.setResume(existingResume); // Set the relationship
                        existingResume.getEducation().add(updatedEdu);
                    }
                }

                educationToRemove.forEach(existingResume.getEducation()::remove);
            }

            // Handle the certification update (adding, updating, or removing
            // certifications)
            if (updatedResume.getCertifications() != null) {
                List<Certification> existingCertifications = existingResume.getCertifications();
                List<Certification> updatedCertifications = updatedResume.getCertifications();

                Map<String, Certification> existingCertificationMap = existingCertifications.stream()
                        .collect(Collectors.toMap(Certification::getId, Function.identity()));

                List<Certification> certificationsToRemove = new ArrayList<>(existingCertifications);

                for (Certification updatedCertification : updatedCertifications) {
                    if (updatedCertification.getId() == null) {
                        updatedCertification.setId(UUID.randomUUID().toString());
                    }

                    Certification existingCertification = existingCertificationMap.get(updatedCertification.getId());
                    if (existingCertification != null) {
                        existingCertification.setTitle(updatedCertification.getTitle());
                        existingCertification.setIssuer(updatedCertification.getIssuer());
                        existingCertification.setIssueDate(updatedCertification.getIssueDate());
                        certificationsToRemove.remove(existingCertification); // Not an orphan
                    } else {
                        updatedCertification.setResume(existingResume); // Set the relationship
                        existingResume.getCertifications().add(updatedCertification);
                    }
                }

                certificationsToRemove.forEach(existingResume.getCertifications()::remove);
            }

            // Handle the language update (adding, updating, or removing languages)
            if (updatedResume.getLanguages() != null) {
                List<Language> existingLanguages = existingResume.getLanguages();
                List<Language> updatedLanguages = updatedResume.getLanguages();

                Map<String, Language> existingLanguageMap = existingLanguages.stream()
                        .collect(Collectors.toMap(Language::getId, Function.identity()));

                List<Language> languagesToRemove = new ArrayList<>(existingLanguages);

                for (Language updatedLanguage : updatedLanguages) {
                    if (updatedLanguage.getId() == null) {
                        updatedLanguage.setId(UUID.randomUUID().toString());
                    }

                    Language existingLanguage = existingLanguageMap.get(updatedLanguage.getId());
                    if (existingLanguage != null) {
                        existingLanguage.setName(updatedLanguage.getName());
                        existingLanguage.setProficiency(updatedLanguage.getProficiency());
                        languagesToRemove.remove(existingLanguage); // Not an orphan
                    } else {
                        updatedLanguage.setResume(existingResume); // Set the relationship
                        existingResume.getLanguages().add(updatedLanguage);
                    }
                }

                languagesToRemove.forEach(existingResume.getLanguages()::remove);
            }

            if (updatedResume.getInterests() != null) {
                existingResume.setInterests(updatedResume.getInterests());
            }

            return resumeRepository.save(existingResume);

        } catch (Exception e) {
            logger.error("Error updating resume: {}", e.getMessage());
            throw new RuntimeException("Error updating resume: " + e.getMessage());
        }
    }

    @Override
    public void deleteResume(String resumeId, String userId) {
        try {
            // Find is resume exists
            Optional<Resume> existingResume = resumeRepository.findById(resumeId);
            if (!existingResume.isPresent()) {
                throw new NotFoundException("No Resume found with id: " + resumeId);
            }

            // Check if the user trying do delete is the owner of the resume
            if (!existingResume.get().getUserId().equals(userId)) {
                throw new NotAllowedException("You are not authorized to delete this resume");
            }

            // Delete resume
            resumeRepository.deleteById(resumeId);

        } catch (Exception e) {
            throw new RuntimeException("Error deleting resume: " + e.getMessage());
        }
    }

}
