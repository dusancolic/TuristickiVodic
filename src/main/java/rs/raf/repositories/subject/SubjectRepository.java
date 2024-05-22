package rs.raf.repositories.subject;

import rs.raf.entities.Subject;

import java.util.List;

public interface SubjectRepository {
    public Subject addSubject(Subject subject);
    public List<Subject> allSubjects();
    public Subject findSubject(Integer id);
    public void deleteSubject(Integer id);
}
