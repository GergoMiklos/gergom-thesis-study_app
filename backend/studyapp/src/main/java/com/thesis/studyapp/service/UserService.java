package com.thesis.studyapp.service;

import com.thesis.studyapp.exception.CustomGraphQLException;
import com.thesis.studyapp.model.Group;
import com.thesis.studyapp.model.User;
import com.thesis.studyapp.repository.GroupRepository;
import com.thesis.studyapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    public User getUser(Long userId) {
        return getUserById(userId, 1);
    }

    public List<User> getUsersByIds(List<Long> ids) {
        return userRepository.findByIdIn(ids, 1);
    }

    //todo userteststatus minden teszthez a groupban
    @Transactional
    public User addStudentFromCodeToGroup(Long groupId, String studentCode) {
        User user = getUserByCode(studentCode, 1);
        Group group = getGroupById(groupId, 0);
        user.addStudentGroup(group);
        return userRepository.save(user, 1);
    }

    @Transactional
    public User addTeacherFromCodeToGroup(Long groupId, String teacherCode) {
        User user = getUserByCode(teacherCode, 1);
        Group group = getGroupById(groupId, 0);
        user.addTeacherGroup(group);
        return userRepository.save(user, 1);
    }

    @Transactional
    public User addStudentFromCodeToParent(Long parentId, String studentCode) {
        User student = getUserByCode(studentCode, 1);
        User parent = getUserById(parentId, 0);
        student.addParent(parent);
        return userRepository.save(student, 1);
    }

    @Transactional
    public void deleteStudentFromParent(Long parentId, Long studentId) {
        if (userRepository.existsById(parentId) && userRepository.existsById(parentId)) {
            userRepository.deleteFollowedStudent(parentId, studentId);
        } else {
            throw new CustomGraphQLException("No user with id: " + parentId + " or " + studentId);
        }
    }

    public List<User> getStudentsForGroup(Long groupId) {
        //todo check group? NE, ez query (BONTSUK SZÉT ESZERINT? KÜLÖNÁLLÓ QUERY SERVICEK, MUTATIONOK PEDIG EZEKET HASZNÁLJÁK)
        // HISZEN VANNAK KÜLÖNBSÉGEK, ILYENKOR PL NINCS EXCEPTION DOBÁS
        // egy GRAPHQL ALKALMAZÁSBAN SOKK AZ ÖSSZEFÜGGÉS, DE MEGKELL PRÓBÁLNI CSÖKKENTENI AZOKAT7
        // BEVÁLLT MÓDSZER NINCS :(
        return userRepository.findByStudentGroupsIdOrderByName(groupId, 1);
    }

    public List<User> getTeachersForGroup(Long groupId) {
        return userRepository.findByTeacherGroupsIdOrderByName(groupId, 1);
    }

    public List<User> getStudentsForParent(Long parentId) {
        return userRepository.findByParentsIdOrderByName(parentId, 1);
    }

    private User getUserById(Long userId, int depth) {
        return userRepository.findById(userId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No user with id: " + userId));
    }

    private User getUserByCode(String userCode, int depth) {
        return userRepository.findByCode(userCode.toUpperCase(), depth)
                .orElseThrow(() -> new CustomGraphQLException("No user with code: " + userCode));
    }

    private Group getGroupById(Long groupId, int depth) {
        return groupRepository.findById(groupId, depth)
                .orElseThrow(() -> new CustomGraphQLException("No group with id: " + groupId));
    }


}
