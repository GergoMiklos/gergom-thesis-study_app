package com.thesis.studyapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;

import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@NodeEntity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Group implements HasId {
    @Id
    //@GeneratedValue(strategy = CustomIdStrategy.class)
    @GeneratedValue
    private Long id;

    @Index(unique = true)
    private String code;
    private String name;
    private String description;
    private String news;
    private ZonedDateTime newsChangedDate;

    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPSTUDENT", direction = Relationship.INCOMING)
    private Set<User> students = new HashSet<>();
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPTEACHER", direction = Relationship.INCOMING)
    private Set<User> teachers = new HashSet<>();
    @JsonIgnore
    @EqualsAndHashCode.Exclude
    @Relationship(type = "GROUPTEST", direction = Relationship.OUTGOING)
    private Set<Test> tests = new HashSet<>();

    public void addStudent(User user) {
        if (students == null) {
            students = new HashSet<>();
        }
        students.add(user);
    }

    public void addTeacher(User user) {
        if (teachers == null) {
            teachers = new HashSet<>();
        }
        teachers.add(user);
    }

    public void addTest(Test test) {
        if (tests == null) {
            tests = new HashSet<>();
        }
        tests.add(test);
    }

    public static class GroupComparator implements Comparator<Group> {

        @Override public int compare(Group g1, Group g2) {
            return g1.getName().compareTo(g2.getName());
        }
    }

}
