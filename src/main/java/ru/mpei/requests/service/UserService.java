package ru.mpei.requests.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.mpei.requests.domain.chats.Chat;
import ru.mpei.requests.domain.users.Role;
import ru.mpei.requests.domain.users.User;
import ru.mpei.requests.repos.ChatRepo;
import ru.mpei.requests.repos.UserRepo;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service //All the business logic for users
public class UserService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo; //Used to work with users

    @Autowired
    private ChatRepo chatRepo; //Used to work with chats

    @Value("${upload.path}")
    private String uploadPath; //Path for uploading files

    @Autowired
    private PasswordEncoder passwordEncoder; //Used to encode passwords

    public void setUserAvatar(User user, MultipartFile file) throws IOException { //Setting the profile image for the user
        File uploadDir = new File(uploadPath + File.separator + "img" + File.separator);

        if(!uploadDir.exists()) { //If the file directory does not exist
            uploadDir.mkdir(); //We make it
        }

        String resultFileName = "";

        if(file != null && !file.getOriginalFilename().isEmpty()) {
            String uuidFile = UUID.randomUUID().toString(); //Unique ID to prevent collisions
            resultFileName = uuidFile + "." + file.getOriginalFilename();
            file.transferTo(new File(uploadPath + File.separator + "img"+ File.separator + resultFileName));
        } else {
            resultFileName = "defaultavatar.png";
        }
        user.setFileName(resultFileName);
    }

    public boolean isPossibleToCreateAUser(String username) { //If the user already exists in DB we cannot use this login
        User userFromDB = userRepo.findByUsername(username);
        return userFromDB == null;
    }

    public void createUser(User user) throws IOException {
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActivationCode(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        userRepo.save(user);

        //Gotta send the letter to the user with the activation code
    }

    public void createUserFromAdminPanel(User user,
                                         String email,
                                         String firstName,
                                         String secondName,
                                         String lastName) throws IOException {
        user.setUsername(email);
//        user.setLastName(lastName);
//        user.setSecondName(secondName);
//        user.setFirstName(firstName);
        setUserAvatar(user, null);
        user.setRoles(Collections.singleton(Role.USER));
        user.setActive(true);
        user.setPassword(passwordEncoder.encode("123"));
//        Chat chat = chatRepo.findByRequestIsNull();
        if (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.MODER)) {
//            chat.getMembers().add(user);
        }
        userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException { //Used for security purposes
        return userRepo.findByUsername(s);
    }

    public List<User> findAll() {
        return userRepo.findAll();
    }

    public void saveUser(User user, String username, Map<String, String> form) { //Saving the user from admin panel
        user.setUsername(username);
        setUserRolesFromRegForm(user, form);
//        Chat chat = chatRepo.findByRequestIsNull();
        if (user.getRoles().contains(Role.ADMIN) || user.getRoles().contains(Role.MODER)) {
//            chat.getMembers().add(user);
//            chatRepo.save(chat);
        }
        userRepo.save(user);
    }

    public void setUserRolesFromRegForm(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet()); //Getting the list of all roles from the form as a set
        user.setRoles(new HashSet<>());
        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }
    }

    @Transactional
    public void deleteUsersByNames(List<String> name) { //IS BUGGED
        for (String s: name) {
            userRepo.deleteByUsername(s);
        }
    }

    public void updateProfile(User user,
                              String password,
                              String firstName,
                              String secondName,
                              String lastName) { //updating the user profile
//        if (!StringUtils.isEmpty(firstName) && !firstName.equals(user.getFirstName()))
//            user.setFirstName(firstName);
//        if (!StringUtils.isEmpty(secondName) && !secondName.equals(user.getSecondName()))
//            user.setSecondName(secondName);
//        if (!StringUtils.isEmpty(lastName) && !lastName.equals(user.getLastName()))
//            user.setLastName(lastName);
//        if (!StringUtils.isEmpty(password))
//            user.setPassword(passwordEncoder.encode(password));
        userRepo.save(user);
    }

    public User findUserByQuery(String name) { //Getting one particular user by a string
        User user;
        user = userRepo.findByUsername(name);
//        if (user == null) {
//            user = userRepo.findByUsernameLike(name);
//        }
//        if (user == null) {
//            user = userRepo.findByFirstName(name);
//        }
//        if (user == null) {
//            user = userRepo.findBySecondName(name);
//        }
//        if (user == null) {
//            user = userRepo.findByLastName(name);
//        }
//        if (user == null) {
//            user = userRepo.findByFirstNameLike(name);
//        }
//        if (user == null) {
//            user = userRepo.findBySecondNameLike(name);
//        }
//        if (user == null) {
//            user = userRepo.findByLastNameLike(name);
//        }
        return user;
    }

    public String getStringRoleName(Role role) { //Giving the roles russian analogues
        if (role == Role.ADMIN)
            return "Администратор";
        if (role == Role.MODER)
            return "Модератор";
        if (role == Role.CLIENT)
            return "Клиент";
        if (role == Role.EXECUTER)
            return "Исполнитель";
        if (role == Role.USER)
            return "Пользователь";
        return "";
    }



    public List<User> getAllUsersByQuery(String query) { //Get all users by a string
//        List<User> firstNameList = userRepo.findAllByFirstNameContaining(query);
//        List<User> secondNameList = userRepo.findAllBySecondNameContaining(query);
//        List<User> lastNameList = userRepo.findAllByLastNameContaining(query);
        List<User> usernameList = userRepo.findAllByUsernameContaining(query);
//        List<User> resultList = ServiceUtils.collideLists(firstNameList, secondNameList);
//        ServiceUtils.collideLists(resultList, lastNameList);
//        ServiceUtils.collideLists(resultList, usernameList);
        return null;
    }

    public List<User> getAllModersAndAdmins() {
        List<User> admins = userRepo.findAllByRoles(Role.ADMIN);
        List<User> moders = userRepo.findAllByRoles(Role.MODER);
        return ServiceUtils.collideLists(admins, moders);
    }

    public List<User> getAllExecuters() {
        return userRepo.findAllByRoles(Role.EXECUTER);
    }

    public List<User> getExecutersByQuery(String query) {
        List<User> users = getAllUsersByQuery(query);
        users.removeIf(u -> !u.isExecuter());
        return users;
    }

    public List<User> getModersAndAdminsByQuery(String query) {
        List<User> users = getAllUsersByQuery(query);
        users.removeIf(u -> !u.isAdmin() && !u.isModer());
        return users;
    }

    public User getClientByQuery(String query) {
        List<User> users = getAllUsersByQuery(query);
        for (User user : users) {
            if (user.isClient())
                return user;
        }
        return null;
    }
}